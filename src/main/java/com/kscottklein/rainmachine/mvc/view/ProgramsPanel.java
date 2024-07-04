package com.kscottklein.rainmachine.mvc.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.entity.ProgramResponseEntity;
import com.kscottklein.rainmachine.listeners.ProgramsListener;
import com.kscottklein.rainmachine.mvc.ctrl.IProgramController;
import com.kscottklein.rainmachine.mvc.model.RainMachineModel;
import com.kscottklein.util.RainMachineException;

public class ProgramsPanel extends JPanel implements ProgramsListener {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger();
	private ProgramResponseEntity programsResponse;
	private JPanel programDetailPanel;
	private JComboBox<String> programComboBox;
	private RainMachineModel model;
	private IProgramController ctrl;

	public ProgramsPanel(RainMachineModel model, IProgramController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		this.buildPanel();
		this.ctrl.addProgramsListener(this);
	}

	private void buildPanel() {
		this.setLayout(new BorderLayout());

		// Create and add the refresh button to the top right corner
		JPanel topPanel = new JPanel(new BorderLayout());
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> ProgramsPanel.this.refreshData());
		topPanel.add(refreshButton, BorderLayout.EAST);

		// Create the combo box for selecting programs
		this.programComboBox = new JComboBox<>();
		this.programComboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				ProgramsPanel.this.displayProgram(ProgramsPanel.this.programComboBox.getSelectedIndex());
			}
		});
		topPanel.add(this.programComboBox, BorderLayout.WEST);

		this.add(topPanel, BorderLayout.NORTH);

		// Create a panel to display the selected program data
		this.programDetailPanel = new JPanel();
		this.programDetailPanel.setLayout(new BoxLayout(this.programDetailPanel, BoxLayout.Y_AXIS));
		this.add(new JScrollPane(this.programDetailPanel), BorderLayout.CENTER);
	}

	private void displayProgram(int index) {
		if (index < 0) {
			ProgramsPanel.log.debug("No data to display");
			return;
		}
		this.programDetailPanel.removeAll(); // Clear existing data

		ProgramResponseEntity.Program program = this.programsResponse.getPrograms().get(index);

		JPanel programPanel = new JPanel();
		programPanel.setLayout(new GridLayout(0, 1));
		programPanel.setBorder(BorderFactory.createTitledBorder(program.getName()));

		programPanel.add(new JLabel("UID: " + program.getUid()));
		programPanel.add(new JLabel("Name: " + program.getName()));
		programPanel.add(new JLabel("Active: " + program.isActive()));
		programPanel.add(new JLabel("Start Time: " + program.getStartTime()));
		programPanel.add(new JLabel("Cycles: " + program.getCycles()));
		programPanel.add(new JLabel("Soak: " + program.getSoak()));
		programPanel.add(new JLabel("CS On: " + program.isCs_on()));
		programPanel.add(new JLabel("Delay: " + program.getDelay()));
		programPanel.add(new JLabel("Delay On: " + program.isDelay_on()));
		programPanel.add(new JLabel("Status: " + program.getStatus()));
		programPanel.add(new JLabel("Next Run: " + program.getNextRun()));
		programPanel.add(new JLabel("Start Date: " + program.getStartDate()));
		programPanel.add(new JLabel("End Date: " + program.getEndDate()));
		programPanel.add(new JLabel("Yearly Recurring: " + program.isYearlyRecurring()));
		programPanel.add(new JLabel("Simulation Expired: " + program.isSimulationExpired()));

		// Display watering times
		for (ProgramResponseEntity.Program.WateringTime wt : program.getWateringTimes()) {
			programPanel.add(new JLabel("  - Watering Time: " + wt.getName() + " (Active: " + wt.isActive() + ")"));
		}

		this.programDetailPanel.add(programPanel);

		this.programDetailPanel.revalidate();
		this.programDetailPanel.repaint();
	}

	@Override
	public void programsUpdated() {
		this.setData(this.model.getPrograms());
	}

	private void refreshData() {
		// Placeholder for refreshing data logic
		// You would typically re-fetch data from the source and update the
		// programsResponse object
		ProgramsPanel.log.debug("Refresh button clicked!");

		// For now, just re-display the existing data
		try {
			this.ctrl.getPrograms();
		} catch (RainMachineException e) {
			ProgramsPanel.log.error("Error refreshing Program data", e);
		}
	}

	void setData(ProgramResponseEntity programsResponse) {
		if (programsResponse.getPrograms() == null || programsResponse.getPrograms().isEmpty()) {
			ProgramsPanel.log.debug("No programs to display");
			return;
		}
		this.programsResponse = programsResponse;

		for (ProgramResponseEntity.Program program : programsResponse.getPrograms()) {
			this.programComboBox.addItem(program.getName());
		}

		// Display the first program initially
		if (programsResponse != null && programsResponse.getPrograms().size() > 0) {
			this.displayProgram(0);
		}
	}
}