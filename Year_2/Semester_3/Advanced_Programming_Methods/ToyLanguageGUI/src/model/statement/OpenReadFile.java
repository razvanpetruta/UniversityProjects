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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFile implements IStatement {
    private final IExpression expression;

    public OpenReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        Value value = this.expression.eval(state.getSymbolTable(), state.getHeap());

        if (!value.getType().equals(new StringType()))  {
            throw new StatementExecutionException(String.format("%s is not String Type", this.expression));
        }

        StringValue fileName = (StringValue) value;
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if (fileTable.isDefined(fileName.getValue())) {
            throw new StatementExecutionException(String.format("%s is already opened", fileName));
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName.getValue()));
        } catch (FileNotFoundException e) {
            throw new StatementExecutionException(fileName + " could not be opened");
        }
        fileTable.put(fileName.getValue(), br);
        state.setFileTable(fileTable);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!this.expression.typeCheck(typeEnv).equals(new StringType())) {
            throw new StatementExecutionException(String.format("%s is not String Type", this.expression));
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("openReadFile(%s)", this.expression);
    }
}
