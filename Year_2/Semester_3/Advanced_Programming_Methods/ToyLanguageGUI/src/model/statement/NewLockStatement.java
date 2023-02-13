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

public class NewLockStatement implements IStatement {
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public NewLockStatement(String variable) {
        this.variable = variable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        NewLockStatement.lock.lock();
        try {
            MyILockTable lockTable = state.getLockTable();
            MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
            int freeAddress = lockTable.getFreeValue();
            lockTable.put(freeAddress, -1);

            if (!symbolTable.isDefined(this.variable)) {
                throw new StatementExecutionException(String.format("%s is not in the SymbolTable", this.variable));
            }

            if (!symbolTable.lookUp(this.variable).getType().equals(new IntType())) {
                throw new StatementExecutionException(String.format("%s is not IntType", this.variable));
            }

            symbolTable.update(this.variable, new IntValue(freeAddress));

            return null;
        } finally {
            NewLockStatement.lock.unlock();
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(this.variable).equals(new IntType())) {
            throw new StatementExecutionException(this.variable + " does not have IntType");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("newLock(%s)", this.variable);
    }
}
