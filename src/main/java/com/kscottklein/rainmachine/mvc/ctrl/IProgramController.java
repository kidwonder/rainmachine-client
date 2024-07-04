package com.kscottklein.rainmachine.mvc.ctrl;

import com.kscottklein.rainmachine.listeners.ProgramsListener;
import com.kscottklein.util.RainMachineException;

public interface IProgramController {

	void addProgramsListener(ProgramsListener listener);

	void getPrograms() throws RainMachineException;

	void programsSelected() throws RainMachineException;

}