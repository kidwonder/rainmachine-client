package com.kscottklein.rainmachine.mvc.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.config.RainMachineConstants;
import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.rainmachine.mvc.ctrl.RainMachineController;
import com.kscottklein.rainmachine.mvc.model.RainMachineModel;
import com.kscottklein.rainmachine.mvc.view.ctrl.StatusPanel;
import com.kscottklein.rainmachine.mvc.view.interfaces.IControllerInterface;
import com.kscottklein.util.EDTTaskRunner;
import com.kscottklein.util.EnumIcon;
import com.kscottklein.util.IconLoader;
import com.kscottklein.util.NamedRunnable;
import com.kscottklein.util.RainMachineException;

public class RainMachine extends JFrame implements IControllerInterface {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> new RainMachine().setVisible(true));
	}

	private JTabbedPane tabPane;

	private RainMachineModel model;

	private RainMachineController ctrl;

	private StatusPanel statusPanel;

	public RainMachine() {
		RainMachine.log.debug("RainMachine v0.0.1");
		this.model = new RainMachineModel();
		this.ctrl = new RainMachineController(this, this.model);

		this.build();
		this.buildMenuBar();
		this.setSize(500, 700);
		this.setIconImage(IconLoader.getImage(EnumIcon.MAIN_24));
		this.doLayout();

		this.promptForConfiguration();
	}

	private void build() {
		this.setLayout(new BorderLayout());

		this.tabPane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		this.add(this.tabPane, BorderLayout.CENTER);

		// TODO: add status pane to the SOUTH
		this.statusPanel = new StatusPanel();
		this.add(this.statusPanel, BorderLayout.SOUTH);

		this.pack();
	}

	public void buildMenuBar() {
		JMenuBar mb = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem fileExitItem = new JMenuItem("Exit");
		fileMenu.add(fileExitItem);
		mb.add(fileMenu);

		JMenu viewMenu = new JMenu("View");
		JMenuItem viewProgramsItem = new JMenuItem("Programs");
		viewProgramsItem.addActionListener(this.buildProgramsListener());
		viewMenu.add(viewProgramsItem);

		JMenuItem viewZonesItem = new JMenuItem(RainMachineConstants.TAB_NAME_ZONES);
		viewZonesItem.addActionListener(this.buildZonesListener());
		viewMenu.add(viewZonesItem);

		mb.add(viewMenu);

		this.setJMenuBar(mb);
	}

	public ActionListener buildProgramsListener() {
		return e -> {
			try {
				RainMachine.this.ctrl.programsSelected();
			} catch (RainMachineException e1) {
				RainMachine.this.showError(e1);
			}
		};
	}

	@Override
	public void buildProgramsTab() throws RainMachineException {
		if (this.hasTabByName(RainMachineConstants.TAB_NAME_PROGRAMS)) {
			this.setSelectedTab(RainMachineConstants.TAB_NAME_PROGRAMS);
		} else {
			ProgramsPanel pp = new ProgramsPanel(this.model, this.ctrl);
			this.tabPane.addTab(RainMachineConstants.TAB_NAME_PROGRAMS, pp);
		}
	}

	public ActionListener buildZonesListener() {
		return e -> {
			try {
				RainMachine.this.ctrl.zonesSelected();
			} catch (RainMachineException e1) {
				RainMachine.this.showError(e1);
			}
		};
	}

	@Override
	public void buildZonesTab() throws RainMachineException {
		if (this.hasTabByName(RainMachineConstants.TAB_NAME_ZONES)) {
			this.setSelectedTab(RainMachineConstants.TAB_NAME_ZONES);
		} else {
			ZonesPanel zp = new ZonesPanel(this.model, this.ctrl);
			this.tabPane.addTab(RainMachineConstants.TAB_NAME_ZONES, zp);
		}
	}

	@Override
	public void configured(AuthResponseEntity config) {
		this.model.setAuthResponse(config);
		this.statusPanel.setConnectionName(this.model.getConfiguration().getIpAddress());
		this.statusPanel.setConnected(true);
		this.statusPanel.setInfoMessage("Connected");
	}

	public Component getTabByName(final String name) throws RainMachineException {
		for (int idx = 0; idx < this.tabPane.getTabCount(); idx++) {
			final String title = this.tabPane.getTitleAt(idx);
			if (title.equals(name)) {
				return this.tabPane.getTabComponentAt(idx);
			}
		}
		throw new RainMachineException("No such tab exists. Use hasTabByName() to check before calling");
	}

	// move to interface
	@Override
	public boolean hasTabByName(final String name) {
		try {
			this.getTabByName(name);
			return true;
		} catch (RainMachineException e) {
			return false;
		}
	}

	public void promptForConfiguration() {
		LoginDialog loginDialog = new LoginDialog(this, this.model, this.ctrl);
		loginDialog.setVisible(true);
	}

	@Override
	public void setConnected(boolean isConnected) {
		this.statusPanel.setConnected(isConnected);
	}

	@Override
	public void setConnectionName(String name) {
		this.statusPanel.setConnectionName(name);
	}

	@Override
	public void setErrorMessage(String msg) {
		this.statusPanel.setErrorMessage(msg);
	}

	@Override
	public void setInfoMessage(String msg) {
		NamedRunnable task = new NamedRunnable("Status Message [INFO] " + msg) {

			@Override
			public void run() {
				RainMachine.this.statusPanel.setInfoMessage(msg);
			}

		};
		EDTTaskRunner.runOnEDTWait(task);
	}

	@Override
	public void setSelectedTab(final String name) throws RainMachineException {
		try {
			this.tabPane.setSelectedComponent(this.getTabByName(name));
		} catch (RainMachineException e) {
			throw new RainMachineException("No such tab exists. Use hasTabByName() to check before calling");
		}
	}

	protected void showError(RainMachineException e1) {
		// TODO add to status messaging
	}
}