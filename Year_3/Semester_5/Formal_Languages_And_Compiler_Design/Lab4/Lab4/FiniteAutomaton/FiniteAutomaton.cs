using System.Text;
using System.Text.RegularExpressions;

namespace Lab4;

public class FiniteAutomaton
{
    public List<string> States { get; set; }
    public List<string> Alphabet { get; set; }
    public List<Transition> Transitions { get; set; }
    public string InitialState { get; set; }
    public List<string> FinalStates { get; set; }
    public string PathToFile { get; set; }

    public FiniteAutomaton(string pathToFile)
    {
        PathToFile = pathToFile;
        States = new List<string>();
        Alphabet = new List<string>();
        Transitions = new List<Transition>();
        InitialState = "";
        FinalStates = new List<string>();
        InitFromFile();
    }

    public void PrintStates()
    {
        PrintListOfStrings("states", States);
    }
    
    public void PrintAlphabet()
    {
        PrintListOfStrings("alphabet", Alphabet);
    }
    
    public void PrintTransitions()
    {
        Console.Write("transitions = {");
        for (int i = 0; i < Transitions.Count; i++)
        {
            if (i != Transitions.Count - 1)
            {
                Console.Write($"({Transitions[i].FromState}, {Transitions[i].ToState}, {Transitions[i].Symbol}); ");
            }
            else
            {
                Console.Write($"({Transitions[i].FromState}, {Transitions[i].ToState}, {Transitions[i].Symbol})");
            }
        }
        Console.WriteLine("}");
    }
    
    public void PrintInitialState()
    {
        Console.WriteLine($"initial_state = {InitialState}");
    }
    
    public void PrintFinalStates()
    {
        PrintListOfStrings("final_states", FinalStates);
    }

    public bool IsWordValid(string word)
    {
        if (!IsDeterministicFiniteAutomaton())
        {
            throw new Exception("Not a deterministic finite automaton");
        }
        
        List<string> tokens = word.Select(c => c.ToString()).ToList();
        string currentState = InitialState;
        foreach (string token in tokens)
        {
            bool found = false;
            foreach (Transition transition in Transitions)
            {
                if (transition.FromState == currentState && transition.Symbol == token)
                {
                    currentState = transition.ToState;
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                return false;
            }
        }

        return FinalStates.Contains(currentState);
    }
    
    public string? GetTokenFromFA(string word)
    {
        var currentState = InitialState;
        StringBuilder match = new StringBuilder();

        foreach (char c in word)
        {
            string? newState = null;

            foreach (Transition transition in Transitions)
            {
                if (transition.FromState == currentState && transition.Symbol == c.ToString())
                {
                    newState = transition.ToState;
                    match.Append(c);
                    break;
                }
            }

            if (newState == null)
            {
                if (!FinalStates.Contains(currentState))
                {
                    return null;
                }
                
                return match.ToString();
            }

            currentState = newState;
        }

        return match.ToString();
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

    private void InitFromFile()
    {
        Regex regex = new Regex("^([a-z_]*=)");
        foreach (string line in File.ReadLines(PathToFile))
        {
            Match match = regex.Match(line);
            if (!match.Success)
            {
                throw new Exception($"Invalid line: {line}");
            }

            switch (match.Value)
            {
                case "states=":
                    States = GetLineRightHandSide(line).Split(",").ToList();
                    break;
                
                case "alphabet=":
                    Alphabet = GetLineRightHandSide(line).Split(",").ToList();
                    break;
                
                case "transitions=":
                    List<string> transitionStrings = GetLineRightHandSide(line).Split("|").ToList();
                    foreach (string transitionString in transitionStrings)
                    {
                        string transitionStringNoParanthesis =
                            transitionString.Substring(1, transitionString.Length - 2);
                        List<string> transitionTokens = transitionStringNoParanthesis.Split(",").ToList();
                        Transitions.Add(new Transition(transitionTokens[0], transitionTokens[1], transitionTokens[2]));
                    }
                    break;
                
                case "initial_state=":
                    InitialState = GetLineRightHandSide(line);
                    break;
                
                case "final_states=":
                    FinalStates = GetLineRightHandSide(line).Split(",").ToList();
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

    private bool IsDeterministicFiniteAutomaton()
    {
        HashSet<string> visitedTransitions = new HashSet<string>();
        foreach (Transition transition in Transitions)
        {
            string key = $"{transition.FromState}{transition.Symbol}";
            if (visitedTransitions.Contains(key))
            {
                return false;
            }

            visitedTransitions.Add(key);
        }

        return true;
    }
}