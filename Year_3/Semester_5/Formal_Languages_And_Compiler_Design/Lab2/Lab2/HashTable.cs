using System.Text;

namespace Lab2;

public class HashTable<T>
{
    private List<List<T>> _items;
    public int Size { get; private set; }
    
    public HashTable(int size)
    {
        Size = size;
        _items = new List<List<T>>();
        for (int i = 0; i < Size; i++)
        {
            _items.Add(new List<T>());
        }
    }

    public KeyValuePair<int, int> Add(T key)
    {
        int hashValue = GetHashValue(key);
        if (!_items[hashValue].Contains(key))
        {
            _items[hashValue].Add(key);
            
            return new KeyValuePair<int, int>(hashValue, _items[hashValue].IndexOf(key));
        }

        throw new Exception($"Key {key} is already in the table!");
    }

    public bool Contains(T key)
    {
        int hashValue = GetHashValue(key);

        return _items[hashValue].Contains(key);
    }

    public KeyValuePair<int, int> GetPosition(T key)
    {
        if (Contains(key))
        {
            int hashValue = GetHashValue(key);

            return new KeyValuePair<int, int>(hashValue, _items[hashValue].IndexOf(key));
        }

        return new KeyValuePair<int, int>(-1, -1);
    }

    public bool Any()
    {
        foreach (var item in _items)
        {
            if (item.Count > 0)
            {
                return true;
            }
        }
        return false;
    }

    
    public override string ToString()
    {
        var sb = new StringBuilder("HashTable {\n");

        for (int i = 0; i < _items.Count; i++)
        {
            if (_items[i].Count > 0)
            {
                sb.Append($"\tIndex {i}:\n");

                foreach (T item in _items[i])
                {
                    int position = _items[i].IndexOf(item);
                    sb.Append($"\t\tPosition {position}: {item}\n");
                }
            }
        }

        sb.Append("\t}");
        return sb.ToString();
    }

    private int Hash(int key)
    {
        return key % Size;
    }

    private int Hash(string key)
    {
        int sum = 0;
        foreach (char c in key)
        {
            sum += (int)c;
        }

        return sum % Size;
    }

    private int GetHashValue(T key)
    {
        int hashValue = -1;
        if (key is int)
        {
            hashValue = Hash((int)(object)key);
        }
        else if (key is string)
        {
            hashValue = Hash((string)(object)key);
        }

        return hashValue;
    }
}