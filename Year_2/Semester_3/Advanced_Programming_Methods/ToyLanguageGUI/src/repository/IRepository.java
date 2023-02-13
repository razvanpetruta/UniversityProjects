package repository;

import exceptions.ADTException;
import model.programState.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    List<ProgramState> getProgramsList();

    void setProgramStates(List<ProgramState> programStates);

    void addProgram(ProgramState programState);

    void logPrgStateExec(ProgramState programState) throws ADTException, IOException;

    List<ProgramState> getProgramList();
}
