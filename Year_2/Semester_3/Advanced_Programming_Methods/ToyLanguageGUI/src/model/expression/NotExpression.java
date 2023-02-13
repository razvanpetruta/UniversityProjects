package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class NotExpression implements IExpression {
    private final IExpression expression;

    public NotExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) throws ExpressionEvaluationException, ADTException {
        Value val = this.expression.eval(symbolTable, heap);

        if (!val.getType().equals(new BoolType())) {
            throw new ExpressionEvaluationException(String.format("%s is not BoolType", this.expression));
        }

        if (!((BoolValue) val).getValue()) {
            return new BoolValue(true);
        } else {
            return new BoolValue(false);
        }
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        return this.expression.typeCheck(typeEnv);
    }

    @Override
    public String toString() {
        return String.format("not(" + this.expression + ")");
    }
}
