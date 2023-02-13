package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.Type;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class ValueExpression implements IExpression{
    private final Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) {
        return this.value;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        return this.value.getType();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
