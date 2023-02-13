package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.Type;
import utils.MyIDictionary;
import utils.MyIStack;

public class CompoundStatement implements IStatement{
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        MyIStack<IStatement> stack = state.getExecutionStack();
        stack.push(this.second);
        stack.push(this.first);
        state.setExecutionStack(stack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        return this.second.typeCheck(this.first.typeCheck(typeEnv));
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.first, this.second);
    }
}
