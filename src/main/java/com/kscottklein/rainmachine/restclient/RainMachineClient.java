package com.kscottklein.rainmachine.restclient;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kscottklein.rainmachine.entity.AuthRequestEntity;
import com.kscottklein.rainmachine.entity.AuthResponseEntity;
import com.kscottklein.rainmachine.entity.ProgramResponseEntity;
import com.kscottklein.rainmachine.entity.ZonesResponseEntity;
import com.kscottklein.rainmachine.mvc.model.Configuration;

public class RainMachineClient implements IZonesApiInterface {
	static enum EnumUrl {
		//@formatter:off
		URL_AUTH_LOGIN("auth/login", false), 
		URL_INFO_PROGRAMS("program", true), 
		URL_INFO_ZONES("zone", true),
		URL_ZONE_START("zone/{}/start", true), 
		URL_ZONE_STOP("zone/{}/stop", true),
		;

		private boolean requiresToken = false;
		private String path;

		private EnumUrl(String path, boolean requiresToken) {
			this.path = path;
			this.requiresToken = requiresToken;
		}

		public String getPath() {
			return path;
		}

		public boolean isRequiresToken() {
			return requiresToken;
		}
	}

	private static final Logger log = LogManager.getLogger();

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{}");

	private static final String URL_PATH_SEPARATOR = "/";

	private static RainMachineClient _instance;

	private static RestClient client;

	private static String baseUrl;

	private static String buildUrl(EnumUrl apiPath, String accessToken, String... args) {
		final String apiPathWithVars = replaceVariables(apiPath.getPath(), args);
		if (apiPath.isRequiresToken()) {
			return String.format("%s%s?access_token=%s", baseUrl, apiPathWithVars, accessToken);
		}
		return String.format("%s%s", baseUrl, apiPathWithVars);
	}

	private static int countPlaceholders(String template) {
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	public static void createInstance(final Configuration config) {
		try {
			baseUrl = config.createBaseUrl();
			if (!baseUrl.endsWith(URL_PATH_SEPARATOR)) {
				baseUrl = String.format("%s%sapi/4/", baseUrl, URL_PATH_SEPARATOR);
			}
			client = new RestClient(config.isSecure());
			_instance = new RainMachineClient();
		} catch (RestClientException e) {
			log.error("Error", e);
		}
	}

	public static RainMachineClient getInstance() {
		if (_instance == null) {
			throw new IllegalStateException("RainMachineClient has not been created.");
		}
		return _instance;
	}

	public static void main(String... strings) throws RestClientException {
		Configuration config = new Configuration(true, "192.168.200.16", "8080", "204562thekid", false);
		log.debug("Configuration: {}", config.toString());
		RainMachineClient.createInstance(config);
		RainMachineClient client = RainMachineClient.getInstance();

		AuthRequestEntity req = new AuthRequestEntity(config);
		AuthResponseEntity resp = client.authLogin(req);
		log.debug(resp);
	}

	private static String replaceVariables(String template, String[] args) {
		int placeholderCount = countPlaceholders(template);
		if (placeholderCount != args.length) {
			final String msg = String.format(
					"Number of path parameter values provided [%d] does not match the number in the URL [%d]: [%s]",
					args.length, placeholderCount, template);
			throw new IllegalArgumentException(msg);
		}

		Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
		Iterator<String> iterator = Arrays.asList(args).iterator();
		
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(result, iterator.next());
		}
		matcher.appendTail(result);

		return result.toString();
	}

	private RainMachineClient() {
		super();
	}

	public AuthResponseEntity authLogin(AuthRequestEntity req) throws RestClientException {
		final String url = buildUrl(EnumUrl.URL_AUTH_LOGIN, null);
		log.debug("API: auth/login > {}", url);
		AuthResponseEntity response = client.post(url, req, AuthResponseEntity.class);
		return response;
	}

	public ProgramResponseEntity programs(final String accessToken) throws RestClientException {
		final String url = buildUrl(EnumUrl.URL_INFO_PROGRAMS, accessToken);
		log.debug("API: programs > {}", url);
		ProgramResponseEntity response = client.get(url, ProgramResponseEntity.class);
		return response;
	}

	@Override
	public void startStopZone(String accessToken, int id, boolean isStopped) {
		EnumUrl eUrl = isStopped ? EnumUrl.URL_ZONE_START : EnumUrl.URL_ZONE_STOP;
		final String url = buildUrl(eUrl, accessToken, new String[] {Integer.toString(id)});
		log.debug("API: zones > {}", url);
		// TODO: Implement API call
		return;
	}

	@Override
	public ZonesResponseEntity zones(String accessToken) throws RestClientException {
		final String url = buildUrl(EnumUrl.URL_INFO_ZONES, accessToken);
		log.debug("API: zones > {}", url);
		ZonesResponseEntity response = client.get(url, ZonesResponseEntity.class);
		return response;
	}
}
