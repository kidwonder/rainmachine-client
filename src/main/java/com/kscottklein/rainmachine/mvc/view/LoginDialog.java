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
		@Override
		public void actionPerformed(ActionEvent e) {
			LoginDialog.this.succeeded = false;
			LoginDialog.this.setMessage(Level.ERROR, "Unable to login");
		}
	}

	class LoginDialogLoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			LoginDialog.this.configuration = new Configuration(LoginDialog.this.secureCheckBox.isSelected(),
					LoginDialog.this.ipAddressField.getText(), LoginDialog.this.portField.getText(),
					new String(LoginDialog.this.passwordField.getPassword()),
					LoginDialog.this.saveCredentialsCheckBox.isSelected());

			if (LoginDialog.this.configuration.areValid()) {
				LoginDialog.this.model.setConfiguration(LoginDialog.this.configuration);
				try {
					LoginDialog.this.response = LoginDialog.this.ctrl.authenticate();

					if (!LoginDialog.this.response.isSuccess()) {
						LoginDialog.this.setMessage(Level.INFO, LoginDialog.this.response.getErrorMessage());
					} else {
						LoginDialog.this.succeeded = true;
						LoginDialog.this.setMessage(Level.INFO, "Success!");
						LoginDialog.this.dispose();
					}
				} catch (RestClientException ex) {
					LoginDialog.this.setMessage(Level.ERROR, "An error occurred during authentication.");
					LoginDialog.log.error("Error verifying configuration", ex);
				}
			} else {
				LoginDialog.this.setMessage(Level.ERROR, "Invalid IP address or password.");
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

		this.buildPanel();

		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(parent);

		this.loadConfiguration();
	}

	private void buildPanel() {
		this.setIconImage(IconLoader.getImage(EnumIcon.MAIN_24));

		this.ipAddressField = new JTextField(20);
		this.passwordField = new JPasswordField(20);
		this.saveCredentialsCheckBox = new JCheckBox("Save credentials");
		this.portField = new JTextField(20);
		this.secureCheckBox = new JCheckBox("Use secure connection");
		this.messageLabel = new JLabel();

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
		panel.add(this.ipAddressField, gbc);

		gbc.gridx = COL1;
		gbc.gridy = ++row;
		panel.add(new JLabel("Port:"), gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(this.portField, gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(this.secureCheckBox, gbc);

		gbc.gridx = COL1;
		gbc.gridy = ++row;
		panel.add(new JLabel("Password:"), gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		panel.add(this.passwordField, gbc);

		gbc.gridx = COL2;
		gbc.gridy = ++row;
		panel.add(this.saveCredentialsCheckBox, gbc);

		gbc.gridx = COL2;
		gbc.gridy = ++row;
		panel.add(this.messageLabel, gbc);

		gbc.gridx = COL1;
		gbc.gridy = row;
		gbc.weightx = 1;
		panel.add(loginButton, gbc);

		gbc.gridx = COL2;
		gbc.gridy = row;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(cancelButton, gbc);

		this.getContentPane().add(panel);
	}

	public Configuration getCredentials() {
		return this.configuration;
	}

	public AuthResponseEntity getResponse() {
		return this.response;
	}

	public boolean isSucceeded() {
		return this.succeeded;
	}

	private void loadConfiguration() {
		this.ctrl.loadConfiguration();
		if (this.model.getConfiguration() != null) {
			Configuration savedConfiguration = this.model.getConfiguration();
			this.ipAddressField.setText(savedConfiguration.getIpAddress());
			this.portField.setText(savedConfiguration.getPort());
			this.secureCheckBox.setSelected(savedConfiguration.isSecure());
			this.passwordField.setText(savedConfiguration.getPassword());
			this.saveCredentialsCheckBox.setSelected(true);
		}
	}

	void setMessage(Level level, String msg) {
		switch (level) {
		case ERROR:
			this.messageLabel.setForeground(java.awt.Color.RED);
			break;
		case INFO:
		default:
			this.messageLabel.setForeground(java.awt.Color.BLACK);
			break;
		}
		this.messageLabel.setText(msg);
	}
}