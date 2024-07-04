package com.kscottklein.rainmachine.mvc.view.ctrl;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.mvc.ctrl.IZoneController;
import com.kscottklein.util.EnumIcon;
import com.kscottklein.util.IconLoader;

public class StartZoneControl extends JPanel {
	private static final long serialVersionUID = -5178745454731476078L;
	private static final Logger log = LogManager.getLogger();
	private static final int defaultMinutes = 5;
	private static final int defaultSeconds = 30;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Custom Timer Control");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setSize(300, 200);
			frame.setLayout(new BorderLayout());
			frame.add(new StartZoneControl(null, "test", 1), BorderLayout.CENTER);
			frame.setVisible(true);
			frame.pack();
		});
	}

	private final int id;
	private final int mins;
	private JSpinner minutesSpinner;
	private JSpinner secondsSpinner;
	private final int secs;
	private final String zoneName;

	private JButton startButton;
	private IZoneController ctrl;

	public StartZoneControl(IZoneController ctrl, String zoneName, int id) {
		this(ctrl, zoneName, id, StartZoneControl.defaultMinutes, StartZoneControl.defaultSeconds);
	}

	public StartZoneControl(IZoneController ctrl, String zoneName, int id, int mins, int secs) {
		this.ctrl = ctrl;
		this.zoneName = zoneName;
		this.id = id;
		this.mins = mins;
		this.secs = secs;
		this.initializeUI();
	}

	private void initializeUI() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Create components
		JLabel headerLabel = new JLabel(String.format("Start Zone %s [%d]", this.zoneName, this.id),
				SwingConstants.CENTER);
		SpinnerNumberModel minutesModel = new SpinnerNumberModel(this.mins, 0, 59, 1);
		SpinnerNumberModel secondsModel = new SpinnerNumberModel(this.secs, 0, 59, 1);
		this.minutesSpinner = new JSpinner(minutesModel);
		this.secondsSpinner = new JSpinner(secondsModel);
		this.startButton = new JButton("Start", IconLoader.getImageIcon(EnumIcon.START_24));

		// Add components to the panel
		gbc.gridx = 0;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(headerLabel, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(new JLabel("Minutes:"), gbc);
		gbc.gridx = 1;
		this.add(this.minutesSpinner, gbc);

		gbc.gridx = 2;
		this.add(new JLabel("Seconds:"), gbc);
		gbc.gridx = 3;
		this.add(this.secondsSpinner, gbc);

		gbc.gridx = 4;
		this.add(this.startButton, gbc);

		// Add action listener for the start button
		this.startButton.addActionListener(e -> {
			int minutes = (int) StartZoneControl.this.minutesSpinner.getValue();
			int seconds = (int) StartZoneControl.this.secondsSpinner.getValue();
			StartZoneControl.log.debug("Starting zone [{}] ({}) for [{}] minutes [{}] seconds",
					StartZoneControl.this.zoneName, StartZoneControl.this.id, minutes, seconds);
			StartZoneControl.this.ctrl.startZoneTimer(StartZoneControl.this.id, minutes * 60 + seconds);
		});
	}
}
