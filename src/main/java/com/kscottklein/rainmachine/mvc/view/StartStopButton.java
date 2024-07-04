package com.kscottklein.rainmachine.mvc.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;

import com.kscottklein.rainmachine.entity.EnumZoneStatus;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity.Zone;
import com.kscottklein.rainmachine.mvc.ctrl.IZoneController;
import com.kscottklein.rainmachine.mvc.model.IModelAuth;
import com.kscottklein.rainmachine.mvc.view.ctrl.StartZoneControl;
import com.kscottklein.util.EnumIcon;
import com.kscottklein.util.IconLoader;

public class StartStopButton implements ActionListener {
	private JButton button;
	private Zone zone;
	private IZoneController ctrl;

	public StartStopButton(IZoneController ctrl, Zone zone, IModelAuth model) {
		this.ctrl = ctrl;
		this.zone = zone;
		this.button = new JButton(this.getZoneLabel(zone), this.getZoneIcon(zone));
		this.button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.isZoneStopped()) {
			// Open the Start dialog
			JDialog d = new JDialog();
			d.setModal(true);
			d.setTitle("Start Zone");
			d.setLayout(new FlowLayout());
			d.add(new StartZoneControl(this.ctrl, this.zone.getName(), this.zone.getUid()));
			d.setVisible(true);
			d.pack();
		} else {
			this.ctrl.stopZone(this.zone.getUid());
		}
	}

	public JButton getButton() {
		return this.button;
	}

	private Icon getZoneIcon(Zone zone) {
		return this.isZoneStopped() ? IconLoader.getImageIcon(EnumIcon.START_16)
				: IconLoader.getImageIcon(EnumIcon.STOP_16);
	}

	private String getZoneLabel(Zone zone) {
		return this.isZoneStopped() ? "Start" : "Stop";
	}

	public boolean isZoneStopped() {
		return EnumZoneStatus.isStopped(this.zone.getState());
	}

}