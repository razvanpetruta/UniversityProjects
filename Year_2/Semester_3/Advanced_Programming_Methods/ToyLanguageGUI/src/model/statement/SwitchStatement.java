package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.programState.ProgramState;
import model.type.Type;
import utils.MyIDictionary;
import utils.MyIStack;

public class SwitchStatement implements IStatement {
    private final IExpression mainExpression;
    private final IExpression expression1;
    private final IStatement statement1;
    private final IExpression expression2;
    private final IStatement statement2;
    private final IStatement defaultStatement;

    public SwitchStatement(IExpression mainExpression, IExpression expression1, IStatement statement1, IExpression expression2, IStatement statement2, IStatement defaultStatement) {
        this.mainExpression = mainExpression;
        this.expression1 = expression1;
        this.statement1 = statement1;
        this.expression2 = expression2;
        this.statement2 = statement2;
        this.defaultStatement = defaultStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIStack<IStatement> executionStack = state.getExecutionStack();
        IStatement converted = new IfStatement(new RelationalExpression(this.mainExpression, this.expression1, "=="),
                this.statement1, new IfStatement(new RelationalExpression(this.mainExpression, this.expression2, "=="),
                this.statement2, this.defaultStatement));
        executionStack.push(converted);
        state.setExecutionStack(executionStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type mainType = this.mainExpression.typeCheck(typeEnv);
        Type type1 = this.expression1.typeCheck(typeEnv);
        Type type2 = this.expression2.typeCheck(typeEnv);

        if (!mainType.equals(type1)) {
            throw new StatementExecutionException(String.format("%s and %s have different types", this.mainExpression, this.expression1));
        }

        if (!mainType.equals(type2)) {
            throw new StatementExecutionException(String.format("%s and %s have different types", this.mainExpression, this.expression2));
        }

        this.statement1.typeCheck(typeEnv.deepCopy());
        this.statement2.typeCheck(typeEnv.deepCopy());
        this.defaultStatement.typeCheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("switch (%s) (case %s: %s) (case %s: %s) (default: %s)", this.mainExpression, this.expression1,
                this.statement1, this.expression2, this.statement2, this.defaultStatement);
    }
}
