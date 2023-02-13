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

public class NewStatement implements IStatement {
    private String varName;
    private IExpression expression;

    public NewStatement(String varName, IExpression expression) {
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

        Value varValue = symbolTable.lookUp(this.varName);

        if (! (varValue.getType() instanceof RefType)) {
            throw new StatementExecutionException(String.format("%s is not RefType", this.varName));
        }

        Value exprValue = this.expression.eval(symbolTable, state.getHeap());
        Type locationType = ((RefValue) varValue).getLocationType();

        if (!locationType.equals(exprValue.getType())) {
            throw new StatementExecutionException(String.format("%s not of %s type", this.varName, exprValue.getType()));
        }

        int newPosition = heap.add(exprValue);
        symbolTable.put(this.varName, new RefValue(newPosition, locationType));
        state.setSymbolTable(symbolTable);
        state.setHeap(heap);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type typeVar = typeEnv.lookUp(this.varName);
        Type typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeVar.equals(new RefType(typeExpression))) {
            throw new StatementExecutionException("NEW statement: right hand side and left hand side have different types");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("new(%s, %s)", this.varName, this.expression);
    }
}
