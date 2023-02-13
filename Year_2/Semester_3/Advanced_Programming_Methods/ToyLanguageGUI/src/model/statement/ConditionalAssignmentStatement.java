package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.BoolType;
import model.type.Type;
import utils.MyIDictionary;
import utils.MyIStack;

public class ConditionalAssignmentStatement implements IStatement {
    private final String variable;
    private final IExpression expression1;
    private final IExpression expression2;
    private final IExpression expression3;

    public ConditionalAssignmentStatement(String variable, IExpression expression1, IExpression expression2, IExpression expression3) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIStack<IStatement> executionStack = state.getExecutionStack();
        IStatement converted = new IfStatement(this.expression1, new AssignStatement(this.variable, this.expression2),
                new AssignStatement(this.variable, this.expression3));
        executionStack.push(converted);
        state.setExecutionStack(executionStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.isDefined(this.variable)) {
            throw new StatementExecutionException(String.format("%s is not defined", this.variable));
        }

        if (!this.expression1.typeCheck(typeEnv).equals(new BoolType())) {
            throw new StatementExecutionException(String.format("%s is not BoolType", this.expression1));
        }

        if (!typeEnv.lookUp(this.variable).equals(this.expression2.typeCheck(typeEnv))) {
            throw new StatementExecutionException(String.format("%s and %s have different types", this.variable, this.expression2));
        }

        if (!typeEnv.lookUp(this.variable).equals(this.expression3.typeCheck(typeEnv))) {
            throw new StatementExecutionException(String.format("%s and %s have different types", this.variable, this.expression3));
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("%s = %s ? %s : %s", this.variable, this.expression1, this.expression2, this.expression3);
    }
}
