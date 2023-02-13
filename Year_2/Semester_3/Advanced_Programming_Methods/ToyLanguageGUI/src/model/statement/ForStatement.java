package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.expression.VariableExpression;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.Type;
import utils.MyIDictionary;
import utils.MyIStack;

public class ForStatement implements IStatement {
    private final String variable;
    private final IExpression expression1;
    private final IExpression expression2;
    private final IExpression expression3;
    private final IStatement statement;

    public ForStatement(String variable, IExpression expression1, IExpression expression2, IExpression expression3, IStatement statement) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIStack<IStatement> executionStack = state.getExecutionStack();
        IStatement converted = new CompoundStatement(new AssignStatement(this.variable, this.expression1), new WhileStatement(
                        new RelationalExpression(new VariableExpression(this.variable), this.expression2, "<"),
                        new CompoundStatement(this.statement, new AssignStatement(this.variable, this.expression3))
                ));
        executionStack.push(converted);
        state.setExecutionStack(executionStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(this.variable).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not IntType", this.variable));
        }

        if (!this.expression1.typeCheck(typeEnv).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not IntType", this.expression1));
        }

        if (!this.expression2.typeCheck(typeEnv).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not IntType", this.expression2));
        }

        if (!this.expression3.typeCheck(typeEnv).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not IntType", this.expression3));
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("for(%s = %s; %s < %s; %s = %s) { %s }", this.variable, this.expression1, this.variable,
                this.expression2, this.variable, this.expression3, this.statement);
    }
}
