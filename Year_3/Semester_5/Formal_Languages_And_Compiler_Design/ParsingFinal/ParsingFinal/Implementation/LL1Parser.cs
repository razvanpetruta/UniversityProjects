using ParsingFinal.Utils;

namespace ParsingFinal.Implementation;

public class LL1Parser
{
    public readonly Grammar grammar;
    private Dictionary<string, HashSet<string>> _firstDictionary;
    private Dictionary<string, HashSet<string>> _followDictionary;
    private Dictionary<Pair<string, string>, Pair<string, int>> _parseTable;

    public LL1Parser(Grammar grammar)
    {
        this.grammar = grammar;
        _firstDictionary = new Dictionary<string, HashSet<string>>();
        _followDictionary = new Dictionary<string, HashSet<string>>();
        _parseTable = new Dictionary<Pair<string, string>, Pair<string, int>>();
        
        GenerateFirstDictionary();
        GenerateFollowDictionary();
        GenerateParseTable();
    }

    private HashSet<string> PerformConcatenationOfSizeOne(List<string> nonterminals, string terminal)
    {
        var concatenation = new HashSet<string>();
        var allContainEpsilon = true;
        
        foreach (var nonterminal in nonterminals)
        {
            if (!_firstDictionary[nonterminal].Contains("ε"))
                allContainEpsilon = false;
        }

        // if all nonterminals up until terminal can derive ε, we also add the terminal
        if (allContainEpsilon)
            concatenation.Add(terminal ?? "ε");
        
        var step = 0;
        while (step < nonterminals.Count)
        {
            var thereIsOneEpsilon = false;
            foreach (var s in _firstDictionary[nonterminals[step]])
            {
                if (s == "ε")
                    thereIsOneEpsilon = true;
                else
                    concatenation.Add(s);
            }

            if (thereIsOneEpsilon)
                step++;
            else
                break;
        }
        
        return concatenation;
    }

    private void GenerateFirstDictionary()
    {
        foreach (var nonterminal in grammar.NonterminalSymbols)
        {
            _firstDictionary[nonterminal] = new HashSet<string>();
            var productionsForNonterminal = grammar.GetProductionLHSsForANonterminal(nonterminal);
            foreach (var production in productionsForNonterminal)
            {
                string firstElementOfRHS = production.RightHandSide.Split(" ")[0];
                if (grammar.TerminalSymbols.Contains(firstElementOfRHS) || firstElementOfRHS == "ε")
                    _firstDictionary[nonterminal].Add(firstElementOfRHS);
            }
        }

        var isChanged = true;
        while (isChanged)
        {
            isChanged = false;
            var newColumn = new Dictionary<string, HashSet<string>>();

            foreach (var nonterminal in grammar.NonterminalSymbols)
            {
                var productionsForNonterminal = grammar.GetProductionLHSsForANonterminal(nonterminal);
                var toAdd = new HashSet<string>(_firstDictionary[nonterminal]);
                foreach (var production in productionsForNonterminal)
                {
                    var nonterminalsRHS = new List<string>();
                    string rhsTerminal = null;
                    foreach (var symbol in production.RightHandSide.Split(" "))
                    {
                        if (grammar.NonterminalSymbols.Contains(symbol))
                            nonterminalsRHS.Add(symbol);
                        else
                        {
                            rhsTerminal = symbol;
                            break;
                        }
                    }
                    toAdd.UnionWith(PerformConcatenationOfSizeOne(nonterminalsRHS, rhsTerminal));
                }
                
                if (!toAdd.SetEquals(_firstDictionary[nonterminal]))
                    isChanged = true;
                
                newColumn[nonterminal] = toAdd;
            }
            
            _firstDictionary = newColumn;
        }
    }
    
    public void PrintFirst()
    {
        foreach (var kv in _firstDictionary)
        {
            Console.WriteLine($"{kv.Key}: {string.Join(", ", kv.Value)}");
        }
    }

    private void GenerateFollowDictionary()
    {
        foreach (var nonterminal in grammar.NonterminalSymbols)
        {
            _followDictionary[nonterminal] = new HashSet<string>();
        }
        _followDictionary[grammar.StartingSymbol].Add("ε");

        var isChanged = true;
        while (isChanged)
        {
            isChanged = false;
            var newColumn = new Dictionary<string, HashSet<string>>();

            foreach (var nonterminal in grammar.NonterminalSymbols)
            {
                newColumn[nonterminal] = new HashSet<string>();

                var toAdd = new HashSet<string>(_followDictionary[nonterminal]);
                foreach (var production in grammar.GetProductionRHSsForANonterminal(nonterminal))
                {
                    var rhsValues = production.RightHandSide.Split(" ");
                    for (var i = 0; i < rhsValues.Length; i++)
                    {
                        if (rhsValues[i] == nonterminal)
                        {
                            if (i + 1 == rhsValues.Length)
                                toAdd.UnionWith(_followDictionary[production.LeftHandSide]);
                            else
                            {
                                var nextSymbol = rhsValues[i + 1];
                                if (grammar.TerminalSymbols.Contains(nextSymbol))
                                    toAdd.Add(nextSymbol);
                                else
                                {
                                    foreach (var symbol in _firstDictionary[nextSymbol])
                                    {
                                        if (symbol == "ε")
                                            toAdd.UnionWith(_followDictionary[production.LeftHandSide]);
                                        else
                                            toAdd.UnionWith(_firstDictionary[nextSymbol]);
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (!toAdd.SetEquals(_followDictionary[nonterminal]))
                    isChanged = true;
                
                newColumn[nonterminal] = toAdd;
            }

            _followDictionary = newColumn;
        }
    }
    
    public void PrintFollow()
    {
        foreach (var kv in _followDictionary)
        {
            Console.WriteLine($"{kv.Key}: {string.Join(", ", kv.Value)}");
        }
    }

    public void GenerateParseTable()
    {
        try
        {
            List<string> rows = new List<string>();
            rows.AddRange(grammar.NonterminalSymbols);
            rows.AddRange(grammar.TerminalSymbols);
            rows.Add("$");

            List<string> columns = new List<string>();
            columns.AddRange(grammar.TerminalSymbols);
            columns.Add("$");

            // initialise with -1 all the cells of the Matrix (parse table)
            foreach (var row in rows)
                foreach (var column in columns)
                    _parseTable[new Pair<string, string>(row, column)] = new Pair<string, int>("err", -1);

            // for each terminal set to pop
            foreach (var column in columns)
                _parseTable[new Pair<string, string>(column, column)] = new Pair<string, int>("pop", -1);

            // update for $, $
            _parseTable[new Pair<string, string>("$", "$")] = new Pair<string, int>("acc", -1);

            for (int i = 0; i < grammar.Productions.Count; i++)
            {
                var lhs = grammar.Productions[i].LeftHandSide;
                var rhs = grammar.Productions[i].RightHandSide;
                var rhsAsArray = rhs.Split(" ");
                
                var firstSymbol = rhsAsArray[0];
                if (grammar.TerminalSymbols.Contains(firstSymbol))
                {
                    var key = new Pair<string, string>(lhs, firstSymbol);
                    CheckForConflicts(key);
                    _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                }
                else if (grammar.NonterminalSymbols.Contains(firstSymbol))
                {
                    foreach (var firstEl in _firstDictionary[firstSymbol])
                    {
                        if (firstEl == "ε")
                        {
                            var key = new Pair<string, string>(lhs, "$");
                            CheckForConflicts(key);
                            _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                            foreach (var followEl in _followDictionary[lhs])
                            {
                                if (followEl == "ε")
                                {
                                    key = new Pair<string, string>(lhs, "$");
                                    CheckForConflicts(key);
                                    _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                                }
                                else
                                {
                                    key = new Pair<string, string>(lhs, followEl);
                                    CheckForConflicts(key);
                                    _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                                }
                            }
                        }
                        else
                        {
                            var key = new Pair<string, string>(lhs, firstEl);
                            CheckForConflicts(key);
                            _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                        }
                    }
                }
                else if (firstSymbol == "ε")
                {
                    foreach (var followEl in _followDictionary[lhs])
                    {
                        if (followEl == "ε")
                        {
                            var key = new Pair<string, string>(lhs, "$");
                            CheckForConflicts(key);
                            _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                        }
                        else
                        {
                            var key = new Pair<string, string>(lhs, followEl);
                            CheckForConflicts(key);
                            _parseTable[key] = new Pair<string, int>(rhs, i + 1);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }

    private void CheckForConflicts(Pair<string, string> key)
    {
        if (_parseTable[key].First != "err")
            throw new Exception($"Conflict: ({key.First}, {key.Second})");
    }
    
    public void PrintParseTable()
    {
        var rows = _parseTable.Keys.Select(k => k.First).Distinct().ToList();
        var columns = _parseTable.Keys.Select(k => k.Second).Distinct().ToList();

        int maxColumnWidth = 15;

        // Print header row
        Console.Write("Key".PadRight(maxColumnWidth));
        foreach (var column in columns)
        {
            Console.Write(column.PadRight(maxColumnWidth));
        }
        Console.WriteLine("\n");

        // Print each row with values
        foreach (var row in rows)
        {
            Console.Write(row.PadRight(maxColumnWidth));
            foreach (var column in columns)
            {
                var key = new Pair<string, string>(row, column);
                string value;
                if (_parseTable.ContainsKey(key))
                {
                    var tableValue = _parseTable[key];
                    if (tableValue.First == "err" && tableValue.Second == -1)
                        value = " ";
                    else
                        value = $"{tableValue.First}, {tableValue.Second}";
                }
                else
                    value = "unknown";
                Console.Write(value.PadRight(maxColumnWidth));
            }
            Console.WriteLine("\n");
        }
    }

    public string ParseSequence(List<string> sequence)
    {
        Stack<string> alpha = new Stack<string>();
        Stack<string> beta = new Stack<string>();
        List<int> pi = new List<int>();
        
        alpha.Push("$");
        for (int i = sequence.Count - 1; i >= 0; i--)
            alpha.Push(sequence[i]);
        
        beta.Push("$");
        beta.Push(grammar.StartingSymbol);

        while (!(alpha.Peek() == "$" && beta.Peek() == "$"))
        {
            string alphaPeek = alpha.Peek();
            string betaPeek = beta.Peek();
            var key = new Pair<string, string>(betaPeek, alphaPeek);
            var value = _parseTable[key];

            if (value.First == "err")
            {
                Console.WriteLine("Syntax error for key " + key);
                Console.WriteLine(alpha);
                Console.WriteLine(beta);
                return "";
            }

            if (value.First == "pop")
            {
                alpha.Pop();
                beta.Pop();
            }
            else
            {
                beta.Pop();
                if (value.First != "ε")
                {
                    string[] val = value.First.Split(" ");
                    for (int i = val.Length - 1; i >= 0; --i)
                        beta.Push(val[i]);
                }
                pi.Add(value.Second);
            }
        }

        var parsingResult = "";
        foreach (var el in pi)
            parsingResult += $"{el}";
        
        return parsingResult;
    }
}