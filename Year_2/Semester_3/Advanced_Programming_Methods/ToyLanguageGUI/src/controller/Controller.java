package controller;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final IRepository repository;
    private boolean displayFlag = false;
    private ExecutorService executorService;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public boolean isDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    public List<Integer> getAddrFromSymbolTable(Collection<Value> symbolTableValues) {
        return symbolTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    public List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symbolTableAddr, List<Integer> heapAddr, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> (symbolTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void conservativeGarbageCollector(List<ProgramState> programList) {
        List<Integer> symbolTableAddresses = programList.stream()
                        .map(p -> this.getAddrFromSymbolTable(p.getSymbolTable().values()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null)
                        .collect(Collectors.toList());


        ProgramState firstProgram = programList.get(0);
        firstProgram.getHeap().setContent((HashMap<Integer, Value>) safeGarbageCollector(symbolTableAddresses, getAddrFromHeap(firstProgram.getHeap().getContent().values()), firstProgram.getHeap().getContent()));
    }

    public void oneStepForAllPrograms(List<ProgramState> programsList) throws InterruptedException {
        // for each program, display the current state before execution
        programsList.forEach(programState -> {
            try {
                this.repository.logPrgStateExec(programState);
                if (this.displayFlag) {
                    System.out.println("BEFORE EXECUTION:");
                }
                this.display(programState);
            } catch (ADTException | IOException e) {
                System.out.println(e.getMessage());
            }
        });

        // run concurrently oneStep for each program

        // prepare the list of callables
        List<Callable<ProgramState>> callList = programsList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        // start the execution of the callables
        // returns the new created programs (threads)
        List<ProgramState> newProgramsList = this.executorService.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // add the new created threads
        programsList.addAll(newProgramsList);

        // print the program list to the file, after execution
        programsList.forEach(programState -> {
            try {
                this.repository.logPrgStateExec(programState);
                if (this.displayFlag) {
                    System.out.println("AFTER EXECUTION:");
                }
                this.display(programState);
            } catch (ADTException | IOException e) {
                System.out.println(e.getMessage());
            }
        });

        this.repository.setProgramStates(programsList);
    }

    public void allSteps() throws StatementExecutionException, ADTException, ExpressionEvaluationException, IOException, InterruptedException {
        this.executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programsList = this.removeCompletedPrograms(this.repository.getProgramsList());
        while (programsList.size() > 0) {
            this.conservativeGarbageCollector(programsList);
            this.oneStepForAllPrograms(programsList);
            programsList = this.removeCompletedPrograms(this.repository.getProgramsList());
        }
        this.executorService.shutdown();
        this.repository.setProgramStates(programsList);
    }

    private void display(ProgramState programState) throws ADTException {
        if (this.displayFlag) {
            System.out.println(programState.programStateToString());
        }
    }

    public List<ProgramState> removeCompletedPrograms(List<ProgramState> inProgramsList) {
        return inProgramsList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public List<ProgramState> getProgramStates() {
        return this.repository.getProgramList();
    }

    public void setProgramStates(List<ProgramState> programStates) {
        this.repository.setProgramStates(programStates);
    }

    public void oneStep() throws InterruptedException {
        this.executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = this.removeCompletedPrograms(this.repository.getProgramList());
        this.oneStepForAllPrograms(programStates);
        this.conservativeGarbageCollector(programStates);
        this.executorService.shutdownNow();
    }
}
