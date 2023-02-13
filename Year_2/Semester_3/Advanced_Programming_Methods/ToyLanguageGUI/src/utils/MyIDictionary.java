package utils;

import exceptions.ADTException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<T1, T2> {
    boolean isDefined(T1 id);

    void put(T1 id, T2 value);

    T2 lookUp(T1 key) throws ADTException;

    void update(T1 id, T2 value);

    Collection<T2> values();

    void remove(T1 id) throws ADTException;

    Set<T1> keySet();

    Map<T1, T2> getContent();

    MyIDictionary<T1, T2> deepCopy() throws ADTException;
}
