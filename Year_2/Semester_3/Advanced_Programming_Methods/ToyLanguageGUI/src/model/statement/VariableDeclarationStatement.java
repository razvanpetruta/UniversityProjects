package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.Type;
import model.value.Value;
import utils.MyIDictionary;

public class VariableDeclarationStatement implements IStatement{
    private final String name;
    private final Type type;

    public VariableDeclarationStatement(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException {
        MyIDictionary<String, Value> symbolsTable = state.getSymbolTable();

        if (symbolsTable.isDefined(this.name)) {
            throw new StatementExecutionException(String.format("%s already in the SymbolTable", this.name));
        }

        symbolsTable.put(this.name, this.type.defaultValue());
        state.setSymbolTable(symbolsTable);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        typeEnv.put(this.name, this.type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.name;
    }
}
