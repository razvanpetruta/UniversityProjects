using System.Text.RegularExpressions;

namespace Lab3;

public class Scanner
{
    private string _program;
    private List<string> _reservedWords;
    private List<string> _otherTokens;
    private SymbolTable _identifiersSymbolTable;
    private SymbolTable _constantsSymbolTable;
    private ProgramInternalForm _PIF;
    private int _index = 0;
    private int _currentLine = 1;
    private int _currentLineIndex = 1;

    public Scanner()
    {
        _reservedWords = new List<string>();
        _otherTokens = new List<string>();
        ResetFields();
        try
        {
            ReadTokens();
        }
        catch (IOException e)
        {
            Console.WriteLine(e.Message);
        }
    }

    public void Scan(string pathToProgram)
    {
        try
        {
            ResetFields();
            _program = File.ReadAllText(pathToProgram);

            while (_index < _program.Length)
            {
                NextToken();
            }

            string filename = pathToProgram.Split("/")[1];
            using (StreamWriter pifWriter = new StreamWriter($"Output/PIF{filename}"))
            {
                pifWriter.WriteLine(_PIF.ToString());
            }

            using (StreamWriter symbolTableWriter = new StreamWriter($"Output/ST{filename}"))
            {
                symbolTableWriter.WriteLine("The symbol table has a 3 hash tables: identifiers, int constants and string constants");
                symbolTableWriter.WriteLine("Identifiers Symbol Table:");
                symbolTableWriter.Write(_identifiersSymbolTable);
                symbolTableWriter.WriteLine("\nConstants Symbol Table:");
                symbolTableWriter.Write(_constantsSymbolTable);
            }
            
            Console.WriteLine($"{filename} is lexically correct");
        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }
    }

    private void ResetFields()
    {
        _identifiersSymbolTable = new SymbolTable(11);
        _constantsSymbolTable = new SymbolTable(11);
        _PIF = new ProgramInternalForm();
        _index = 0;
        _currentLine = 1;
        _currentLineIndex = 1;
    }

    private void ReadTokens()
    {
        using StreamReader reader = new StreamReader("Resources/token.in");
        string line;
        while ((line = reader.ReadLine()) != null)
        {
            string token = line.Trim();
            switch (token)
            {
                case "int":
                case "string":
                case "char":
                case "if":
                case "else":
                case "while":
                case "for":
                case "read":
                case "write":
                case "vector":
                    _reservedWords.Add(token);
                    break;
                default:
                    _otherTokens.Add(token);
                    break;
            }
        }
    }
    
    private void NextToken()
    {
        SkipWhitespaces();
        
        if (_index == _program.Length)
        {
            return;
        }
        
        if (CheckStringConstant())
        {
            return;
        }
        
        if (CheckIntConstant())
        {
            return;
        }

        if (CheckTokens())
        {
            return;
        }
        
        if (CheckIdentifiers())
        {
            return;
        }

        throw new ScannerException($"Lexical error: invalid token at line {_currentLine}, index {_currentLineIndex}");
    }

    private void SkipWhitespaces()
    {
        while (_index < _program.Length && char.IsWhiteSpace(_program[_index]))
        {
            if (_program[_index] == '\n')
            {
                _currentLine++;
                _currentLineIndex = 0;
            }
            _index++;
            _currentLineIndex++;
        }
    }

    private bool CheckTokens()
    {
        string possibleToken = _program.Substring(_index).Split(" ")[0];
        
        foreach (var reservedWord in _reservedWords)
        {
            // use starts with in case there is no space after the token
            if (possibleToken.StartsWith(reservedWord))
            {
                int endIndex = _index + reservedWord.Length;
                // check if it might be an identifier starting with a keyword
                if (endIndex < _program.Length &&
                    (char.IsLetterOrDigit(_program[endIndex]) || _program[endIndex] == '_'))
                {
                    return false;
                }
                
                _index += reservedWord.Length;
                _currentLineIndex += reservedWord.Length;
                _PIF.AddElement(new KeyValuePair<string, KeyValuePair<int, int>>(reservedWord, new KeyValuePair<int, int>(-1, -1)));
                
                return true;
            }
        }

        // use starts with in case there is no space after the token
        foreach (var token in _otherTokens)
        {
            if (possibleToken.StartsWith(token))
            {
                _index += token.Length;
                _currentLineIndex += token.Length;
                _PIF.AddElement(new KeyValuePair<string, KeyValuePair<int, int>>(token, new KeyValuePair<int, int>(-1, -1)));
                
                return true;
            }
        }

        return false;
    }

    private bool CheckIdentifiers()
    {
        Regex regex = new Regex("^([a-zA-Z_][a-zA-Z0-9_]*)");
        Match match = regex.Match(_program.Substring(_index));
        
        if (!match.Success)
        {
            return false;
        }
        
        string identifier = match.Value;
        _index += identifier.Length;
        _currentLineIndex += identifier.Length;
        KeyValuePair<int, int> position;
        try
        {
            position = _identifiersSymbolTable.AddIdentifier(identifier);
        }
        catch (Exception e)
        {
            position = _identifiersSymbolTable.GetIdentifierPosition(identifier);
        }
        
        _PIF.AddElement(new KeyValuePair<string, KeyValuePair<int, int>>("identifier", position));
        
        return true;
    }

    private bool CheckStringConstant()
    {
        Regex regex = new Regex("^\".*\"");
        Match match = regex.Match(_program.Substring(_index));
        
        if (!match.Success)
        {
            if (new Regex("^\"[^\"]").Match(_program.Substring(_index)).Success)
            {
                throw new ScannerException($"Missing \" at line {_currentLine}");
            }
            return false;
        }
        
        string stringConstant = match.Value;
        _index += stringConstant.Length;
        _currentLineIndex += stringConstant.Length;
        KeyValuePair<int, int> position;
        try
        {
            position = _constantsSymbolTable.AddStringConstant(stringConstant);
        }
        catch (Exception e)
        {
            position = _constantsSymbolTable.GetStringConstantPosition(stringConstant);
        }
        
        _PIF.AddElement(new KeyValuePair<string, KeyValuePair<int, int>>("string const", position));
        
        return true;
    }

    private bool CheckIntConstant()
    {
        Regex regex = new Regex("^(0|[+-]?[1-9][0-9]*)");
        Match match = regex.Match(_program.Substring(_index));

        if (!match.Success)
        {
            return false;
        }
        
        // identifiers cannot start with digits
        if (new Regex("^(0|[+-]?[1-9][0-9]*)[a-zA-Z_]").Match(_program.Substring(_index)).Success)
        {
            return false;
        }
        
        var intConstant = match.Value;
        _index += intConstant.Length;
        _currentLineIndex += intConstant.Length;
        KeyValuePair<int, int> position;
        try
        {
            position = _constantsSymbolTable.AddIntConstant(int.Parse(intConstant));
        }
        catch (Exception e)
        {
            position = _constantsSymbolTable.GetIntConstantPosition(int.Parse(intConstant));
        }
        
        _PIF.AddElement(new KeyValuePair<string, KeyValuePair<int, int>>("int const", position));
        
        return true;
    }
}