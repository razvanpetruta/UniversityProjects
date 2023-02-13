package utils;

import exceptions.ADTException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws ADTException;

    void push(T el);

    T peek() throws ADTException;

    boolean isEmpty();

    List<T> getReversed();
}
