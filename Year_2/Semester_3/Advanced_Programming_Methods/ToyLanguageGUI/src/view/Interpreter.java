//package view;
//
//import controller.Controller;
//import exceptions.ADTException;
//import exceptions.ExpressionEvaluationException;
//import exceptions.StatementExecutionException;
//import model.expression.*;
//import model.programState.ProgramState;
//import model.statement.*;
//import model.type.BoolType;
//import model.type.IntType;
//import model.type.RefType;
//import model.type.StringType;
//import model.value.BoolValue;
//import model.value.IntValue;
//import model.value.StringValue;
//import repository.IRepository;
//import repository.Repository;
//import utils.*;
//
//import java.io.IOException;
//
//public class Interpreter {
//    public static void main(String[] args) {
//        TextMenu menu = new TextMenu();
//        menu.addCommand(new ExitCommand("0", "exit"));
//
//        IStatement s1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
//                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
//                        new PrintStatement(new VariableExpression("v"))));
//        try {
//            s1.typeCheck(new MyDictionary<>());
//            ProgramState prg1 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s1);
//            IRepository repo1 = new Repository(prg1, "src/files/log1.txt");
//            Controller controller1 = new Controller(repo1);
//            menu.addCommand(new RunExampleCommand("1", s1.toString(), controller1));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s2 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
//                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
//                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression(
//                                new ValueExpression(new IntValue(2)), new ArithmeticExpression(
//                                new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*')
//                                , '+')), new CompoundStatement(new AssignStatement("b", new ArithmeticExpression(
//                                new VariableExpression("a"), new ValueExpression(new IntValue(1)), '+')),
//                                new PrintStatement(new VariableExpression("b"))))));
//        try {
//            s2.typeCheck(new MyDictionary<>());
//            ProgramState prg2 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s2);
//            IRepository repo2 = new Repository(prg2, "src/files/log2.txt");
//            Controller controller2 = new Controller(repo2);
//            menu.addCommand(new RunExampleCommand("2", s2.toString(), controller2));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
//                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
//                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
//                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
//                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
//                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
//                                        new PrintStatement(new VariableExpression("v"))))));
//        try {
//            s3.typeCheck(new MyDictionary<>());
//            ProgramState prg3 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s3);
//            IRepository repo3 = new Repository(prg3, "src/files/log3.txt");
//            Controller controller3 = new Controller(repo3);
//            menu.addCommand(new RunExampleCommand("3", s3.toString(), controller3));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s4 = new CompoundStatement(new VariableDeclarationStatement("varF", new StringType()),
//                new CompoundStatement(new AssignStatement("varF", new ValueExpression(new StringValue("src/files/test.in"))),
//                    new CompoundStatement(new OpenReadFile(new VariableExpression("varF")),
//                        new CompoundStatement(new VariableDeclarationStatement("varC", new IntType()),
//                            new CompoundStatement(new ReadFile(new VariableExpression("varF"), "varC"),
//                                new CompoundStatement(new PrintStatement(new VariableExpression("varC")),
//                                        new CompoundStatement(new ReadFile(new VariableExpression("varF"), "varC"),
//                                                new CompoundStatement(new PrintStatement(new VariableExpression("varC")),
//                                                        new CloseReadFile(new VariableExpression("varF"))))))))));
//        try {
//            s4.typeCheck(new MyDictionary<>());
//            ProgramState prg4 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s4);
//            IRepository repo4 = new Repository(prg4, "src/files/log4.txt");
//            Controller controller4 = new Controller(repo4);
//            menu.addCommand(new RunExampleCommand("4", s4.toString(), controller4));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s5 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
//                new CompoundStatement(new AssignStatement("a", new ValueExpression(new IntValue(3))),
//                        new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
//                        new CompoundStatement(new AssignStatement("b", new ValueExpression(new IntValue(5))),
//                                new IfStatement(new RelationalExpression(new VariableExpression("a"),
//                                        new VariableExpression("b"), "<="), new PrintStatement(
//                                                new VariableExpression("a")), new PrintStatement(
//                                                        new VariableExpression("b")
//                                ))))));
//        try {
//            s5.typeCheck(new MyDictionary<>());
//            ProgramState prg5 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s5);
//            IRepository repo5 = new Repository(prg5, "src/files/log5.txt");
//            Controller controller5 = new Controller(repo5);
//            menu.addCommand(new RunExampleCommand("5", s5.toString(), controller5));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s6 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
//                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
//                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
//                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
//                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
//                                                new PrintStatement(new VariableExpression("a")))))));
//        try {
//            s6.typeCheck(new MyDictionary<>());
//            ProgramState prg6 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s6);
//            IRepository repo6 = new Repository(prg6, "src/files/log6.txt");
//            Controller controller6 = new Controller(repo6);
//            menu.addCommand(new RunExampleCommand("6", s6.toString(), controller6));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s7 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
//                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
//                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
//                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
//                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
//                                                new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)), '+')))))));
//        try {
//            s7.typeCheck(new MyDictionary<>());
//            ProgramState prg7 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s7);
//            IRepository repo7 = new Repository(prg7, "src/files/log7.txt");
//            Controller controller7 = new Controller(repo7);
//            menu.addCommand(new RunExampleCommand("7", s7.toString(), controller7));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s8 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
//                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
//                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
//                                new CompoundStatement(new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
//                                        new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5)), '+'))))));
//        try {
//            s8.typeCheck(new MyDictionary<>());
//            ProgramState prg8 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s8);
//            IRepository repo8 = new Repository(prg8, "src/files/log8.txt");
//            Controller controller8 = new Controller(repo8);
//            menu.addCommand(new RunExampleCommand("8", s8.toString(), controller8));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s9 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
//                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
//                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
//                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
//                                        new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
//                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));
//        try {
//            s9.typeCheck(new MyDictionary<>());
//            ProgramState prg9 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s9);
//            IRepository repo9 = new Repository(prg9, "src/files/log9.txt");
//            Controller controller9 = new Controller(repo9);
//            menu.addCommand(new RunExampleCommand("9", s9.toString(), controller9));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s10 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
//                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
//                        new CompoundStatement(new WhileStatement(new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
//                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')))),
//                                new PrintStatement(new VariableExpression("v")))));
//        try {
//            s10.typeCheck(new MyDictionary<>());
//            ProgramState prg10 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s10);
//            IRepository repo10 = new Repository(prg10, "src/files/log10.txt");
//            Controller controller10 = new Controller(repo10);
//            menu.addCommand(new RunExampleCommand("10", s10.toString(), controller10));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s11 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
//                new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
//                        new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
//                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))),
//                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
//                                                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(32))),
//                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
//                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));
//        try {
//            s11.typeCheck(new MyDictionary<>());
//            ProgramState prg11 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s11);
//            IRepository repo11 = new Repository(prg11, "src/files/log11.txt");
//            Controller controller11 = new Controller(repo11);
//            menu.addCommand(new RunExampleCommand("11", s11.toString(), controller11));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        IStatement s12 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
//                new AssignStatement("v", new ValueExpression(new StringValue("ana"))));
//
////        IStatement s12 = new CompoundStatement(new VariableDeclarationStatement("varF", new StringType()),
////                new CompoundStatement(new AssignStatement("varF", new ValueExpression(new StringValue("src/files/test.in"))),
////                        new CompoundStatement(new OpenReadFile(new VariableExpression("varF")),
////                                new CompoundStatement(new VariableDeclarationStatement("varC", new StringType()),
////                                        new CompoundStatement(new ReadFile(new VariableExpression("varF"), "varC"),
////                                                new CompoundStatement(new PrintStatement(new VariableExpression("varC")),
////                                                        new CompoundStatement(new ReadFile(new VariableExpression("varF"), "varC"),
////                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varC")),
////                                                                        new CloseReadFile(new VariableExpression("varF"))))))))));
//
//        try {
//            s12.typeCheck(new MyDictionary<>());
//            ProgramState prg12 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), s12);
//            IRepository repo12 = new Repository(prg12, "src/files/log12.txt");
//            Controller controller12 = new Controller(repo12);
//            menu.addCommand(new RunExampleCommand("12", s12.toString(), controller12));
//        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException e) {
//            System.out.println(e.getMessage());
//        }
//
//        menu.show();
//    }
//}
