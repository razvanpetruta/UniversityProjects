package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;

public class WriteHeapStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public WriteHeapStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();

        if (!symbolTable.isDefined(this.varName)) {
            throw new StatementExecutionException(String.format("%s is not in SymbolTable", this.varName));
        }

        Value value = symbolTable.lookUp(this.varName);

        if (!(value.getType() instanceof RefType)) {
            throw new StatementExecutionException(String.format("%s is not RefType", value));
        }

        RefValue refValue = (RefValue) value;

        if (!heap.containsKey(refValue.getAddress())) {
            throw new StatementExecutionException(String.format("%s is not defined in the heap", refValue));
        }

        Value exprValue = this.expression.eval(symbolTable, heap);

        if (!exprValue.getType().equals(refValue.getLocationType())) {
            throw new StatementExecutionException(String.format("%s not of %s type", exprValue, refValue.getLocationType()));
        }

        heap.update(refValue.getAddress(), exprValue);
        state.setHeap(heap);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(this.varName).equals(new RefType(this.expression.typeCheck(typeEnv)))) {
            throw new StatementExecutionException("WriteHeap: right hand side and left hand side have different types");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("writeHeap(%s, %s)", this.varName, this.expression);
    }
}
