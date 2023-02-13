package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

import java.util.Objects;

public class LogicExpression implements IExpression{
    private final IExpression expression1;
    private final IExpression expression2;
    private final String operation;

    public LogicExpression(IExpression expression1, IExpression expression2, String operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) throws ExpressionEvaluationException, ADTException {
        Value value1, value2;
        value1 = this.expression1.eval(symbolTable, heap);

        if (!value1.getType().equals(new BoolType())) {
            throw new ExpressionEvaluationException("First operand is not boolean");
        }

        value2 = this.expression2.eval(symbolTable, heap);

        if (!value2.getType().equals(new BoolType())) {
            throw new ExpressionEvaluationException("Second operand is not boolean");
        }

        BoolValue bool1 = (BoolValue) value1;
        BoolValue bool2 = (BoolValue) value2;
        boolean b1, b2;
        b1 = bool1.getValue();
        b2 = bool2.getValue();
        if (Objects.equals(this.operation, "and")) {
            return new BoolValue(b1 && b2);
        } else if (Objects.equals(this.operation, "or")) {
            return new BoolValue(b1 || b2);
        }

        return null;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        Type type1, type2;
        type1 = this.expression1.typeCheck(typeEnv);
        type2 = this.expression2.typeCheck(typeEnv);

        if (!type1.equals(new BoolType())) {
            throw new ExpressionEvaluationException("First operand is not boolean");
        }

        if (!type2.equals(new BoolType())) {
            throw new ExpressionEvaluationException("Second operand is not boolean");
        }

        return new BoolType();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.expression1, this.operation, this.expression2);
    }
}
