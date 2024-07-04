package com.kscottklein.rainmachine.restclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.kscottklein.rainmachine.entity.AuthRequestEntity;
import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.rainmachine.mvc.model.Configuration;
import com.kscottklein.rainmachine.restclient.RainMachineClient.EnumUrl;

public class RestClient {
	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		Properties props = System.getProperties();
		props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

		try {
			Configuration config = new Configuration(true, "192.168.200.16", "8080", "204562thekid", true);
			RestClient client = new RestClient(true);
			String url = "https://192.168.200.16:8080/api/4/" + EnumUrl.URL_AUTH_LOGIN.getPath();
			AuthRequestEntity req = new AuthRequestEntity(config);
			AuthResponseEntity response = client.post(url, req, AuthResponseEntity.class);
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HttpClient client;

	private Gson gson;

	public RestClient(boolean insecure) throws RestClientException {
		if (insecure) {
			// disable hostname verification for SSL connections
			Properties props = System.getProperties();
			props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
		}
		this.init();
	}

	public <T> T get(String url, Class<T> responseType) throws RestClientException {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new RuntimeException("Failed to fetch data: " + response.statusCode());
			}

			return gson.fromJson(response.body(), responseType);
		} catch (Exception e) {
			throw new RestClientException("Error", e);
		}
	}

	private void init() throws RestClientException {
		try {
			// Trust all certificates
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} }, new java.security.SecureRandom());

			// Build the HttpClient with the SSL context and custom hostname verifier
			//@formatter:off
			this.client = HttpClient.newBuilder()
									.sslContext(sslContext)
									.sslParameters(new SSLParameters() {
										{
											setEndpointIdentificationAlgorithm("");
										}
									})
									.build();
			//@formatter:on

			this.gson = new Gson();
		} catch (Exception e) {
			throw new RestClientException("Error creating REST client", e);
		}
	}

	public <T, R> R post(String url, T requestBody, Class<R> responseType) throws RestClientException {
		try {
			String jsonRequestBody = gson.toJson(requestBody);
			log.debug("POST: {} <= {}", url, jsonRequestBody);
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new RuntimeException("Failed to post data: " + response.statusCode());
			}

			return gson.fromJson(response.body(), responseType);
		} catch (IOException ioe) {
			throw new RestClientException("Error reading response", ioe);
		} catch (InterruptedException e) {
			throw new RestClientException("Error connecting", e);
		}
	}
}