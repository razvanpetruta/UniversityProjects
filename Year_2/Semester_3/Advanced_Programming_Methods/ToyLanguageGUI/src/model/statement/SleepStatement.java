package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.Type;
import utils.MyIDictionary;
import utils.MyIStack;

public class
SleepStatement implements IStatement {
    private final int number;

    public SleepStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        if (!(this.number == 0)) {
            MyIStack<IStatement> executionStack = state.getExecutionStack();
            executionStack.push(new SleepStatement(this.number - 1));
            state.setExecutionStack(executionStack);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("sleep(%s)", this.number);
    }
}
