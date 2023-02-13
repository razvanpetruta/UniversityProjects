package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.IntType;
import model.type.Type;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class MULExpression implements IExpression {
    private final IExpression expression1;
    private final IExpression expression2;

    public MULExpression(IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) throws ExpressionEvaluationException, ADTException {
        IExpression converted = new ArithmeticExpression(
                new ArithmeticExpression(this.expression1, this.expression2, '*'),
                new ArithmeticExpression(this.expression1, this.expression2, '+'),
                '-'
        );
        return converted.eval(symbolTable, heap);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        if (!this.expression1.typeCheck(typeEnv).equals(new IntType())) {
            throw new ExpressionEvaluationException(this.expression1 + " is not IntType");
        }

        if (!this.expression2.typeCheck(typeEnv).equals(new IntType())) {
            throw new ExpressionEvaluationException(this.expression2 + " is not IntType");
        }

        return new IntType();
    }

    @Override
    public String toString() {
        return String.format("MUL(%s, %s)", this.expression1, this.expression2);
    }
}
