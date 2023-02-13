package view;

import controller.Controller;
import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expression.ArithmeticExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.programState.ProgramState;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;
import repository.IRepository;
import repository.Repository;
import utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class View {
    private void showMenu() {
        System.out.println("MENU: ");
        System.out.println("0. Exit.");
        System.out.println("1. Run the first program: \n\tint v;\n\tv=2;\n\tprint(v)");
        System.out.println("2. Run the second program: \n\tint a;\n\tint b;\n\ta=2+3*5;\n\tb=a+1;\n\tprint(b)");
        System.out.println("3. Run the third program: \n\tbool a;\n\tint v;\n\ta=true;\n\t(If a Then v=2 Else v=3);\n\tprint(v)");
        System.out.println("Choose an option: ");
    }

    public void run() {
        boolean done = false;
        while (!done) {
            try {
                this.showMenu();
                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                if (option == 0) {
                    done = true;
                } else if (option == 1) {
                    this.runProgram1();
                } else if (option == 2) {
                    this.runProgram2();
                } else if (option == 3) {
                    this.runProgram3();
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void runProgram1() throws StatementExecutionException, ADTException, ExpressionEvaluationException, IOException, InterruptedException {
        IStatement s1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        this.runStatement(s1);
    }

    private void runProgram2() throws StatementExecutionException, ADTException, ExpressionEvaluationException, IOException, InterruptedException {
        IStatement s2 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression(
                                new ValueExpression(new IntValue(2)), new ArithmeticExpression(
                                        new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*')
                        , '+')), new CompoundStatement(new AssignStatement("b", new ArithmeticExpression(
                                new VariableExpression("a"), new ValueExpression(new IntValue(1)), '+')),
                                new PrintStatement(new VariableExpression("b"))))));
        this.runStatement(s2);
    }

    private void runProgram3() throws StatementExecutionException, ADTException, ExpressionEvaluationException, IOException, InterruptedException {
        IStatement s3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                new PrintStatement(new VariableExpression("v"))))));
        this.runStatement(s3);
    }

    private void runStatement(IStatement statement) throws StatementExecutionException, ADTException, ExpressionEvaluationException, IOException, InterruptedException {
        MyIStack<IStatement> executionStack = new MyStack<>();
        MyIDictionary<String, Value> symbolsTable = new MyDictionary<>();
        MyIList<Value> output = new MyList<>();
        MyIDictionary<String, BufferedReader> fileTable = new MyDictionary<>();

        ProgramState state = new ProgramState(executionStack, symbolsTable, output, fileTable, new MyHeap(), new MyLockTable(), new MySemaphoreTable(), new MyLatchTable(), statement);

        IRepository repository = new Repository(state, "log.txt");
        Controller controller = new Controller(repository);

        System.out.println("Do you want to display the steps? [y / n]");
        Scanner readOption = new Scanner(System.in);
        String option = readOption.next();
        controller.setDisplayFlag(Objects.equals(option, "y"));

        controller.allSteps();
    }
}
