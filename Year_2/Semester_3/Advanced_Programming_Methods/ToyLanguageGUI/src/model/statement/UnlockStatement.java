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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnlockStatement implements IStatement {
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public UnlockStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        UnlockStatement.lock.lock();
        try {
            MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
            MyILockTable lockTable = state.getLockTable();

            if (!symbolTable.isDefined(this.variable)) {
                throw new StatementExecutionException(String.format("%s is not in SymbolTable", this.variable));
            }

            if (!symbolTable.lookUp(this.variable).getType().equals(new IntType())) {
                throw new StatementExecutionException(String.format("%s is not IntType", this.variable));
            }

            IntValue fi = (IntValue) symbolTable.lookUp(this.variable);
            int foundIndex = fi.getValue();

            if (!lockTable.containsKey(foundIndex)) {
                throw new StatementExecutionException(String.format("%s not in the LockTable", foundIndex));
            }

            if (lockTable.get(foundIndex) == state.getId())
                lockTable.update(foundIndex, -1);

            return null;
        } finally {
            UnlockStatement.lock.unlock();
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(this.variable).equals(new IntType())) {
            throw new StatementExecutionException(this.variable + " is not of type int!");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("unlock(%s)", this.variable);
    }
}
