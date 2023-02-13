package view;

import controller.Controller;
import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class RunExampleCommand extends Command {
    private final Controller controller;

    public RunExampleCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Do you want to display the steps? [y / n]");
            Scanner readOption = new Scanner(System.in);
            String option = readOption.next();
            this.controller.setDisplayFlag(Objects.equals(option, "y"));
            this.controller.allSteps();
        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException | IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
