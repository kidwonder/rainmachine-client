package com.kscottklein.rainmachine.mvc.view.ctrl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.mvc.view.IStatusPanel;
import com.kscottklein.util.EDTTaskRunner;
import com.kscottklein.util.EnumIcon;
import com.kscottklein.util.IconLoader;
import com.kscottklein.util.NamedRunnable;

public class StatusPanel extends JPanel implements IStatusPanel {
	private static final Integer PAUSE_MESSAGE_MS = 5000;
	private static final Integer PAUSE_ERROR_MS = 15000;

	private static final Logger log = LogManager.getLogger();

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Status Panel Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 100);

		StatusPanel statusPanel = new StatusPanel();
		frame.add(statusPanel, BorderLayout.SOUTH);

		frame.setVisible(true);

		statusPanel.activeLoginIcon.setIcon(IconLoader.getImageIcon(EnumIcon.CONNECTED_16));
		statusPanel.machineNameLabel.setText("127.0.0.1");
		statusPanel.statusMessageArea.setText("Message");
	}

	private JLabel activeLoginIcon;
	private JLabel machineNameLabel;

	private JTextField statusMessageArea;
	private Timer clearTimer;

	public StatusPanel() {
		setLayout(new GridBagLayout());
		buildPanel();
		this.doLayout();
	}

	private void buildPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.ipadx = 10;
		gbc.ipady = 5;
		gbc.weightx = 0;

		activeLoginIcon = new JLabel();
		activeLoginIcon.setPreferredSize(new Dimension(20, 20));
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		add(activeLoginIcon, gbc);

		// Machine name section
		machineNameLabel = new JLabel();
		machineNameLabel.setPreferredSize(new Dimension(125, 20));
		gbc.gridx = 1;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(machineNameLabel, gbc);

		// Status message section
		statusMessageArea = new JTextField();
		statusMessageArea.setEditable(false);
		statusMessageArea.setBackground(Color.LIGHT_GRAY);
		// Create a thin border
		Border thinBorder = new LineBorder(Color.BLACK, 1);
		// Set the border of the JTextField
		statusMessageArea.setBorder(thinBorder);

		gbc.gridx = 6;
		gbc.weightx = 1; // Remaining space for the text field
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(statusMessageArea, gbc);
	}

	public void setActiveLoginIcon(ImageIcon icon) {
		log.debug("STATUS: Setting icon");
		this.setIcon(activeLoginIcon, icon);
	}

	@Override
	public void setConnected(boolean isConnected) {
		EnumIcon iconToShow = isConnected ? EnumIcon.CONNECTED_16 : EnumIcon.DISCONNECTED_16;
		ImageIcon icon = IconLoader.getImageIcon(iconToShow);
		this.setIcon(activeLoginIcon, icon);
	}

	@Override
	public void setConnectionName(String name) {
		log.debug("STATUS: Machine {}", name);
		NamedRunnable task = new NamedRunnable("Set Connection Name: " + name) {

			@Override
			public void run() {
				machineNameLabel.setText(name);
			}

		};
		EDTTaskRunner.runOnEDTWait(task);
	}

	@Override
	public void setErrorMessage(String msg) {
		log.debug("STATUS: Error {}", msg);
		this.setMessage(Color.magenta, msg, PAUSE_ERROR_MS);
	}

	private void setIcon(JLabel label, ImageIcon icon) {
		NamedRunnable task = new NamedRunnable("Set Icon") {

			@Override
			public void run() {
				label.setIcon(icon);
			}

		};
		EDTTaskRunner.runOnEDTWait(task);
	}

	@Override
	public void setInfoMessage(String msg) {
		log.debug("STATUS: Info {}", msg);
		this.setMessage(Color.blue, msg, PAUSE_MESSAGE_MS);
	}

	private void setMessage(final Color color, final String msg, final Integer disposeMs) {
		NamedRunnable task1 = new NamedRunnable("Set Status Message: " + msg) {

			@Override
			public void run() {
				statusMessageArea.setForeground(color);
				statusMessageArea.setText(msg);
			}

		};
		EDTTaskRunner.runOnEDTWait(task1);

		if (clearTimer != null && clearTimer.isRunning()) {
			clearTimer.stop();
		}

		if (disposeMs == null) {
			return;
		}

		// Create a new Timer to clear the text after 15 seconds
		clearTimer = new Timer((int) disposeMs, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMessage(Color.black, "", null);
			}
		});

		// Start the timer
		clearTimer.setRepeats(false); // Ensure the timer only runs once
		clearTimer.start();
	}

	public void setStatusMessage(String message) {
		this.setMessage(Color.black, message, PAUSE_MESSAGE_MS);
		log.debug("STATUS: Message {}", message);
	}
}
