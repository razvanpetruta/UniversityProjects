package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.Type;
import model.value.Value;
import utils.MyIDictionary;

public class AssignStatement implements IStatement{
    private final String id;
    private final IExpression expression;

    public AssignStatement(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIDictionary<String, Value> symbolsTable = state.getSymbolTable();

        if (!symbolsTable.isDefined(this.id)) {
            throw new StatementExecutionException(String.format("%s not in the symbol table", this.id));
        }

        Value value = this.expression.eval(symbolsTable, state.getHeap());
        Type typeId = symbolsTable.lookUp(this.id).getType();

        if (!value.getType().equals(typeId)) {
            throw new StatementExecutionException(String.format("%s and %s have different types", this.id, this.expression));
        }

        symbolsTable.update(this.id, value);
        state.setSymbolTable(symbolsTable);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type typeVar = typeEnv.lookUp(this.id);
        Type typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeVar.equals(typeExpression)) {
            throw new StatementExecutionException(String.format("%s and %s have different types", this.id, this.expression));
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", this.id, this.expression);
    }
}
