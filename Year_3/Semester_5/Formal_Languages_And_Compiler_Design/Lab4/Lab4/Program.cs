namespace Lab4;

class Program
{
    static void Main(string[] args)
    {
        // Menu menu = new Menu();
        // menu.Run();
        Scanner scanner = new Scanner();
        scanner.Scan("Resources/p1.txt");
        scanner.Scan("Resources/p2.txt");
        scanner.Scan("Resources/p3.txt");
        scanner.Scan("Resources/p1err.txt");
    }
}