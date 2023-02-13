package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyILockTable;
import utils.MyIStack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStatement implements IStatement {
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public LockStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        LockStatement.lock.lock();
        try {
            MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
            MyILockTable lockTable = state.getLockTable();

            if (!symbolTable.isDefined(this.variable)) {
                throw new StatementExecutionException(String.format("%s is not defined in the SymbolTable", this.variable));
            }

            if (!symbolTable.lookUp(this.variable).getType().equals(new IntType())) {
                throw new StatementExecutionException(String.format("%s is not IntType", this.variable));
            }

            IntValue intValue = (IntValue) symbolTable.lookUp(this.variable);
            int foundIndex = intValue.getValue();

            if (!lockTable.containsKey(foundIndex)) {
                throw new StatementExecutionException(String.format("%s not in the LockTables", foundIndex));
            }

            if (lockTable.get(foundIndex) == -1) {
                lockTable.update(foundIndex, state.getId());
                state.setLockTable(lockTable);
            } else {
                MyIStack<IStatement> executionStack = state.getExecutionStack();
                executionStack.push(this);
                state.setExecutionStack(executionStack);
            }

            return null;
        } finally {
            LockStatement.lock.unlock();
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(this.variable).equals(new IntType())) {
            throw new StatementExecutionException(this.variable + " is not of int type!");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("lock(%s)", this.variable);
    }
}
