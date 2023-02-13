package model.statement;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.IExpression;
import model.programState.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;
import utils.MyIDictionary;
import utils.MyIHeap;
import utils.MyILatchTable;

public class NewLatchStatement implements IStatement {
    private final String variable;
    private final IExpression expression;

    public NewLatchStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();
        MyILatchTable latchTable = state.getLatchTable();

        if (!this.expression.eval(symbolTable, heap).getType().equals(new IntType())) {
            throw new StatementExecutionException(this.expression + " is not IntType");
        }

        IntValue nr = (IntValue) (expression.eval(symbolTable, heap));
        int number = nr.getValue();
        int freeAddress = latchTable.getFreeAddress();
        latchTable.put(freeAddress, number);
        if (symbolTable.isDefined(variable)) {
            symbolTable.update(variable, new IntValue(freeAddress));
        } else {
            throw new StatementExecutionException(String.format("%s is not defined in the symbol table!", variable));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (!typeEnv.lookUp(variable).equals(new IntType())) {
            throw new StatementExecutionException(String.format("%s is not of int type!", this.variable));
        }

        if (!this.expression.typeCheck(typeEnv).equals(new IntType())) {
            throw new StatementExecutionException("Expression doesn't have the int type!");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("newLatch(%s, %s)", this.variable, this.expression);
    }
}
