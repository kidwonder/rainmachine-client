package com.kscottklein.rainmachine.mvc.model;

import com.kscottklein.rainmachine.entity.AuthResponseEntity;

public interface IModelAuth {

	AuthResponseEntity getAuthResponse();

	void setAuthResponse(AuthResponseEntity authResponse);

}