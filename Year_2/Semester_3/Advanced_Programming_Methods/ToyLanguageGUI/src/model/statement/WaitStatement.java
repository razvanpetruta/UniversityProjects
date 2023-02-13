package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.ValueExpression;
import model.programState.ProgramState;
import model.type.Type;
import model.value.IntValue;
import utils.MyIDictionary;
import utils.MyIStack;

public class WaitStatement implements IStatement {
    private final int number;

    public WaitStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        if (this.number > 0) {
            IStatement converted = new CompoundStatement(
                    new PrintStatement(new ValueExpression(new IntValue(this.number))),
                    new WaitStatement(this.number - 1)
            );
            MyIStack<IStatement> executionStack = state.getExecutionStack();
            executionStack.push(converted);
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
        return String.format("wait(%s)", this.number);
    }
}
