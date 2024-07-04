package com.kscottklein.rainmachine.mvc.view;

public interface IStatusPanel {
	public void setConnected(boolean isConnected);

	public void setConnectionName(String name);

	public void setErrorMessage(String msg);

	public void setInfoMessage(String msg);
}
