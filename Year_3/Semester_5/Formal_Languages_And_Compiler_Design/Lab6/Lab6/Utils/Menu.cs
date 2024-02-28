using Lab6.Implementation;

namespace Lab6.Utils;

public class Menu
{
    public void Run()
    {
        Grammar grammar = new Grammar("Resources/otherExample.txt");
        LL1Parser parser = new LL1Parser(grammar);
        
        Console.WriteLine("0: EXIT");
        Console.WriteLine("1: Show nonterminal symbols");
        Console.WriteLine("2: Show terminal symbols");
        Console.WriteLine("3: Show set of productions");
        Console.WriteLine("4: Show productions for a given nonterminal");
        Console.WriteLine("5: Show starting symbol");
        Console.WriteLine("6: Is the grammar a context free grammar?");
        Console.WriteLine("7. Show FIRST");
        Console.WriteLine("8. Show FOLLOW");

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
                        grammar.PrintProductionLHSsForANonterminal(nonterminal);
                        break;

                    case 5:
                        grammar.PrintStartingSymbol();
                        break;
                    
                    case 6:
                        Console.WriteLine(grammar.CheckCFG() ? "Yes" : "No");
                        break;
                    
                    case 7:
                        parser.PrintFirst();
                        break;
                    
                    case 8:
                        parser.PrintFollow();
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