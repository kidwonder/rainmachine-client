package com.kscottklein.rainmachine.mvc.view;

import static org.mockito.Mockito.mock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.entity.EnumZoneStatus;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity.Zone;
import com.kscottklein.rainmachine.listeners.ZonesListener;
import com.kscottklein.rainmachine.mvc.ctrl.IZoneController;
import com.kscottklein.rainmachine.mvc.ctrl.RainMachineController;
import com.kscottklein.rainmachine.mvc.model.RainMachineModel;
import com.kscottklein.rainmachine.mvc.view.interfaces.IControllerInterface;
import com.kscottklein.rainmachine.restclient.RainMachineClient;
import com.kscottklein.rainmachine.restclient.RestClientException;
import com.kscottklein.util.IconLoader;
import com.kscottklein.util.RainMachineException;

public class ZonesPanel extends JPanel implements ZonesListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main application frame
            JFrame frame = new JFrame("Test ZonesPanel");
            
            // Set the default close operation
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            // Create an instance of ZonesPanel and add it to the frame
            IControllerInterface app = mock(IControllerInterface.class);
            RainMachineModel model = new RainMachineModel();
            RainMachineController ctrl = new RainMachineController(app, model);
            ctrl.loadConfiguration();
            RainMachineClient.createInstance(model.getConfiguration());
            try {
				ctrl.authenticate();
			} catch (RestClientException e) {
				return;
			}

            ZonesPanel zonesPanel = new ZonesPanel(model, ctrl);
            frame.add(zonesPanel);
            
            // Set the frame size
            frame.setSize(800, 600);
            
            // Center the frame on the screen
            frame.setLocationRelativeTo(null);
            
            // Make the frame visible
            frame.setVisible(true);
        });
    }

	private JButton refreshButton;
	private ZonesResponseEntity zonesResponse;
	private RainMachineModel model;
	private IZoneController ctrl;
	private JPanel zoneDisplayPanel;
	private JScrollPane scrollPane;

	public ZonesPanel(RainMachineModel model, IZoneController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		buildPanel();
		this.buildListeners();
		this.ctrl.addZonesListener(this);
		this.refreshData();
	}

	private void buildListeners() {
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshData();
			}
		});
	}

	private void buildPanel() {
		setLayout(new BorderLayout());

		// TOP: refresh button
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		refreshButton = new JButton("Refresh");
		topPanel.add(refreshButton, BorderLayout.EAST);
		this.add(topPanel, BorderLayout.NORTH);

		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(scrollPane, BorderLayout.CENTER);
		
		zoneDisplayPanel = new JPanel(new GridBagLayout());
		zoneDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		scrollPane.setViewportView(zoneDisplayPanel);

		this.doLayout();
	}

	private void displayZone() {
		if (zonesResponse == null || zonesResponse.getZones() == null) {
			log.debug("No data to display");
			return;
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // padding
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		int row = -1;
		for (Zone zone : zonesResponse.getZones()) {
			// First Row: Zone Name, Zone UID, Status, Active
			JLabel nameLabel = new JLabel("Name");
			gbc.gridy = ++row;
			gbc.gridx = 0;
			gbc.gridwidth = 1; // Reset grid width
			gbc.fill = GridBagConstraints.HORIZONTAL; // Reset fill
			zoneDisplayPanel.add(nameLabel, gbc);

			JLabel nameValue = new JLabel(zone.getName());
			gbc.gridx = 1;
			zoneDisplayPanel.add(nameValue, gbc);

			gbc.gridx = 2;
			JLabel uidLabel = new JLabel("UID");
			zoneDisplayPanel.add(uidLabel, gbc);

			JLabel uidValue = new JLabel(String.valueOf(zone.getUid()));
			gbc.gridx = 3;
			zoneDisplayPanel.add(uidValue, gbc);

			// Third Row: Zone State
			gbc.gridx = 4;
			JLabel stateLabel = new JLabel("State");
			zoneDisplayPanel.add(stateLabel, gbc);

			EnumZoneStatus status = EnumZoneStatus.get(zone.getState());
			JLabel stateValue = new JLabel(status.getName(), IconLoader.getImageIcon(status.getIcon()), JLabel.HORIZONTAL);
			gbc.gridx = 5;
			zoneDisplayPanel.add(stateValue, gbc);

			gbc.gridx = 6;
			JCheckBox activeButton = new JCheckBox("Active", zone.isActive());
			zoneDisplayPanel.add(activeButton, gbc);


			gbc.gridy = ++row;
			gbc.gridx = 0;
			gbc.gridwidth = 8;			
			StartStopButton startStopButton = new StartStopButton(this.ctrl, zone, model);
			zoneDisplayPanel.add(startStopButton.getButton(), gbc);
			
			// Separator between zones
			JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
			sep.setForeground(Color.LIGHT_GRAY);
			gbc.gridy = ++row;
			gbc.gridx = 0;
			gbc.gridwidth = 8;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			zoneDisplayPanel.add(sep, gbc);
		}
		

	    // Update preferred size of the zoneDisplayPanel based on its content
	    zoneDisplayPanel.revalidate();
	    zoneDisplayPanel.repaint();
	    scrollPane.revalidate();
	    scrollPane.repaint();
	}

	private void refreshData() {
		log.debug("Refresh button clicked!");

		try {
			this.ctrl.getZones();
		} catch (RainMachineException e) {
			log.error("Error refreshing Zone data", e);
		}
	}

	void setData(ZonesResponseEntity zonesResponse) {
		if (zonesResponse.getZones() == null || zonesResponse.getZones().isEmpty()) {
			log.debug("No programs to display");
			return;
		}
		this.zonesResponse = zonesResponse;

		displayZone();
	}

	@Override
	public void zonesUpdated() {
		this.setData(this.model.getZones());
	}
}