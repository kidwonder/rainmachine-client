package com.kscottklein.util;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EDTTaskRunner {
	private static final Logger log = LogManager.getLogger();

	public static void runOnEDTLater(NamedRunnable task) {
		if (SwingUtilities.isEventDispatchThread()) {
			task.run();
		} else {
			try {
				SwingUtilities.invokeLater(task);
			} catch (Exception e) {
				EDTTaskRunner.log.error("Error executing task: {}", task.getName());
			}
		}
	}

	public static void runOnEDTWait(NamedRunnable task) {
		if (SwingUtilities.isEventDispatchThread()) {
			task.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(task);
			} catch (Exception e) {
				EDTTaskRunner.log.error("Error executing task: {}", task.getName());
			}
		}
	}
}
