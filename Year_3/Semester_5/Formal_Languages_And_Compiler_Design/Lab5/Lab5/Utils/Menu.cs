using Lab5.Implementation;

namespace Lab5.Utils;

public class Menu
{
    public void Run()
    {
        Grammar grammar = new Grammar("Resources/g2.txt");
        
        Console.WriteLine("0: EXIT");
        Console.WriteLine("1: Show nonterminal symbols");
        Console.WriteLine("2: Show terminal symbols");
        Console.WriteLine("3: Show set of productions");
        Console.WriteLine("4: Show productions for a given nonterminal");
        Console.WriteLine("5: Show starting symbol");
        Console.WriteLine("6: Is the grammar a context free grammar?");

        while (true)
        {
            try
            {
                Console.Write("> ");
                int option = int.Parse(Console.ReadLine());

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
                        string nonterminal = Console.ReadLine();
                        grammar.PrintProductionForANonterminal(nonterminal);
                        break;

                    case 5:
                        grammar.PrintStartingSymbol();
                        break;
                    
                    case 6:
                        Console.WriteLine(grammar.CheckCFG() ? "Yes" : "No");
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