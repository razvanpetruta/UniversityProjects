package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import javafx.util.Pair;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyISemaphoreTable;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseStatement implements IStatement {
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public ReleaseStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        ReleaseStatement.lock.lock();
        try {
            MyIDictionary<String, Value> symTable = state.getSymbolTable();
            MyISemaphoreTable semaphoreTable = state.getSemaphoreTable();

            if (!symTable.isDefined(var)) {
                throw new StatementExecutionException("Index not in symbol table!");
            }

            if (!symTable.lookUp(var).getType().equals(new IntType())) {
                throw new StatementExecutionException("Index must be of int type!");
            }

            IntValue fi = (IntValue) symTable.lookUp(var);
            int foundIndex = fi.getValue();

            if (!semaphoreTable.getSemaphoreTable().containsKey(foundIndex)) {
                throw new StatementExecutionException("Index not in the semaphore table!");
            }

            Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(foundIndex);
            if (foundSemaphore.getValue().contains(state.getId()))
                foundSemaphore.getValue().remove((Integer) state.getId());
            semaphoreTable.update(foundIndex, new Pair<>(foundSemaphore.getKey(), foundSemaphore.getValue()));

            return null;
        } finally {
            ReleaseStatement.lock.unlock();
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(var).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not int!", var));
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("release(%s)", var);
    }
}
