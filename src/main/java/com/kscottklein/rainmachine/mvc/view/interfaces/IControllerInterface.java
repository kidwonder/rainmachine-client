package com.kscottklein.rainmachine.mvc.view.interfaces;

import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.util.RainMachineException;

public interface IControllerInterface extends IStatusInterface, IProgramsInterface, IZonesInterface {
	public void configured(AuthResponseEntity config);

	public boolean hasTabByName(String tabName);

	public void setSelectedTab(String tabName) throws RainMachineException;
}
