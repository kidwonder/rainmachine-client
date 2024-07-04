package com.kscottklein.rainmachine.restclient;

import com.kscottklein.rainmachine.entity.ZonesResponseEntity;

public interface IZonesApiInterface {

	void startStopZone(String accessToken, int id, boolean isStopped);

	ZonesResponseEntity zones(String accessToken) throws RestClientException;

}