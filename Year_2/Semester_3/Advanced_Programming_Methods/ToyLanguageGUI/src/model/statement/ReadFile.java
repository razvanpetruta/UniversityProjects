package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.StringType;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;
import utils.MyIDictionary;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStatement {
    private final IExpression expression;
    private final String varName;

    public ReadFile(IExpression expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if (!symbolTable.isDefined(this.varName)) {
            throw new StatementExecutionException(String.format("%s is not defined in the SymbolTable", this.varName));
        }

        Value value = symbolTable.lookUp(this.varName);

        if (!value.getType().equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not IntType", value));
        }

        value = this.expression.eval(symbolTable, state.getHeap());

        if (!value.getType().equals(new StringType())) {
            throw new StatementExecutionException(String.format("%s is not StringType", value));
        }

        StringValue castValue = (StringValue) value;

        if (!fileTable.isDefined(castValue.getValue())) {
            throw new StatementExecutionException(String.format("%s not in the FileTable", castValue));
        }

        BufferedReader br = fileTable.lookUp(castValue.getValue());
        try {
            String line = br.readLine();
            if (line == null) {
                line = "0";
            }
            symbolTable.put(varName, new IntValue(Integer.parseInt(line)));
            state.setSymbolTable(symbolTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!this.expression.typeCheck(typeEnv).equals(new StringType())) {
            throw new StatementExecutionException("ReadFile requires a string in its expression parameter");
        }

        if (!typeEnv.lookUp(this.varName).equals(new IntType())) {
            throw new StatementExecutionException("ReadFile requires an int as its variable parameter");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("readFile(%s, %s)", this.expression, this.varName);
    }
}
