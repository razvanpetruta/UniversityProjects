namespace ParsingFinal.Utils;

public class Pair<K, V>
{
    public K First { get; set; }
    public V Second { get; set; }

    public Pair(K first, V second)
    {
        First = first;
        Second = second;
    }

    public override bool Equals(Object? obj)
    {
        if (obj == null)
            return false;

        if (obj is Pair<K, V> pair) 
            return EqualityComparer<K>.Default.Equals(First, pair.First) &&
                   EqualityComparer<V>.Default.Equals(Second, pair.Second);

        return false;
    }
    
    public override int GetHashCode()
    {
        return HashCode.Combine(First, Second);
    }
}