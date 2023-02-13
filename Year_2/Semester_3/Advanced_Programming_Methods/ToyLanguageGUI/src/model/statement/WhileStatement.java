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

public class WhileStatement implements IStatement {
    private final IExpression expression;
    private final IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        Value value = this.expression.eval(state.getSymbolTable(), state.getHeap());
        MyIStack<IStatement> executionStack = state.getExecutionStack();

        if (!value.getType().equals(new BoolType())) {
            throw new StatementExecutionException(String.format("%s is not BoolType", value));
        }

        if (!(value instanceof BoolValue boolValue)) {
            throw new StatementExecutionException(String.format("%s is not BoolValue", value));
        }

        if (boolValue.getValue()) {
            executionStack.push(this);
            executionStack.push(this.statement);
            state.setExecutionStack(executionStack);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!this.expression.typeCheck(typeEnv).equals(new BoolType())) {
            throw new StatementExecutionException("The condition of WHILE does not have the bool type");
        }

        this.statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("while (%s) { %s }", this.expression, this.statement);
    }
}
