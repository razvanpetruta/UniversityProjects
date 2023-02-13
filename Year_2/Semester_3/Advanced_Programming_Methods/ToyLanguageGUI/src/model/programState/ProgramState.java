package model.programState;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.statement.IStatement;
import model.value.Value;
import utils.*;

import java.io.BufferedReader;
import java.util.List;

public class ProgramState {
    private MyIStack<IStatement> executionStack;
    private MyIDictionary<String, Value> symbolTable;
    private MyIList<Value> out;
    private MyIDictionary<String, BufferedReader> fileTable;
    private MyIHeap heap;
    private MyILockTable lockTable;
    private MyISemaphoreTable semaphoreTable;
    private MyILatchTable latchTable;
    private IStatement originalProgram;
    private final int id;
    private static int lastId = 0;

    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, Value> symbolsTable, MyIList<Value> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MyILockTable lockTable, MyISemaphoreTable semaphoreTable, MyILatchTable latchTable, IStatement program) {
        this.executionStack = stack;
        this.symbolTable = symbolsTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.semaphoreTable = semaphoreTable;
        this.latchTable = latchTable;
        this.originalProgram = program;
        this.executionStack.push(this.originalProgram);
        this.id = this.setId();
    }

    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, Value> symbolsTable, MyIList<Value> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MyILockTable lockTable, MyISemaphoreTable semaphoreTable, MyILatchTable latchTable) {
        this.executionStack = stack;
        this.symbolTable = symbolsTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.semaphoreTable = semaphoreTable;
        this.latchTable = latchTable;
        this.id = this.setId();
    }

    public synchronized int setId() {
        ProgramState.lastId++;
        return ProgramState.lastId;
    }
    public MyILockTable getLockTable() {
        return lockTable;
    }

    public void setLockTable(MyILockTable lockTable) {
        this.lockTable = lockTable;
    }
    public synchronized int getId() {
        return this.id;
    }

    public MyIHeap getHeap() {
        return heap;
    }

    public void setHeap(MyIHeap heap) {
        this.heap = heap;
    }

    public MyIStack<IStatement> getExecutionStack() {
        return this.executionStack;
    }

    public void setExecutionStack(MyIStack<IStatement> executionStack) {
        this.executionStack = executionStack;
    }

    public MyIDictionary<String, Value> getSymbolTable() {
        return this.symbolTable;
    }

    public void setSymbolTable(MyIDictionary<String, Value> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public MyISemaphoreTable getSemaphoreTable() {
        return semaphoreTable;
    }

    public void setSemaphoreTable(MyISemaphoreTable semaphoreTable) {
        this.semaphoreTable = semaphoreTable;
    }

    public MyILatchTable getLatchTable() {
        return latchTable;
    }

    public void setLatchTable(MyILatchTable latchTable) {
        this.latchTable = latchTable;
    }

    public MyIList<Value> getOut() {
        return this.out;
    }

    public void setOut(MyIList<Value> out) {
        this.out = out;
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(MyIDictionary<String, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public boolean isNotCompleted() {
        return !this.executionStack.isEmpty();
    }

    public ProgramState oneStep() throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        if (this.executionStack.isEmpty())
            throw new StatementExecutionException("Program state stack is empty.");
        IStatement currentStatement = this.executionStack.pop();
        return currentStatement.execute(this);
    }

    public String executionStackToString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        List<IStatement> stack = this.executionStack.getReversed();
        for (IStatement statement: stack) {
            exeStackStringBuilder.append(statement.toString()).append("\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symbolTableToString() throws ADTException {
        StringBuilder symTableStringBuilder = new StringBuilder();
        for (String key: this.symbolTable.keySet()) {
            symTableStringBuilder.append(String.format("%s -> %s\n", key, this.symbolTable.lookUp(key).toString()));
        }
        return symTableStringBuilder.toString();
    }

    public String outToString() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (Value elem: this.out.getList()) {
            outStringBuilder.append(String.format("%s\n", elem.toString()));
        }
        return outStringBuilder.toString();
    }

    public String fileTableToString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String key: this.fileTable.keySet()) {
            fileTableStringBuilder.append(String.format("%s\n", key));
        }
        return fileTableStringBuilder.toString();
    }

    public String heapToString() throws ADTException {
        StringBuilder heapStringBuilder = new StringBuilder();
        for (int key: heap.keySet()) {
            heapStringBuilder.append(String.format("%d -> %s\n", key, this.heap.get(key)));
        }
        return heapStringBuilder.toString();
    }

    public String lockTableToString() throws ADTException {
        StringBuilder lockTableStringBuilder = new StringBuilder();
        for (int key: lockTable.keySet()) {
            lockTableStringBuilder.append(String.format("%d -> %d\n", key, lockTable.get(key)));
        }
        return lockTableStringBuilder.toString();
    }

    public String semaphoreTableToString() throws ADTException {
        StringBuilder semaphoreTableStringBuilder = new StringBuilder();
        for (int key: semaphoreTable.getSemaphoreTable().keySet()) {
            semaphoreTableStringBuilder.append(String.format("%d -> (%d, %s)\n", key, semaphoreTable.get(key).getKey(), semaphoreTable.get(key).getValue()));
        }
        return semaphoreTableStringBuilder.toString();
    }

    public String latchTableToString() throws ADTException {
        StringBuilder latchTableStringBuilder = new StringBuilder();
        for (int key: latchTable.getLatchTable().keySet()) {
            latchTableStringBuilder.append(String.format("%d -> %d\n", key, latchTable.get(key)));
        }
        return latchTableStringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Execution stack: \n" + this.executionStack.getReversed() + "\nSymbol table: \n" +
                this.symbolTable.toString() + "\nOutput list: \n" + this.out.toString() + "\nFile table: \n"
                + this.fileTable.toString() + "\nLock table: \n" + this.lockTable.toString() + "Semaphore table: \n"
                + this.semaphoreTable.toString() + "\nLatch table: \n" + this.latchTable.toString();
    }

    public String programStateToString() throws ADTException {
        return "Program #" + this.id + "\n" +
                "Execution stack: \n" + this.executionStackToString() + "Symbol table: \n" + this.symbolTableToString()
                + "Output list: \n" + this.outToString() + "File table: \n" + this.fileTableToString() +
                "Heap memory: \n" + this.heapToString() + "Lock table: \n" + this.lockTableToString() +
                "Semaphore table: \n" + this.semaphoreTableToString() + "Latch table: \n" + this.latchTableToString();
    }
}
