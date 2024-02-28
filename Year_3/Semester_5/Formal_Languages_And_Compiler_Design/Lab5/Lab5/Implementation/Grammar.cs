using System.Text.RegularExpressions;

namespace Lab5.Implementation;

public class Grammar
{
    public List<string> NonterminalSymbols { get; set; }
    public List<string> TerminalSymbols { get; set; }
    public List<Production> Productions { get; set; }
    public string StartingSymbol { get; set; }
    private string PathToFile;

    public Grammar(string pathToFile)
    {
        NonterminalSymbols = new List<string>();
        TerminalSymbols = new List<string>();
        Productions = new List<Production>();
        PathToFile = pathToFile;
        
        InitFromFile();
    }

    public void PrintNonterminalSymbols()
    {
        PrintListOfStrings("N", NonterminalSymbols);
    }

    public void PrintTerminalSymbols()
    {
        PrintListOfStrings("E", TerminalSymbols);
    }
    
    public void PrintAllProductions()
    {
        Console.WriteLine("P = {");
        for (int i = 0; i < Productions.Count; i++)
        {
            Console.WriteLine($"\t{Productions[i].LeftHandSide} -> {Productions[i].RightHandSide}");
        }
        Console.WriteLine("}");
    }

    public bool CheckCFG()
    {
        foreach (var production in Productions)
        {
            if (!NonterminalSymbols.Contains(production.LeftHandSide))
            {
                return false;
            }

            foreach (var symbol in production.RightHandSide.Split(" "))
            {
                if (!NonterminalSymbols.Contains(symbol) && !TerminalSymbols.Contains(symbol))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void PrintProductionForANonterminal(string nonterminal)
    {
        for (int i = 0; i < Productions.Count; i++)
        {
            if (Productions[i].LeftHandSide == nonterminal)
            {
                Console.WriteLine($"{Productions[i].LeftHandSide} -> {Productions[i].RightHandSide}");
            }
        }
    }

    public void PrintStartingSymbol()
    {
        Console.WriteLine($"S = {StartingSymbol}");
    }

    private void InitFromFile()
    {
        Regex regex = new Regex("^([a-zA-Z _]*=)");
        foreach (string line in File.ReadLines(PathToFile))
        {
            Match match = regex.Match(line);
            if (!match.Success)
            {
                throw new Exception($"Invalid line: {line}");
            }

            switch (match.Value)
            {
                case "N=":
                    NonterminalSymbols = GetLineRightHandSide(line).Split(",").ToList();
                    break;

                case "E=":
                    TerminalSymbols = GetLineRightHandSide(line).Split(",").ToList();
                    break;

                case "P=":
                    List<string> productionStrings = GetLineRightHandSide(line).Split("|").ToList();
                    foreach (string productionString in productionStrings)
                    {
                        List<string> productionTokens = productionString.Split("->").ToList();
                        Productions.Add(new Production(productionTokens[0], productionTokens[1]));
                    }

                    break;

                case "S=":
                    StartingSymbol = line.Substring(line.IndexOf("=") + 1);
                    break;

                default:
                    Console.WriteLine("Unexpected line");
                    break;
            }
        }
    }
    
    private string GetLineRightHandSide(string line)
    {
        string rightHandSide = line.Substring(line.IndexOf("=") + 1);
        
        return rightHandSide.Substring(1, rightHandSide.Length - 2);
    }
    
    private void PrintListOfStrings(string name, List<string> list)
    {
        Console.Write($"{name} = {{");
        for (int i = 0; i < list.Count; i++)
        {
            Console.Write(i == list.Count - 1 ? $"{list[i]}" : $"{list[i]},");
        }
        Console.WriteLine("}");
    }
}