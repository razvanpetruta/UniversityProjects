package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.expression.NotExpression;
import model.programState.ProgramState;
import model.type.BoolType;
import model.type.Type;
import utils.MyIDictionary;
import utils.MyIStack;

public class RepeatUntilStatement implements IStatement {
    private final IStatement statement;
    private final IExpression expression;

    public RepeatUntilStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIStack<IStatement> executionStack = state.getExecutionStack();
        IStatement converted = new CompoundStatement(this.statement, new WhileStatement(new NotExpression(this.expression), this.statement));
        executionStack.push(converted);
        state.setExecutionStack(executionStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!this.expression.typeCheck(typeEnv).equals(new BoolType())) {
            throw new StatementExecutionException(String.format("%s is not BoolType", this.expression));
        }

        this.statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("Repeat %s until %s", this.statement, this.expression);
    }
}
