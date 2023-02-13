package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.Type;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class VariableExpression implements IExpression{
    private final String id;

    public VariableExpression(String id) {
        this.id = id;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) throws ADTException {
        return symbolTable.lookUp(this.id);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        return typeEnv.lookUp(this.id);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
