package com.kscottklein.rainmachine.mvc.view.ctrl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
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
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		this.setLayout(new GridBagLayout());
		this.buildPanel();
		this.doLayout();
	}

	private void buildPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.ipadx = 10;
		gbc.ipady = 5;
		gbc.weightx = 0;

		this.activeLoginIcon = new JLabel();
		this.activeLoginIcon.setPreferredSize(new Dimension(20, 20));
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(this.activeLoginIcon, gbc);

		// Machine name section
		this.machineNameLabel = new JLabel();
		this.machineNameLabel.setPreferredSize(new Dimension(125, 20));
		gbc.gridx = 1;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.machineNameLabel, gbc);

		// Status message section
		this.statusMessageArea = new JTextField();
		this.statusMessageArea.setEditable(false);
		this.statusMessageArea.setBackground(Color.LIGHT_GRAY);
		// Create a thin border
		Border thinBorder = new LineBorder(Color.BLACK, 1);
		// Set the border of the JTextField
		this.statusMessageArea.setBorder(thinBorder);

		gbc.gridx = 6;
		gbc.weightx = 1; // Remaining space for the text field
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.statusMessageArea, gbc);
	}

	public void setActiveLoginIcon(ImageIcon icon) {
		StatusPanel.log.debug("STATUS: Setting icon");
		this.setIcon(this.activeLoginIcon, icon);
	}

	@Override
	public void setConnected(boolean isConnected) {
		EnumIcon iconToShow = isConnected ? EnumIcon.CONNECTED_16 : EnumIcon.DISCONNECTED_16;
		ImageIcon icon = IconLoader.getImageIcon(iconToShow);
		this.setIcon(this.activeLoginIcon, icon);
	}

	@Override
	public void setConnectionName(String name) {
		StatusPanel.log.debug("STATUS: Machine {}", name);
		NamedRunnable task = new NamedRunnable("Set Connection Name: " + name) {

			@Override
			public void run() {
				StatusPanel.this.machineNameLabel.setText(name);
			}

		};
		EDTTaskRunner.runOnEDTWait(task);
	}

	@Override
	public void setErrorMessage(String msg) {
		StatusPanel.log.debug("STATUS: Error {}", msg);
		this.setMessage(Color.magenta, msg, StatusPanel.PAUSE_ERROR_MS);
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
		StatusPanel.log.debug("STATUS: Info {}", msg);
		this.setMessage(Color.blue, msg, StatusPanel.PAUSE_MESSAGE_MS);
	}

	private void setMessage(final Color color, final String msg, final Integer disposeMs) {
		NamedRunnable task1 = new NamedRunnable("Set Status Message: " + msg) {

			@Override
			public void run() {
				StatusPanel.this.statusMessageArea.setForeground(color);
				StatusPanel.this.statusMessageArea.setText(msg);
			}

		};
		EDTTaskRunner.runOnEDTWait(task1);

		if (this.clearTimer != null && this.clearTimer.isRunning()) {
			this.clearTimer.stop();
		}

		if (disposeMs == null) {
			return;
		}

		// Create a new Timer to clear the text after 15 seconds
		this.clearTimer = new Timer(disposeMs, e -> StatusPanel.this.setMessage(Color.black, "", null));

		// Start the timer
		this.clearTimer.setRepeats(false); // Ensure the timer only runs once
		this.clearTimer.start();
	}

	public void setStatusMessage(String message) {
		this.setMessage(Color.black, message, StatusPanel.PAUSE_MESSAGE_MS);
		StatusPanel.log.debug("STATUS: Message {}", message);
	}
}
