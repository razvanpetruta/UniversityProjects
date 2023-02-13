package model.expression;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class ReadHeapExpression implements IExpression {
    private final IExpression expression;

    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symbolTable, MyIHeap heap) throws ExpressionEvaluationException, ADTException {
        Value value = this.expression.eval(symbolTable, heap);

        if (!(value instanceof RefValue refValue)) {
            throw new ExpressionEvaluationException(String.format("%s is not RefType", value));
        }

        if (!heap.containsKey(refValue.getAddress())) {
            throw new ExpressionEvaluationException(String.format("%s is not defined on the heap", refValue.getAddress()));
        }

        return heap.get(refValue.getAddress());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        Type type = this.expression.typeCheck(typeEnv);
        if (!(type instanceof RefType refType)) {
            throw new ExpressionEvaluationException("The readHeap argument is not RefType");
        }

        return refType.getInner();
    }

    @Override
    public String toString() {
        return String.format("readHeap(%s)", this.expression);
    }
}
