package utils;

import exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T el) {
        synchronized (this) {
            this.list.add(el);
        }
    }

    @Override
    public T pop() throws ADTException {
        synchronized (this) {
            if (this.list.isEmpty()) {
                throw new ADTException("List is empty");
            }
            return this.list.remove(0);
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (this) {
            return this.list.isEmpty();
        }
    }

    @Override
    public List<T> getList() {
        synchronized (this) {
            return this.list;
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return this.list.toString();
        }
    }
}
