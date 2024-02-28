namespace ParsingFinal.Implementation;

public class Production
{
    public string LeftHandSide { get; set; }
    public string RightHandSide { get; set; }

    public Production(string leftHandSide, string rightHandSide)
    {
        LeftHandSide = leftHandSide;
        RightHandSide = rightHandSide;
    }
}