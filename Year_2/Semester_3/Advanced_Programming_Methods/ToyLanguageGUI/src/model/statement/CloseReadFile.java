package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.StringType;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;
import utils.MyIDictionary;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements IStatement {
    private final IExpression expression;

    public CloseReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        Value value = this.expression.eval(symbolTable, state.getHeap());

        if (!value.getType().equals(new StringType())) {
            throw new StatementExecutionException(String.format("%s is not StringType", this.expression));
        }

        StringValue fileName = (StringValue) value;

        if (!fileTable.isDefined(fileName.getValue())) {
            throw new StatementExecutionException(String.format("%s is not in FileTable", fileName));
        }

        BufferedReader br = fileTable.lookUp(fileName.getValue());
        try {
            br.close();
        } catch (IOException e) {
            throw new StatementExecutionException("Unexpected error in closing " + fileName);
        }
        fileTable.remove(fileName.getValue());
        state.setFileTable(fileTable);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!this.expression.typeCheck(typeEnv).equals(new StringType())) {
            throw new StatementExecutionException("CloseReadFile requires a string in the expression");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("closeReadFile(%s)", this.expression);
    }
}
