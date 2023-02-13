package utils;

import exceptions.ADTException;
import model.value.Value;

import java.util.HashMap;
import java.util.Set;

public class MyHeap implements MyIHeap {
    private HashMap<Integer, Value> heap;
    private Integer freeLocation;

    public MyHeap() {
        this.heap = new HashMap<>();
        this.freeLocation = 1;
    }

    @Override
    public int getFreeValue() {
        synchronized (this) {
            return this.freeLocation;
        }
    }

    @Override
    public HashMap<Integer, Value> getContent() {
        synchronized (this) {
            return this.heap;
        }
    }

    @Override
    public void setContent(HashMap<Integer, Value> newMap) {
        synchronized (this) {
            this.heap = newMap;
        }
    }

    private void nextFreeLocation() {
        this.freeLocation += 1;
        while (this.containsKey(this.freeLocation)) {
            this.freeLocation += 1;
        }
    }

    @Override
    public int add(Value value) {
        synchronized (this) {
            this.heap.put(this.freeLocation, value);
            int toReturn = this.freeLocation;
            this.nextFreeLocation();
            return toReturn;
        }
    }

    @Override
    public void update(Integer position, Value value) throws ADTException {
        synchronized (this) {
            if (!this.containsKey(position)) {
                throw new ADTException(position + " is not present in the heap");
            }
            this.heap.put(position, value);
        }
    }

    @Override
    public Value get(Integer position) throws ADTException {
        synchronized (this) {
            if (!this.containsKey(position)) {
                throw new ADTException(position + " is not present in the heap");
            }
            return this.heap.get(position);
        }
    }

    @Override
    public boolean containsKey(Integer position) {
        synchronized (this) {
            return this.heap.containsKey(position);
        }
    }

    @Override
    public void remove(Integer key) throws ADTException {
        synchronized (this) {
            if (!this.containsKey(key)) {
                throw new ADTException(key + " is not present in the heap");
            }
            this.freeLocation = key;
            this.heap.remove(key);
        }
    }

    @Override
    public Set<Integer> keySet() {
        synchronized (this) {
            return this.heap.keySet();
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return this.heap.toString();
        }
    }
}
