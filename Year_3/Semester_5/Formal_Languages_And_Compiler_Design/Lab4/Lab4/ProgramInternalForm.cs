namespace Lab4;

public class ProgramInternalForm
{
    private List<KeyValuePair<string, KeyValuePair<int, int>>> _elements = new();

    public void AddElement(KeyValuePair<string, KeyValuePair<int, int>> element)
    {
        _elements.Add(element);
    }

    public override string ToString()
    {
        return string.Join(Environment.NewLine, _elements.Select(pair =>
            $"{pair.Key} -> ({pair.Value.Key}, {pair.Value.Value})"));
    }
}