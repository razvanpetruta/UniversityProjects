namespace Lab3;

class Program
{
    static void Main(string[] args)
    {
        Scanner scanner = new Scanner();
        scanner.Scan("Resources/p1.txt");
        scanner.Scan("Resources/p2.txt");
        scanner.Scan("Resources/p3.txt");
        scanner.Scan("Resources/p1err.txt");
        // scanner.Scan("Resources/testing.txt");
    }
}