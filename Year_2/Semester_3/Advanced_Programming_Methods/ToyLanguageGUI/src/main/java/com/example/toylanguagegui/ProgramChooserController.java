package com.example.toylanguagegui;

import controller.Controller;
import repository.Repository;
import repository.IRepository;
import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.expression.*;
import model.programState.ProgramState;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.RefType;
import model.type.StringType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import utils.*;

import java.util.ArrayList;
import java.util.List;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }
    @FXML
    private ListView<IStatement> programsListView;

    @FXML
    public void initialize() {
        this.programsListView.setItems(this.getAllStatements());
        this.programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent actionEvent) {
        IStatement selectedStatement = this.programsListView.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered!");
            alert.setContentText("No statement selected!");
            alert.showAndWait();
        } else {
            int id = this.programsListView.getSelectionModel().getSelectedIndex();
            try {
                selectedStatement.typeCheck(new MyDictionary<>());
                ProgramState programState = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), new MyLatchTable(), selectedStatement);
                IRepository repository = new Repository(programState, "src/files/log" + (id + 1) + ".txt");
                Controller controller = new Controller(repository);
                this.programExecutorController.setController(controller);
            } catch (StatementExecutionException | ExpressionEvaluationException | ADTException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error encountered!");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private ObservableList<IStatement> getAllStatements() {
        List<IStatement> allStatements = new ArrayList<>();

        IStatement s1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        allStatements.add(s1);

        IStatement s2 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression(
                                new ValueExpression(new IntValue(2)), new ArithmeticExpression(
                                new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*')
                                , '+')), new CompoundStatement(new AssignStatement("b", new ArithmeticExpression(
                                new VariableExpression("a"), new ValueExpression(new IntValue(1)), '+')),
                                new PrintStatement(new VariableExpression("b"))))));
        allStatements.add(s2);

        IStatement s3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
        allStatements.add(s3);

        IStatement s4 = new CompoundStatement(new VariableDeclarationStatement("varF", new StringType()),
                new CompoundStatement(new AssignStatement("varF", new ValueExpression(new StringValue("src/files/test.in"))),
                        new CompoundStatement(new OpenReadFile(new VariableExpression("varF")),
                                new CompoundStatement(new VariableDeclarationStatement("varC", new IntType()),
                                        new CompoundStatement(new ReadFile(new VariableExpression("varF"), "varC"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varC")),
                                                        new CompoundStatement(new ReadFile(new VariableExpression("varF"), "varC"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varC")),
                                                                        new CloseReadFile(new VariableExpression("varF"))))))))));
        allStatements.add(s4);

        IStatement s5 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new AssignStatement("a", new ValueExpression(new IntValue(3))),
                        new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                                new CompoundStatement(new AssignStatement("b", new ValueExpression(new IntValue(5))),
                                        new IfStatement(new RelationalExpression(new VariableExpression("a"),
                                                new VariableExpression("b"), "<="), new PrintStatement(
                                                new VariableExpression("a")), new PrintStatement(
                                                new VariableExpression("b")
                                        ))))));
        allStatements.add(s5);

        IStatement s6 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a")))))));
        allStatements.add(s6);

        IStatement s7 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)), '+')))))));
        allStatements.add(s7);

        IStatement s8 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5)), '+'))))));
        allStatements.add(s8);

        IStatement s9 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5)), '+'))))));
        allStatements.add(s9);

        IStatement s10 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));
        allStatements.add(s10);

        IStatement s11 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')))),
                                new PrintStatement(new VariableExpression("v")))));
        allStatements.add(s11);

        IStatement s12 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));
        allStatements.add(s12);

        IStatement s13 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(3))),
                        new WhileStatement(new RelationalExpression(new VariableExpression("v"), new ValueExpression(
                                new IntValue(0)), ">"), new CompoundStatement(new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')))),
                                new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-'))))));
        allStatements.add(s13);

        IStatement s14 = new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                new CompoundStatement(new ForStatement("v", new ValueExpression(new IntValue(0)),
                                        new ValueExpression(new IntValue(3)), new ArithmeticExpression(
                                        new VariableExpression("v"), new ValueExpression(new IntValue(1)), '+'
                                ), new ForkStatement(new CompoundStatement(
                                        new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v", new ArithmeticExpression(
                                                new VariableExpression("v"), new ReadHeapExpression(new VariableExpression("a")), '+'
                                        ))
                                ))), new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))));
        allStatements.add(s14);

        IStatement s15 = new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                new CompoundStatement(new VariableDeclarationStatement("b", new RefType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(0))),
                                        new CompoundStatement(new NewStatement("b", new ValueExpression(new IntValue(0))),
                                                new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(1))),
                                                        new CompoundStatement(new WriteHeapStatement("b", new ValueExpression(new IntValue(2))),
                                                                new CompoundStatement(new ConditionalAssignmentStatement("v", new RelationalExpression(
                                                                        new ReadHeapExpression(new VariableExpression("a")), new ReadHeapExpression(new VariableExpression("b")),
                                                                        "<"), new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200))), new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("v")), new CompoundStatement(new ConditionalAssignmentStatement(
                                                                        "v", new RelationalExpression(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("b")), new ValueExpression(new IntValue(2)), '-'),
                                                                        new ReadHeapExpression(new VariableExpression("a")), ">")
                                                                        , new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200))), new PrintStatement(new VariableExpression("v"))))
                                                                ))))))));
        allStatements.add(s15);

        IStatement s16 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()), new CompoundStatement(
                new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(new VariableDeclarationStatement("c", new IntType()),
                new CompoundStatement(new AssignStatement("a", new ValueExpression(new IntValue(1))), new CompoundStatement(
                        new AssignStatement("b", new ValueExpression(new IntValue(2))), new CompoundStatement(
                        new AssignStatement("c", new ValueExpression(new IntValue(5))), new CompoundStatement(new SwitchStatement(
                        new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(10)), '*'),
                        new ArithmeticExpression(new VariableExpression("b"), new VariableExpression("c"), '*'),
                        new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("b"))),
                        new ValueExpression(new IntValue(10)),
                        new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(100))), new PrintStatement(new ValueExpression(new IntValue(200)))),
                        new PrintStatement(new ValueExpression(new IntValue(300)))
                ), new PrintStatement(new ValueExpression(new IntValue(300))))
                )
                )))
        ));
        allStatements.add(s16);

        IStatement s17 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("x", new IntType()),
                        new CompoundStatement(new VariableDeclarationStatement("y", new IntType()),
                                new CompoundStatement(new VariableDeclarationStatement("z", new IntType()),
                                        new CompoundStatement(new VariableDeclarationStatement("w", new IntType()),
                                                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(0))),
                                                        new CompoundStatement(new RepeatUntilStatement(new CompoundStatement(
                                                                new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                        new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')))),
                                                                new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '+'))),
                                                                new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(3)), "==")
                                                        ),
                                                                new CompoundStatement(new AssignStatement("x", new ValueExpression(new IntValue(1))),
                                                                        new CompoundStatement(new AssignStatement("y", new ValueExpression(new IntValue(2))),
                                                                                new CompoundStatement(new AssignStatement("z", new ValueExpression(new IntValue(3))),
                                                                                        new CompoundStatement(new AssignStatement("w", new ValueExpression(new IntValue(4))),
                                                                                                new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), '*'))))))
                                                        )))))));
        allStatements.add(s17);

        IStatement s18 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                        new CompoundStatement(new ForkStatement(new CompoundStatement(
                                new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')),
                                new CompoundStatement(new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), '-')),
                                        new PrintStatement(new VariableExpression("v")))
                        )), new CompoundStatement(new SleepStatement(10), new PrintStatement(
                                new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), '*')
                        )))));
        allStatements.add(s18);

        IStatement s19 = new CompoundStatement(new VariableDeclarationStatement("v1", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("v2", new IntType()),
                        new CompoundStatement(new AssignStatement("v1", new ValueExpression(new IntValue(2))),
                                new CompoundStatement(new AssignStatement("v2", new ValueExpression(new IntValue(3))),
                                        new IfStatement(new RelationalExpression(new VariableExpression("v1"), new ValueExpression(new IntValue(0)), "!="),
                                                new PrintStatement(new MULExpression(new VariableExpression("v1"), new VariableExpression("v2"))),
                                                new PrintStatement(new VariableExpression("v1")))))));
        allStatements.add(s19);

        IStatement s20 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new WaitStatement(10),
                                new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), '*')))));
        allStatements.add(s20);

        IStatement s21 = new CompoundStatement(new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompoundStatement(new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("x", new IntType()),
                                new CompoundStatement(new VariableDeclarationStatement("q", new IntType()),
                                        new CompoundStatement(new NewStatement("v1", new ValueExpression(new IntValue(20))),
                                                new CompoundStatement(new NewStatement("v2", new ValueExpression(new IntValue(30))),
                                                        new CompoundStatement(new NewLockStatement("x"),
                                                                new CompoundStatement(new ForkStatement(
                                                                        new CompoundStatement(new ForkStatement(
                                                                                new CompoundStatement(new LockStatement("x"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v1", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), '-')),
                                                                                                new UnlockStatement("x")))
                                                                        ),
                                                                                new CompoundStatement(new LockStatement("x"),
                                                                                        new CompoundStatement(new WriteHeapStatement("v1", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), '*')),
                                                                                                new UnlockStatement("x"))))
                                                                ),
                                                                        new CompoundStatement( new NewLockStatement("q"),
                                                                                new CompoundStatement(new ForkStatement(
                                                                                        new CompoundStatement( new ForkStatement(
                                                                                                new CompoundStatement(new LockStatement("q"),
                                                                                                        new CompoundStatement(new WriteHeapStatement("v2", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(5)), '+')),
                                                                                                                new UnlockStatement("q")))
                                                                                        ),
                                                                                                new CompoundStatement(new LockStatement("q"), new CompoundStatement(new WriteHeapStatement("v2", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), '*')),
                                                                                                        new UnlockStatement("q"))))
                                                                                ),
                                                                                        new CompoundStatement(new NopStatement(),
                                                                                                new CompoundStatement(new NopStatement(),
                                                                                                        new CompoundStatement(new NopStatement(),
                                                                                                                new CompoundStatement(new NopStatement(),
                                                                                                                        new CompoundStatement(new LockStatement("x"),
                                                                                                                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                                        new CompoundStatement(new UnlockStatement("x"),
                                                                                                                                                new CompoundStatement(new LockStatement("q"),
                                                                                                                                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                                                new UnlockStatement("q"))))))))))))))))))));

        allStatements.add(s21);

        IStatement s22 = new CompoundStatement(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompoundStatement(
                        new VariableDeclarationStatement("cnt", new IntType()),
                        new CompoundStatement(
                                new NewStatement("v1", new ValueExpression(new IntValue(1))),
                                new CompoundStatement(
                                        new CreateSemaphoreStatement("cnt", new ReadHeapExpression(new VariableExpression("v1"))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new AcquireStatement("cnt"),
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("v1", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), '*')),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                new ReleaseStatement("cnt")
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new AcquireStatement("cnt"),
                                                                        new CompoundStatement(
                                                                                new WriteHeapStatement("v1", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), '*')),
                                                                                new CompoundStatement(
                                                                                        new WriteHeapStatement("v1", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)), '*')),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                new ReleaseStatement("cnt")
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new AcquireStatement("cnt"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), '-')),
                                                                        new ReleaseStatement("cnt")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStatements.add(s22);

        IStatement s23 = new CompoundStatement(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompoundStatement(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v3", new RefType(new IntType())),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("cnt", new IntType()),
                                        new CompoundStatement(
                                                new NewStatement("v1", new ValueExpression(new IntValue(2))),
                                                new CompoundStatement(
                                                        new NewStatement("v2", new ValueExpression(new IntValue(3))),
                                                        new CompoundStatement(
                                                                new NewStatement("v3", new ValueExpression(new IntValue(4))),
                                                                new CompoundStatement(
                                                                        new NewLatchStatement("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                        new CompoundStatement(
                                                                                new ForkStatement(
                                                                                        new CompoundStatement(
                                                                                                new WriteHeapStatement("v1", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), '*')),
                                                                                                new CompoundStatement(
                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                        new CompoundStatement(
                                                                                                                new CountDownStatement("cnt"),
                                                                                                                new ForkStatement(
                                                                                                                        new CompoundStatement(
                                                                                                                                new WriteHeapStatement("v2", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), '*')),
                                                                                                                                new CompoundStatement(
                                                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                        new CompoundStatement(
                                                                                                                                                new CountDownStatement("cnt"),
                                                                                                                                                new ForkStatement(
                                                                                                                                                        new CompoundStatement(
                                                                                                                                                                new WriteHeapStatement("v3", new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v3")), new ValueExpression(new IntValue(10)), '*')),
                                                                                                                                                                new CompoundStatement(
                                                                                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v3"))),
                                                                                                                                                                        new CountDownStatement("cnt")
                                                                                                                                                                )
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                        )
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new AwaitStatement("cnt"),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new ValueExpression(new IntValue(100))),
                                                                                                new CompoundStatement(
                                                                                                        new CountDownStatement("cnt"),
                                                                                                        new PrintStatement(new ValueExpression(new IntValue(100)))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        allStatements.add(s23);


        return FXCollections.observableArrayList(allStatements);
    }
}
