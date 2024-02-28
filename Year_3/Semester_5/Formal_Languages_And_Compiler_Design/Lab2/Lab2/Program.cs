namespace Lab2;

class Program
{
    static void Main(string[] args)
    {
        SymbolTable symbolTable = new SymbolTable(11);

        try
        {
            Console.WriteLine("var1 -> " + symbolTable.AddIdentifier("var1"));
            Console.WriteLine("var2 -> " + symbolTable.AddIdentifier("var2"));
            Console.WriteLine("mySum -> " + symbolTable.AddIdentifier("mySum"));
            Console.WriteLine("myProduct -> " + symbolTable.AddIdentifier("myProduct"));
            
            Console.WriteLine("1 -> " + symbolTable.AddIntConstant(1));
            Console.WriteLine("121 -> " + symbolTable.AddIntConstant(121));
            Console.WriteLine("34 -> " + symbolTable.AddIntConstant(34));
            Console.WriteLine("5 -> " + symbolTable.AddIntConstant(5));
            Console.WriteLine("95 -> " + symbolTable.AddIntConstant(95));
            
            Console.WriteLine("\"ana\" -> " + symbolTable.AddStringConstant("ana"));
            Console.WriteLine("\"are\" -> " + symbolTable.AddStringConstant("are"));
            Console.WriteLine("\"mere\" -> " + symbolTable.AddStringConstant("mere"));
            Console.WriteLine("\"si\" -> " + symbolTable.AddStringConstant("si"));
            Console.WriteLine("\"banane\" -> " + symbolTable.AddStringConstant("banane"));
            
            Console.WriteLine("Does the table have identifier 'var1'? " + symbolTable.HasIdentifier("var1"));
            Console.WriteLine("Does the table have int constant 121? " + symbolTable.HasIntConstant(121));
            Console.WriteLine("Does the table have int constant 999? " + symbolTable.HasIntConstant(999));
            Console.WriteLine("Does the table have string constant \"ana\"? " + symbolTable.HasStringConstant("ana"));
            Console.WriteLine("Does the table have string constant \"banana\"? " + symbolTable.HasStringConstant("banana"));

            Console.WriteLine("Position of identifier 'var1' -> " + symbolTable.GetIdentifierPosition("var1"));
            Console.WriteLine("Position of int constant 121 -> " + symbolTable.GetIntConstantPosition(121));
            Console.WriteLine("Position of int constant 999 -> " + symbolTable.GetIntConstantPosition(999));
            Console.WriteLine("Position of string constant \"ana\" -> " + symbolTable.GetStringConstantPosition("ana"));
            Console.WriteLine("Position of string constant \"banana\" -> " + symbolTable.GetStringConstantPosition("banana"));

            Console.WriteLine(symbolTable);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }
}