package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.ValueExpression;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyILatchTable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownStatement implements IStatement {
    private final String var;
    private static final Lock lock = new ReentrantLock();
    public CountDownStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        CountDownStatement.lock.lock();
        try {
            MyIDictionary<String, Value> symTable = state.getSymbolTable();
            MyILatchTable latchTable = state.getLatchTable();

            if (!symTable.isDefined(var)) {
                throw new StatementExecutionException("Variable not defined!");
            }

            if (!symTable.lookUp(var).getType().equals(new IntType())) {
                throw new StatementExecutionException(this.var + " is not IntType");
            }

            IntValue fi = (IntValue) symTable.lookUp(var);
            int foundIndex = fi.getValue();

            if (!latchTable.containsKey(foundIndex)) {
                throw new StatementExecutionException("Index not found in the latch table!");
            }

            if (latchTable.get(foundIndex) > 0) {
                latchTable.update(foundIndex, latchTable.get(foundIndex) - 1);
            }
            state.getExecutionStack().push(new PrintStatement(new ValueExpression(new IntValue(state.getId()))));

            return null;
        } finally {
            CountDownStatement.lock.unlock();
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(var).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not of int type!", var));
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("countDown(%s)", var);
    }
}
