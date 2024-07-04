package com.kscottklein.rainmachine.mvc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.entity.EnumZoneStatus;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity.Zone;
import com.kscottklein.rainmachine.listeners.ZonesListener;
import com.kscottklein.rainmachine.mvc.ctrl.IZoneController;
import com.kscottklein.rainmachine.mvc.model.RainMachineModel;
import com.kscottklein.util.IconLoader;
import com.kscottklein.util.RainMachineException;

public class ZonesPanel extends JPanel implements ZonesListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger();

	private JButton refreshButton;
	private ZonesResponseEntity zonesResponse;
	private RainMachineModel model;
	private IZoneController ctrl;
	private JPanel zoneDisplayPanel;
	private JScrollPane scrollPane;

	public ZonesPanel(RainMachineModel model, IZoneController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		this.buildPanel();
		this.buildListeners();
		this.ctrl.addZonesListener(this);
		this.refreshData();
	}

	private void buildListeners() {
		this.refreshButton.addActionListener(e -> ZonesPanel.this.refreshData());
	}

	private void buildPanel() {
		this.setLayout(new BorderLayout());

		// TOP: refresh button
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.refreshButton = new JButton("Refresh");
		topPanel.add(this.refreshButton, BorderLayout.EAST);
		this.add(topPanel, BorderLayout.NORTH);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(this.scrollPane, BorderLayout.CENTER);

		this.zoneDisplayPanel = new JPanel(new GridBagLayout());
		this.zoneDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.scrollPane.setViewportView(this.zoneDisplayPanel);

		this.doLayout();
	}

	private void displayZone() {
		if (this.zonesResponse == null || this.zonesResponse.getZones() == null) {
			ZonesPanel.log.debug("No data to display");
			return;
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // padding
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		int row = -1;
		for (Zone zone : this.zonesResponse.getZones()) {
			// First Row: Zone Name, Zone UID, Status, Active
			JLabel nameLabel = new JLabel("Name");
			gbc.gridy = ++row;
			gbc.gridx = 0;
			gbc.gridwidth = 1; // Reset grid width
			gbc.fill = GridBagConstraints.HORIZONTAL; // Reset fill
			this.zoneDisplayPanel.add(nameLabel, gbc);

			JLabel nameValue = new JLabel(zone.getName());
			gbc.gridx = 1;
			this.zoneDisplayPanel.add(nameValue, gbc);

			gbc.gridx = 2;
			JLabel uidLabel = new JLabel("UID");
			this.zoneDisplayPanel.add(uidLabel, gbc);

			JLabel uidValue = new JLabel(String.valueOf(zone.getUid()));
			gbc.gridx = 3;
			this.zoneDisplayPanel.add(uidValue, gbc);

			// Third Row: Zone State
			gbc.gridx = 4;
			JLabel stateLabel = new JLabel("State");
			this.zoneDisplayPanel.add(stateLabel, gbc);

			EnumZoneStatus status = EnumZoneStatus.get(zone.getState());
			JLabel stateValue = new JLabel(status.getName(), IconLoader.getImageIcon(status.getIcon()),
					SwingConstants.HORIZONTAL);
			gbc.gridx = 5;
			this.zoneDisplayPanel.add(stateValue, gbc);

			gbc.gridx = 6;
			JCheckBox activeButton = new JCheckBox("Active", zone.isActive());
			this.zoneDisplayPanel.add(activeButton, gbc);

			gbc.gridy = ++row;
			gbc.gridx = 0;
			gbc.gridwidth = 8;
			StartStopButton startStopButton = new StartStopButton(this.ctrl, zone, this.model);
			this.zoneDisplayPanel.add(startStopButton.getButton(), gbc);

			// Separator between zones
			JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
			sep.setForeground(Color.LIGHT_GRAY);
			gbc.gridy = ++row;
			gbc.gridx = 0;
			gbc.gridwidth = 8;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			this.zoneDisplayPanel.add(sep, gbc);
		}

		// Update preferred size of the zoneDisplayPanel based on its content
		this.zoneDisplayPanel.revalidate();
		this.zoneDisplayPanel.repaint();
		this.scrollPane.revalidate();
		this.scrollPane.repaint();
	}

	private void refreshData() {
		ZonesPanel.log.debug("Refresh button clicked!");

		try {
			this.ctrl.getZones();
		} catch (RainMachineException e) {
			ZonesPanel.log.error("Error refreshing Zone data", e);
		}
	}

	void setData(ZonesResponseEntity zonesResponse) {
		if (zonesResponse.getZones() == null || zonesResponse.getZones().isEmpty()) {
			ZonesPanel.log.debug("No programs to display");
			return;
		}
		this.zonesResponse = zonesResponse;

		this.displayZone();
	}

	@Override
	public void zonesUpdated() {
		this.setData(this.model.getZones());
	}
}