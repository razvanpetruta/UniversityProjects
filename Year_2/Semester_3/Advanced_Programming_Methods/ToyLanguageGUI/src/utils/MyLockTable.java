package utils;

import exceptions.ADTException;

import java.util.HashMap;
import java.util.Set;

public class MyLockTable implements MyILockTable {
    private HashMap<Integer, Integer> lockTable;
    private int freeLocation;

    public MyLockTable() {
        this.lockTable = new HashMap<>();
    }

    @Override
    public int getFreeValue() {
        synchronized (this) {
            this.freeLocation++;
            return this.freeLocation;
        }
    }

    @Override
    public void put(int key, int value) throws ADTException {
        synchronized (this) {
            if (!this.lockTable.containsKey(key)) {
                this.lockTable.put(key, value);
            } else {
                throw new ADTException(String.format("Lock table already contains the key %d", key));
            }
        }
    }

    @Override
    public HashMap<Integer, Integer> getContent() {
        synchronized (this) {
            return this.lockTable;
        }
    }

    @Override
    public boolean containsKey(int position) {
        synchronized (this) {
            return this.lockTable.containsKey(position);
        }
    }

    @Override
    public int get(int position) throws ADTException {
        synchronized (this) {
            if (!this.lockTable.containsKey(position)) {
                throw new ADTException(String.format("%d is not present in the lock table", position));
            }
            return this.lockTable.get(position);
        }
    }

    @Override
    public void update(int position, int value) throws ADTException {
        synchronized (this) {
            if (this.lockTable.containsKey(position)) {
                this.lockTable.replace(position, value);
            } else {
                throw new ADTException(String.format("%d is not present in the lock table", position));
            }
        }
    }

    @Override
    public void setContent(HashMap<Integer, Integer> newMap) {
        synchronized (this) {
            this.lockTable = newMap;
        }
    }

    @Override
    public Set<Integer> keySet() {
        synchronized (this) {
            return this.lockTable.keySet();
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return this.lockTable.toString();
        }
    }
}
