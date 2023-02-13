package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class RelationalExpression implements IExpression {
    private final IExpression expression1;
    private final IExpression expression2;
    private final String operator;

    public RelationalExpression(IExpression expression1, IExpression expression2, String operator) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operator = operator;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) throws ExpressionEvaluationException, ADTException {
        Value value1, value2;
        value1 = this.expression1.eval(symbolTable, heap);

        if (!value1.getType().equals(new IntType())) {
            throw new ExpressionEvaluationException("First operand is not an integer");
        }

        value2 = this.expression2.eval(symbolTable, heap);

        if (!value2.getType().equals(new IntType())) {
            throw new ExpressionEvaluationException("Second operand is not an integer");
        }

        IntValue v1 = (IntValue) value1;
        IntValue v2 = (IntValue) value2;
        int int1, int2;
        int1 = v1.getValue();
        int2 = v2.getValue();
        switch (this.operator) {
            case "<":
                return new BoolValue(int1 < int2);
            case "<=":
                return new BoolValue(int1 <= int2);
            case "==":
                return new BoolValue(int1 == int2);
            case "!=":
                return new BoolValue(int1 != int2);
            case ">":
                return new BoolValue(int1 > int2);
            case ">=":
                return new BoolValue(int1 >= int2);
        }

        return null;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        Type type1, type2;
        type1 = this.expression1.typeCheck(typeEnv);
        type2 = this.expression2.typeCheck(typeEnv);

        if (!type1.equals(new IntType())) {
            throw new ExpressionEvaluationException("First operand is not an integer");
        }

        if (!type2.equals(new IntType())) {
            throw new ExpressionEvaluationException("Second operand is not an integer");
        }

        return new BoolType();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.expression1, this.operator, this.expression2);
    }
}
