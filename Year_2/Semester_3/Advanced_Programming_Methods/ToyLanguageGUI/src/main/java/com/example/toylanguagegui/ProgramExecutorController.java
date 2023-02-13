package com.example.toylanguagegui;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.programState.ProgramState;
import model.value.Value;
import model.statement.IStatement;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}

public class ProgramExecutorController {
    private Controller controller;
    @FXML
    private TextField numberOfProgramStatesTextField;

    @FXML
    private TableView<Pair<Integer, Value>> heapTableView;

    @FXML
    private TableColumn<Pair<Integer, Value>, Integer> addressColumn;

    @FXML
    private TableColumn<Pair<Integer, Value>, String> valueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> programStateIdentifiersListView;

    @FXML
    private TableView<Pair<String, Value>> symbolTableView;

    @FXML
    private TableColumn<Pair<String, Value>, String> variableNameColumn;

    @FXML
    private TableColumn<Pair<String, Value>, String> variableValueColumn;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<Pair<Integer, Integer>> lockTableView;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> locationColumn;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> locationValueColumn;

    @FXML
    private TableView<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>> semaphoreTableView;

    @FXML
    private TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, Integer> indexSemaphoreTableColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, Integer> valueSemaphoreTableColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, List<Integer>> listSemaphoreTableColumn;

    @FXML
    private TableView<Pair<Integer, Integer>> latchTableView;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> locationLatchColumn;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> locationValueLatchColumn;

    public void setController(Controller controller) {
        this.controller = controller;
        this.populate();
    }

    @FXML
    public void initialize() {
        this.programStateIdentifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        this.valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        this.variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        this.variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        this.locationColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first.toString()));
        this.locationValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        this.indexSemaphoreTableColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        this.valueSemaphoreTableColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue().getKey()).asObject());
        this.listSemaphoreTableColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue().getValue()));
        this.locationLatchColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first.toString()));
        this.locationValueLatchColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
    }

    private ProgramState getCurrentProgramState() {
        if (this.controller.getProgramStates().size() == 0) {
            return null;
        }
        else {
            int currentId = this.programStateIdentifiersListView.getSelectionModel().getSelectedIndex();
            if (currentId == -1) {
                return this.controller.getProgramStates().get(0);
            }
            else {
                return this.controller.getProgramStates().get(currentId);
            }
        }
    }

    private void populate() {
        this.populateHeapTableView();
        this.populateOutputListView();
        this.populateFileTableListView();
        this.populateProgramStateIdentifiersListView();
        this.populateSymbolTableView();
        this.populateExecutionStackListView();
        this.populateLockTableView();
        this.populateSemaphoreTableView();
        this.populateLatchTableView();
    }

    @FXML
    private void changeProgramState(MouseEvent event) {
        this.populateExecutionStackListView();
        this.populateSymbolTableView();
    }

    private void populateNumberOfProgramStatesTextField() {
        List<ProgramState> programStates = this.controller.getProgramStates();
        this.numberOfProgramStatesTextField.setText(String.valueOf(programStates.size()));
    }

    private void populateHeapTableView() {
        ProgramState programState = this.getCurrentProgramState();
        MyIHeap heap = Objects.requireNonNull(programState).getHeap();
        ArrayList<Pair<Integer, Value>> heapEntries = new ArrayList<>();
        for(Map.Entry<Integer, Value> entry: heap.getContent().entrySet()) {
            heapEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        this.heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView() {
        ProgramState programState = this.getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<Value> outputList = Objects.requireNonNull(programState).getOut().getList();
        for (int index = 0; index < outputList.size(); index++){
            output.add(outputList.get(index).toString());
        }
        this.outputListView.setItems(FXCollections.observableArrayList(output));
    }

    private void populateFileTableListView() {
        ProgramState programState = getCurrentProgramState();
        List<String> files = new ArrayList<>(Objects.requireNonNull(programState).getFileTable().getContent().keySet());
        this.fileTableListView.setItems(FXCollections.observableList(files));
    }

    private void populateProgramStateIdentifiersListView() {
        List<ProgramState> programStates = controller.getProgramStates();
        List<Integer> idList = programStates.stream().map(ProgramState::getId).collect(Collectors.toList());
        this.programStateIdentifiersListView.setItems(FXCollections.observableList(idList));
        this.populateNumberOfProgramStatesTextField();
    }

    private void populateSymbolTableView() {
        ProgramState programState = getCurrentProgramState();
        MyIDictionary<String, Value> symbolTable = Objects.requireNonNull(programState).getSymbolTable();
        ArrayList<Pair<String, Value>> symbolTableEntries = new ArrayList<>();
        for (Map.Entry<String, Value> entry: symbolTable.getContent().entrySet()) {
            symbolTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        this.symbolTableView.setItems(FXCollections.observableArrayList(symbolTableEntries));
    }

    private void populateExecutionStackListView() {
        ProgramState programState = getCurrentProgramState();
        List<String> executionStackToString = new ArrayList<>();
        if (programState != null)
            for (IStatement statement: programState.getExecutionStack().getReversed()) {
                executionStackToString.add(statement.toString());
            }
        this.executionStackListView.setItems(FXCollections.observableList(executionStackToString));
    }

    private void populateLockTableView() {
        ProgramState programState = getCurrentProgramState();
        MyILockTable lockTable = Objects.requireNonNull(programState).getLockTable();
        ArrayList<Pair<Integer, Integer>> lockTableEntries = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: lockTable.getContent().entrySet()) {
            lockTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        lockTableView.setItems(FXCollections.observableArrayList(lockTableEntries));
    }

    private void populateSemaphoreTableView() {
        ProgramState programState = getCurrentProgramState();
        MyISemaphoreTable semaphoreTable = Objects.requireNonNull(programState).getSemaphoreTable();
        List<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>> semaphoreList = new ArrayList<>();
        for (Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>> entry: semaphoreTable.getSemaphoreTable().entrySet()) {
            semaphoreList.add(entry);
        }
        semaphoreTableView.setItems(FXCollections.observableArrayList(semaphoreList));
        semaphoreTableView.refresh();
    }

    private void populateLatchTableView() {
        ProgramState programState = getCurrentProgramState();
        MyILatchTable latchTable = Objects.requireNonNull(programState).getLatchTable();
        ArrayList<Pair<Integer, Integer>> lockTableEntries = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: latchTable.getLatchTable().entrySet()) {
            lockTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        latchTableView.setItems(FXCollections.observableArrayList(lockTableEntries));
    }

    @FXML
    private void runOneStep(MouseEvent mouseEvent) {
        if (this.controller != null) {
            try {
                List<ProgramState> programStates = Objects.requireNonNull(this.controller.getProgramStates());
                if (programStates.size() > 0) {
                    this.controller.oneStep();
                    this.populate();
                    programStates = this.controller.removeCompletedPrograms(this.controller.getProgramStates());
                    this.controller.setProgramStates(programStates);
                    this.populateProgramStateIdentifiersListView();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("An error has occurred!");
                    alert.setContentText("There is nothing left to be executed!");
                    alert.showAndWait();
                }
            } catch (InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Execution error!");
                alert.setHeaderText("An execution error has occurred!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("An error has occurred!");
            alert.setContentText("No program selected!");
            alert.showAndWait();
        }
    }
}
