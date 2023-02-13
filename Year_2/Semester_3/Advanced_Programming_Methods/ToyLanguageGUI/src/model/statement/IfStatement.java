package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIStack;

public class IfStatement implements IStatement{
    private final IExpression expression;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        Value result = this.expression.eval(state.getSymbolTable(), state.getHeap());

        if (! (result instanceof BoolValue boolResult)) {
            throw new StatementExecutionException(String.format("%s is not BoolType", this.expression));
        }

        IStatement statement;
        if (boolResult.getValue()) {
            statement = thenStatement;
        } else {
            statement = elseStatement;
        }

        MyIStack<IStatement> stack = state.getExecutionStack();
        stack.push(statement);
        state.setExecutionStack(stack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeExpression.equals(new BoolType())) {
            throw new StatementExecutionException(String.format("%s is not BoolType", this.expression));
        }

        this.thenStatement.typeCheck(typeEnv.deepCopy());
        this.elseStatement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("if (%s) then %s else %s", this.expression, this.thenStatement, this.elseStatement);
    }
}
