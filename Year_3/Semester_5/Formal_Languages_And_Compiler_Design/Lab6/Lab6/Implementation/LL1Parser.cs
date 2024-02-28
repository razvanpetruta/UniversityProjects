namespace Lab6.Implementation;

public class LL1Parser
{
    private readonly Grammar _grammar;
    private Dictionary<string, HashSet<string>> _firstDictionary;
    private Dictionary<string, HashSet<string>> _followDictionary;

    public LL1Parser(Grammar grammar)
    {
        _grammar = grammar;
        _firstDictionary = new Dictionary<string, HashSet<string>>();
        _followDictionary = new Dictionary<string, HashSet<string>>();
        
        GenerateFirstDictionary();
        GenerateFollowDictionary();
    }

    private HashSet<string> PerformConcatenationOfSizeOne(List<string> nonterminals, string terminal)
    {
        var concatenation = new HashSet<string>();
        var allContainEpsilon = true;
        
        foreach (var nonterminal in nonterminals)
        {
            if (!_firstDictionary[nonterminal].Contains("ε"))
                allContainEpsilon = false;
        }

        // if all nonterminals up until terminal can derive ε, we also add the terminal
        if (allContainEpsilon)
            concatenation.Add(terminal ?? "ε");
        
        var step = 0;
        while (step < nonterminals.Count)
        {
            var thereIsOneEpsilon = false;
            foreach (var s in _firstDictionary[nonterminals[step]])
            {
                if (s == "ε")
                    thereIsOneEpsilon = true;
                else
                    concatenation.Add(s);
            }

            if (thereIsOneEpsilon)
                step++;
            else
                break;
        }
        
        return concatenation;
    }

    private void GenerateFirstDictionary()
    {
        foreach (var nonterminal in _grammar.NonterminalSymbols)
        {
            _firstDictionary[nonterminal] = new HashSet<string>();
            var productionsForNonterminal = _grammar.GetProductionLHSsForANonterminal(nonterminal);
            foreach (var production in productionsForNonterminal)
            {
                string firstElementOfRHS = production.RightHandSide.Split(" ")[0];
                if (_grammar.TerminalSymbols.Contains(firstElementOfRHS) || firstElementOfRHS == "ε")
                    _firstDictionary[nonterminal].Add(firstElementOfRHS);
            }
        }

        var isChanged = true;
        while (isChanged)
        {
            isChanged = false;
            var newColumn = new Dictionary<string, HashSet<string>>();

            foreach (var nonterminal in _grammar.NonterminalSymbols)
            {
                var productionsForNonterminal = _grammar.GetProductionLHSsForANonterminal(nonterminal);
                var toAdd = new HashSet<string>(_firstDictionary[nonterminal]);
                foreach (var production in productionsForNonterminal)
                {
                    var nonterminalsRHS = new List<string>();
                    string rhsTerminal = null;
                    foreach (var symbol in production.RightHandSide.Split(" "))
                    {
                        if (_grammar.NonterminalSymbols.Contains(symbol))
                            nonterminalsRHS.Add(symbol);
                        else
                        {
                            rhsTerminal = symbol;
                            break;
                        }
                    }
                    toAdd.UnionWith(PerformConcatenationOfSizeOne(nonterminalsRHS, rhsTerminal));
                }
                
                if (!toAdd.SetEquals(_firstDictionary[nonterminal]))
                    isChanged = true;
                
                newColumn[nonterminal] = toAdd;
            }
            
            _firstDictionary = newColumn;
        }
    }
    
    public void PrintFirst()
    {
        foreach (var kv in _firstDictionary)
        {
            Console.WriteLine($"{kv.Key}: {string.Join(", ", kv.Value)}");
        }
    }

    private void GenerateFollowDictionary()
    {
        foreach (var nonterminal in _grammar.NonterminalSymbols)
        {
            _followDictionary[nonterminal] = new HashSet<string>();
        }
        _followDictionary[_grammar.StartingSymbol].Add("ε");

        var isChanged = true;
        while (isChanged)
        {
            isChanged = false;
            var newColumn = new Dictionary<string, HashSet<string>>();

            foreach (var nonterminal in _grammar.NonterminalSymbols)
            {
                newColumn[nonterminal] = new HashSet<string>();

                var toAdd = new HashSet<string>(_followDictionary[nonterminal]);
                foreach (var production in _grammar.GetProductionRHSsForANonterminal(nonterminal))
                {
                    var rhsValues = production.RightHandSide.Split(" ");
                    for (var i = 0; i < rhsValues.Length; i++)
                    {
                        if (rhsValues[i] == nonterminal)
                        {
                            if (i + 1 == rhsValues.Length)
                                toAdd.UnionWith(_followDictionary[production.LeftHandSide]);
                            else
                            {
                                var nextSymbol = rhsValues[i + 1];
                                if (_grammar.TerminalSymbols.Contains(nextSymbol))
                                    toAdd.Add(nextSymbol);
                                else
                                {
                                    foreach (var symbol in _firstDictionary[nextSymbol])
                                    {
                                        if (symbol == "ε")
                                            toAdd.UnionWith(_followDictionary[production.LeftHandSide]);
                                        else
                                            toAdd.UnionWith(_firstDictionary[nextSymbol]);
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (!toAdd.SetEquals(_followDictionary[nonterminal]))
                    isChanged = true;
                
                newColumn[nonterminal] = toAdd;
            }

            _followDictionary = newColumn;
        }
    }
    
    public void PrintFollow()
    {
        foreach (var kv in _followDictionary)
        {
            Console.WriteLine($"{kv.Key}: {string.Join(", ", kv.Value)}");
        }
    }
}