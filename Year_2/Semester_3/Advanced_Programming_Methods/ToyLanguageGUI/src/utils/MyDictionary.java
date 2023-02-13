package utils;

import exceptions.ADTException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MyDictionary<T1, T2> implements MyIDictionary<T1, T2>{
    HashMap<T1, T2> dictionary;

    public MyDictionary() {
        this.dictionary = new HashMap<>();
    }

    @Override
    public boolean isDefined(T1 id) {
        synchronized (this) {
            return this.dictionary.containsKey(id);
        }
    }

    @Override
    public void put(T1 id, T2 value) {
        synchronized (this) {
            this.dictionary.put(id, value);
        }
    }

    @Override
    public T2 lookUp(T1 key) throws ADTException {
        synchronized (this) {
            if (!this.dictionary.containsKey(key)) {
                throw new ADTException(key + " is not defined");
            }
            return this.dictionary.get(key);
        }
    }

    @Override
    public void update(T1 id, T2 value) {
        synchronized (this) {
            this.dictionary.put(id, value);
        }
    }

    @Override
    public Collection<T2> values() {
        synchronized (this) {
            return this.dictionary.values();
        }
    }

    @Override
    public void remove(T1 id) throws ADTException {
        synchronized (this) {
            if (!this.dictionary.containsKey(id)) {
                throw new ADTException(id + " is not defined");
            }
            this.dictionary.remove(id);
        }
    }

    @Override
    public Set<T1> keySet() {
        synchronized (this) {
            return this.dictionary.keySet();
        }
    }

    @Override
    public Map<T1, T2> getContent() {
        synchronized (this) {
            return this.dictionary;
        }
    }

    @Override
    public MyIDictionary<T1, T2> deepCopy() throws ADTException {
        MyIDictionary<T1, T2> toReturn = new MyDictionary<>();
        for (Map.Entry<T1, T2> entry : this.dictionary.entrySet()) {
            toReturn.put(entry.getKey(), entry.getValue());
        }
        return toReturn;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return this.dictionary.toString();
        }
    }
}
