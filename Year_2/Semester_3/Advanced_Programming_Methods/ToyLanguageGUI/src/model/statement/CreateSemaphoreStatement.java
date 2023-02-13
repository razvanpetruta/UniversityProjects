package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import javafx.util.Pair;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;
import utils.MyISemaphoreTable;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStatement implements IStatement {
    private final String var;
    private final IExpression expression;
    private static final Lock lock = new ReentrantLock();

    public CreateSemaphoreStatement(String var, IExpression expression) {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        CreateSemaphoreStatement.lock.lock();
        try {
            MyIDictionary<String, Value> symTable = state.getSymbolTable();
            MyIHeap heap = state.getHeap();
            MyISemaphoreTable semaphoreTable = state.getSemaphoreTable();
            IntValue nr = (IntValue) (expression.eval(symTable, heap));
            int number = nr.getValue();
            int freeAddress = semaphoreTable.getFreeAddress();
            semaphoreTable.put(freeAddress, new Pair<>(number, new ArrayList<>()));
            if (symTable.isDefined(var) && symTable.lookUp(var).getType().equals(new IntType()))
                symTable.update(var, new IntValue(freeAddress));
            else
                throw new StatementExecutionException(String.format("Error for variable %s: not defined/does not have int type!", var));
            return null;
        } finally {
            CreateSemaphoreStatement.lock.unlock();
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(var).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not of type int!", var));
        }

        if (!this.expression.typeCheck(typeEnv).equals(new IntType())) {
            throw new StatementExecutionException("Expression is not of int type!");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("createSemaphore(%s, %s)", var, expression);
    }
}
