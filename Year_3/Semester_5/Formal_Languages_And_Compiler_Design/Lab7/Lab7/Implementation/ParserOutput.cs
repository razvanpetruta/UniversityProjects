using Lab7.Utils;

namespace Lab7.Implementation;

public class ParserOutput
{
    private readonly LL1Parser _parser;
    private Node _head;
    private List<Node> _nodeList;
    private List<int> _parsingResult;

    public ParserOutput(LL1Parser parser)
    {
        _parser = parser;
        _nodeList = new List<Node>();
    }

    private void GenerateTreeTable(List<string> sequence)
    {
        BuildParsingResultList(sequence);

        int currentParseSolutionIndex = 0;
        int currentNodeIndex = 1;
        Stack<Node> stack = new Stack<Node>();

        Node headNode = new Node();
        headNode.Index = currentNodeIndex;
        headNode.Parent = 0;
        headNode.Info = _parser.grammar.StartingSymbol;
        headNode.RightSibling = 0;
        _head = headNode;
        
        stack.Push(_head);
        _nodeList.Add(_head);
        currentNodeIndex++;

        while (currentParseSolutionIndex < _parsingResult.Count && stack.Count != 0)
        {
            Node currentNode = stack.Peek();

            if (_parser.grammar.TerminalSymbols.Contains(currentNode.Info) || currentNode.Info.Contains("ε"))
            {
                while (stack.Count > 0 && (_parser.grammar.TerminalSymbols.Contains(stack.Peek().Info) || stack.Peek().Info.Contains("ε")))
                    stack.Pop();
                
                if (stack.Count == 0)
                    break;
            }
            else
            {
                var production = _parser.grammar.Productions[_parsingResult[currentParseSolutionIndex] - 1];
                var rhsTokens = production.RightHandSide.Split(" ");
                currentNodeIndex += rhsTokens.Length - 1;
                
                for (int i = rhsTokens.Length - 1; i >= 0; i--)
                {
                    Node child = new Node();
                    child.Parent = currentNode.Index;
                    child.Info = rhsTokens[i];
                    child.Index = currentNodeIndex;
                    if (i == rhsTokens.Length - 1)
                        child.RightSibling = 0;
                    else
                        child.RightSibling = currentNodeIndex + 1;

                    currentNodeIndex--;
                    stack.Push(child);
                    _nodeList.Add(child);
                }

                currentNodeIndex += rhsTokens.Length + 1;
                currentParseSolutionIndex++;
            }
        }
    }

    public void PrintTreeTable(List<string> sequence)
    {
        GenerateTreeTable(sequence);
        _nodeList.Sort((node1, node2) => node1.Index.CompareTo(node2.Index));
    
        int maxColumnWidth = 15;
    
        using (StreamWriter file = new StreamWriter("Resources/TreeTableOutput.txt"))
        {
            void WriteLineToBoth(string line)
            {
                Console.WriteLine(line);
                file.WriteLine(line);
            }

            string header = "Index".PadRight(maxColumnWidth) +
                            "Parent".PadRight(maxColumnWidth) +
                            "Info".PadRight(maxColumnWidth) +
                            "Right sibling".PadRight(maxColumnWidth);

            WriteLineToBoth(header);

            foreach (var node in _nodeList)
            {
                string line = $"{node.Index}".PadRight(maxColumnWidth) +
                              $"{node.Parent}".PadRight(maxColumnWidth) +
                              $"{node.Info}".PadRight(maxColumnWidth) +
                              $"{node.RightSibling}".PadRight(maxColumnWidth);

                WriteLineToBoth(line);
            }
        }
    }


    private void BuildParsingResultList(List<string> sequence)
    {
        _parsingResult = new List<int>();
        foreach (var el in _parser.ParseSequence(sequence))
            _parsingResult.Add(el - '0');
    }
}