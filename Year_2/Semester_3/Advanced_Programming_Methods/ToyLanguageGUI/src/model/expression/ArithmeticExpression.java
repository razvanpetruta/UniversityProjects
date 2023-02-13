package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class ArithmeticExpression implements IExpression{
    private final IExpression expression1;
    private final IExpression expression2;
    private final char operation;

    public ArithmeticExpression(IExpression expression1, IExpression expression2, char operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
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

        IntValue int1 = (IntValue) value1;
        IntValue int2 = (IntValue) value2;
        int n1, n2;
        n1 = int1.getValue();
        n2 = int2.getValue();
        switch (this.operation) {
            case '+':
                return new IntValue(n1 + n2);
            case '-':
                return new IntValue(n1 - n2);
            case '*':
                return new IntValue(n1 * n2);
            case '/':
                if (n2 == 0) {
                    throw new ExpressionEvaluationException("Division by 0");
                }
                return new IntValue(n1 / n2);
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

        return new IntType();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.expression1, this.operation, this.expression2);
    }
}
