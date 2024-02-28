using Lab7.Implementation;

namespace Lab7.Utils;

public class Menu
{
    public void Run()
    {
        Grammar grammar = new Grammar("Resources/seminar8ex1.txt");
        LL1Parser parser = new LL1Parser(grammar);
        ParserOutput parserOutput = new ParserOutput(parser);
        
        Console.WriteLine("0: EXIT");
        Console.WriteLine("1: Show nonterminal symbols");
        Console.WriteLine("2: Show terminal symbols");
        Console.WriteLine("3: Show set of productions");
        Console.WriteLine("4: Show productions for a given nonterminal (LHS)");
        Console.WriteLine("5: Show productions for a given nonterminal (RHS)");
        Console.WriteLine("6: Show starting symbol");
        Console.WriteLine("7: Is the grammar a context free grammar?");
        Console.WriteLine("8. Show FIRST");
        Console.WriteLine("9. Show FOLLOW");
        Console.WriteLine("10. Show Parse Table");
        Console.WriteLine("11. Parse a sequence and print tree table");

        while (true)
        {
            try
            {
                Console.Write("> ");
                int option = int.Parse(Console.ReadLine());
                string nonterminal;

                switch (option)
                {
                    case 0:
                        return;

                    case 1:
                        grammar.PrintNonterminalSymbols();
                        break;

                    case 2:
                        grammar.PrintTerminalSymbols();
                        break;

                    case 3:
                        grammar.PrintAllProductions();
                        break;
                    
                    case 4:
                        Console.Write("nonterminal: ");
                        nonterminal = Console.ReadLine();
                        grammar.PrintProductionLHSsForANonterminal(nonterminal);
                        break;
                    
                    case 5:
                        Console.Write("nonterminal: ");
                        nonterminal = Console.ReadLine();
                        grammar.PrintProductionRHSsForANonterminal(nonterminal);
                        break;

                    case 6:
                        grammar.PrintStartingSymbol();
                        break;
                    
                    case 7:
                        Console.WriteLine(grammar.CheckCFG() ? "Yes" : "No");
                        break;
                    
                    case 8:
                        parser.PrintFirst();
                        break;
                    
                    case 9:
                        parser.PrintFollow();
                        break;
                    
                    case 10:
                        parser.PrintParseTable();
                        break;
                    
                    case 11:
                        Console.Write("sequence (space separated symbols): ");
                        var sequence = Console.ReadLine();
                        Console.WriteLine(parser.ParseSequence(new List<string>(sequence.Split(" "))));
                        parserOutput.PrintTreeTable(new List<string>(sequence.Split(" ")));
                        break;

                    default:
                        Console.WriteLine("Invalid option");
                        break;
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}