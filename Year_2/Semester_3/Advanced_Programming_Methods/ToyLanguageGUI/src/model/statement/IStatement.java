package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.Type;
import utils.MyIDictionary;

public interface IStatement {
    ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException;

    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException;
}
