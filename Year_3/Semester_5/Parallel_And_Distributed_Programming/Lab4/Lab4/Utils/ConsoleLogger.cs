namespace Lab4.Utils;

public class ConsoleLogger
{
    private readonly bool _shouldPrint;

    public ConsoleLogger(bool shouldPrint)
    {
        _shouldPrint = shouldPrint;
    }

    public void Show(string message)
    {
        if (_shouldPrint)
        {
            Console.WriteLine(message);
        }
    }
}