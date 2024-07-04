package com.kscottklein.rainmachine.mvc.view.ctrl;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.mvc.ctrl.IZoneController;
import com.kscottklein.rainmachine.mvc.view.StartStopButton;
import com.kscottklein.util.EnumIcon;
import com.kscottklein.util.IconLoader;

public class StartZoneControl extends JPanel {
	private static final long serialVersionUID = -5178745454731476078L;
	private static final Logger log = LogManager.getLogger();
	private static final int defaultMinutes = 5;
	private static final int defaultSeconds = 30;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Custom Timer Control");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(300, 200);
				frame.setLayout(new BorderLayout());
				frame.add(new StartZoneControl(null, null, "test", 1), BorderLayout.CENTER);
				frame.setVisible(true);
				frame.pack();
			}
		});
	}

	private final int id;
	private final int mins;
	private JSpinner minutesSpinner;
	private JSpinner secondsSpinner;
	private final int secs;
	private final String zoneName;

	private JButton startButton;
	private StartStopButton btn;
	private IZoneController ctrl;

	public StartZoneControl(IZoneController ctrl, StartStopButton btn, String zoneName, int id) {
		this(ctrl, btn, zoneName, id, defaultMinutes, defaultSeconds);
	}

	public StartZoneControl(IZoneController ctrl, StartStopButton btn, String zoneName, int id, int mins, int secs) {
		this.ctrl = ctrl;
		this.btn = btn;
		this.zoneName = zoneName;
		this.id = id;
		this.mins = mins;
		this.secs = secs;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Create components
		JLabel headerLabel = new JLabel(String.format("Start Zone %s [%d]", this.zoneName, this.id), JLabel.CENTER);
		SpinnerNumberModel minutesModel = new SpinnerNumberModel(this.mins, 0, 59, 1);
		SpinnerNumberModel secondsModel = new SpinnerNumberModel(this.secs, 0, 59, 1);
		minutesSpinner = new JSpinner(minutesModel);
		secondsSpinner = new JSpinner(secondsModel);
		startButton = new JButton("Start", IconLoader.getImageIcon(EnumIcon.START_24));

		// Add components to the panel
		gbc.gridx = 0;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(headerLabel, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Minutes:"), gbc);
		gbc.gridx = 1;
		add(minutesSpinner, gbc);

		gbc.gridx = 2;
		add(new JLabel("Seconds:"), gbc);
		gbc.gridx = 3;
		add(secondsSpinner, gbc);

		gbc.gridx = 4;
		add(startButton, gbc);

		// Add action listener for the start button
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int minutes = (int) minutesSpinner.getValue();
				int seconds = (int) secondsSpinner.getValue();
				log.debug("Starting zone [{}] ({}) for [{}] minutes [{}] seconds", zoneName, id, minutes, seconds);
				ctrl.startZoneTimer(id, minutes * 60 + seconds);
			}
		});
	}
}
