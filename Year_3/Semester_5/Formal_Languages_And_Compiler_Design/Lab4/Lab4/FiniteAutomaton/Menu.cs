namespace Lab4;

public class Menu
{
    public void Run()
    {
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton("FiniteAutomaton/finite_automaton.txt");
        
        Console.WriteLine("0: EXIT");
        Console.WriteLine("1: Show states");
        Console.WriteLine("2: Show alphabet");
        Console.WriteLine("3: Show transitions");
        Console.WriteLine("4: Show initial state");
        Console.WriteLine("5: Show final states");
        Console.WriteLine("6: Check sequence acceptance");

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
                        finiteAutomaton.PrintStates();
                        break;

                    case 2:
                        finiteAutomaton.PrintAlphabet();
                        break;

                    case 3:
                        finiteAutomaton.PrintTransitions();
                        break;

                    case 4:
                        finiteAutomaton.PrintInitialState();
                        break;

                    case 5:
                        finiteAutomaton.PrintFinalStates();
                        break;

                    case 6:
                        Console.Write("Enter word: ");
                        var word = Console.ReadLine();
                        Console.WriteLine(finiteAutomaton.IsWordValid(word));
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