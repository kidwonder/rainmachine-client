package com.kscottklein.rainmachine.mvc.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.rainmachine.mvc.ctrl.RainMachineController;
import com.kscottklein.rainmachine.mvc.model.Configuration;
import com.kscottklein.rainmachine.mvc.model.Level;
import com.kscottklein.rainmachine.mvc.model.RainMachineModel;
import com.kscottklein.rainmachine.restclient.RestClientException;
import com.kscottklein.util.EnumIcon;
import com.kscottklein.util.IconLoader;

public class LoginDialog extends JDialog {

	class LoginDialogCancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			succeeded = false;
			setMessage(Level.ERROR, "Unable to login");
		}
	}

	class LoginDialogLoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			configuration = new Configuration(secureCheckBox.isSelected(), ipAddressField.getText(),
					portField.getText(), new String(passwordField.getPassword()), saveCredentialsCheckBox.isSelected());

			if (configuration.areValid()) {
				model.setConfiguration(configuration);
				try {
					response = ctrl.authenticate();

					if (!response.isSuccess()) {
						setMessage(Level.INFO, response.getErrorMessage());
					} else {
						succeeded = true;
						setMessage(Level.INFO, "Success!");
						dispose();
					}
				} catch (RestClientException ex) {
					setMessage(Level.ERROR, "An error occurred during authentication.");
					log.error("Error verifying configuration", ex);
				}
			} else {
				setMessage(Level.ERROR, "Invalid IP address or password.");
			}
		}
	}

	private static final Logger log = LogManager.getLogger();
	private static final long serialVersionUID = 1L;
	private Configuration configuration;
	private RainMachineController ctrl;
	private JTextField ipAddressField;
	private JLabel messageLabel;
	private RainMachineModel model;
	private JPasswordField passwordField;
	private JTextField portField;
	private AuthResponseEntity response;

	private JCheckBox saveCredentialsCheckBox;
	private JCheckBox secureCheckBox;
	private boolean succeeded;

	public LoginDialog(JFrame parent, RainMachineModel model, RainMachineController ctrl) {
		super(parent, "Configure", true);
		this.model = model;
		this.ctrl = ctrl;

		buildPanel();

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);

		loadConfiguration();
	}

	private void buildPanel() {
		this.setIconImage(IconLoader.getImage(EnumIcon.MAIN_24));

		ipAddressField = new JTextField(20);
		passwordField = new JPasswordField(20);
		saveCredentialsCheckBox = new JCheckBox("Save credentials");
		portField = new JTextField(20);
		secureCheckBox = new JCheckBox("Use secure connection");
		messageLabel = new JLabel();

		JButton loginButton = new JButton("Connect", IconLoader.getImageIcon(EnumIcon.LOGIN_16));
		JButton cancelButton = new JButton("Cancel", IconLoader.getImageIcon(EnumIcon.CANCEL_16));

		loginButton.addActionListener(new LoginDialogLoginListener());
		cancelButton.addActionListener(new LoginDialogCancelListener());

		GridBagConstraints gbc = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		final int COL1 = 0;
		final int COL2 = 1;

		int row = 0;

		gbc.gridx = COL1;
		gbc.gridy = row;
		panel.add(new JLabel("IP Address:"), gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(ipAddressField, gbc);

		gbc.gridx = COL1;
		gbc.gridy = ++row;
		panel.add(new JLabel("Port:"), gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(portField, gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(secureCheckBox, gbc);

		gbc.gridx = COL1;
		gbc.gridy = ++row;
		panel.add(new JLabel("Password:"), gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(passwordField, gbc);

		gbc.gridx = COL2;
		gbc.gridy = ++row;
		panel.add(saveCredentialsCheckBox, gbc);

		gbc.gridx = COL2;
		gbc.gridy = ++row;
		panel.add(messageLabel, gbc);

		gbc.gridx = COL1;
		gbc.gridy = row;
		gbc.weightx = 1;
		panel.add(loginButton, gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(cancelButton, gbc);

		getContentPane().add(panel);
	}

	public Configuration getCredentials() {
		return configuration;
	}

	public AuthResponseEntity getResponse() {
		return response;
	}

	public boolean isSucceeded() {
		return succeeded;
	}

	private void loadConfiguration() {
		this.ctrl.loadConfiguration();
		if (this.model.getConfiguration() != null) {
			Configuration savedConfiguration = model.getConfiguration();
			ipAddressField.setText(savedConfiguration.getIpAddress());
			portField.setText(savedConfiguration.getPort());
			secureCheckBox.setSelected(savedConfiguration.isSecure());
			passwordField.setText(savedConfiguration.getPassword());
			saveCredentialsCheckBox.setSelected(true);
		}
	}

	void setMessage(Level level, String msg) {
		switch (level) {
		case ERROR:
			messageLabel.setForeground(java.awt.Color.RED);
			break;
		case INFO:
		default:
			messageLabel.setForeground(java.awt.Color.BLACK);
			break;
		}
		messageLabel.setText(msg);
	}
}