    using System.Text;

    namespace Lab3;

    public class SymbolTable
    {
        private int _size;
        private HashTable<string> _identifiersHashTable;
        private HashTable<int> _intConstantsHashTable;
        private HashTable<string> _stringConstantsHashTable;

        public SymbolTable(int size)
        {
            _size = size;
            _identifiersHashTable = new HashTable<string>(_size);
            _intConstantsHashTable = new HashTable<int>(_size);
            _stringConstantsHashTable = new HashTable<string>(_size);
        }

        public KeyValuePair<int, int> AddIdentifier(string name)
        {
            try
            {
                return _identifiersHashTable.Add(name);
            }
            catch (Exception e)
            {
                throw new Exception($"Failed to add identifier: {e.Message}");
            }
        }
        
        public KeyValuePair<int, int> AddIntConstant(int constant)
        {
            try
            {
                return _intConstantsHashTable.Add(constant);
            }
            catch (Exception e)
            {
                throw new Exception($"Failed to add int constant: {e.Message}");
            }
        }
        
        public KeyValuePair<int, int> AddStringConstant(string constant)
        {
            try
            {
                return _stringConstantsHashTable.Add(constant);
            }
            catch (Exception e)
            {
                throw new Exception($"Failed to add string constant: {e.Message}");
            }
        }

        public bool HasIdentifier(string name)
        {
            return _identifiersHashTable.Contains(name);
        }

        public bool HasIntConstant(int constant)
        {
            return _intConstantsHashTable.Contains(constant);
        }

        public bool HasStringConstant(string constant)
        {
            return _stringConstantsHashTable.Contains(constant);
        }

        public KeyValuePair<int, int> GetIdentifierPosition(string name)
        {
            return _identifiersHashTable.GetPosition(name);
        }

        public KeyValuePair<int, int> GetIntConstantPosition(int constant)
        {
            return _intConstantsHashTable.GetPosition(constant);
        }

        public KeyValuePair<int, int> GetStringConstantPosition(string constant)
        {
            return _stringConstantsHashTable.GetPosition(constant);
        }

        public override string ToString()
        {
            var sb = new StringBuilder("SymbolTable\n{");

            if (_identifiersHashTable.Any())
            {
                sb.Append("\n\tidentifiersHashTable = " + _identifiersHashTable);
            }

            if (_intConstantsHashTable.Any())
            {
                sb.Append("\n\tintConstantsHashTable = " + _intConstantsHashTable);
            }

            if (_stringConstantsHashTable.Any())
            {
                sb.Append("\n\tstringConstantsHashTable = " + _stringConstantsHashTable);
            }

            sb.Append("\n}");
            return sb.ToString();
        }

    }