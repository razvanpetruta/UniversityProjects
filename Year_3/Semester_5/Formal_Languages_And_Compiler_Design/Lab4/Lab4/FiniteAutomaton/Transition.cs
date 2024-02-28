namespace Lab4;

public class Transition
{
    public string FromState { get; set; }
    public string ToState { get; set; }
    public string Symbol { get; set; }

    public Transition(string fromState, string toState, string symbol)
    {
        FromState = fromState;
        ToState = toState;
        Symbol = symbol;
    }
}