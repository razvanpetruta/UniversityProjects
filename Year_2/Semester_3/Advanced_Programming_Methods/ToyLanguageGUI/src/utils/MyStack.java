package utils;

import exceptions.ADTException;

import java.util.*;

public class MyStack<T> implements MyIStack<T>{
    Stack<T> stack;

    public MyStack() {
        this.stack = new Stack<>();
    }

    @Override
    public T pop() throws ADTException {
        if (this.stack.isEmpty()) {
            throw new ADTException("Stack is empty");
        }
        return this.stack.pop();
    }

    @Override
    public void push(T el) {
        this.stack.push(el);
    }

    @Override
    public T peek() throws ADTException {
        if (this.stack.isEmpty()) {
            throw new ADTException("Stack is empty");
        }
        return this.stack.peek();
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public List<T> getReversed() {
        List<T> list = new ArrayList<>(this.stack);
        Collections.reverse(list);
        return list;
    }

    @Override
    public String toString() {
        return this.stack.toString();
    }
}
