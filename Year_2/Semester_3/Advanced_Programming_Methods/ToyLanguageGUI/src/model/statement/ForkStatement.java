package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.Type;
import model.value.Value;
import utils.MyDictionary;
import utils.MyIDictionary;
import utils.MyIStack;
import utils.MyStack;

import java.util.Map;

public class ForkStatement implements IStatement {
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIStack<IStatement> newStack = new MyStack<>();
        newStack.push(this.statement);
        MyIDictionary<String, Value> newSymbolTable = new MyDictionary<>();
        for (Map.Entry<String, Value> entry: state.getSymbolTable().getContent().entrySet()) {
            newSymbolTable.put(entry.getKey(), entry.getValue().deepCopy());
        }

        return new ProgramState(newStack, newSymbolTable, state.getOut(), state.getFileTable(), state.getHeap(), state.getLockTable(), state.getSemaphoreTable(), state.getLatchTable());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        this.statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("fork(%s)", this.statement);
    }
}
