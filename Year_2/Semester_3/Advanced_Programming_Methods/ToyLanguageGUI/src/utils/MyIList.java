package utils;

import exceptions.ADTException;

import java.util.List;

public interface MyIList<T> {
    void add(T el);

    T pop() throws ADTException;

    boolean isEmpty();

    List<T> getList();
}
