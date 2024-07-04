package com.kscottklein.rainmachine.restclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.kscottklein.rainmachine.mvc.model.Configuration;

public class RestClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private RestClient restClient;
    private Gson gson;
    @SuppressWarnings("unused")
	private Configuration config;

    @BeforeEach
    public void setUp() throws RestClientException {
        MockitoAnnotations.openMocks(this);
        restClient = new RestClient(true);
        gson = new Gson();
        this.config = new Configuration(true, "192.168.200.16", "8080", "204562thekid", true);
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testGet() throws IOException, InterruptedException, RestClientException {
        String url = "https://example.com/api";
        String responseBody = "{\"key\":\"value\"}";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(responseBody);

        Map<String, String> result = restClient.get(url, Map.class);

        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
        HttpRequest capturedRequest = requestCaptor.getValue();

        assertEquals(URI.create(url), capturedRequest.uri());
        assertEquals("value", result.get("key"));
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testPost() throws RestClientException, IOException, InterruptedException {
        String url = "https://example.com/api";
        Object requestBody = new Object();
        String jsonRequestBody = gson.toJson(requestBody);
        String responseBody = "{\"key\":\"value\"}";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(responseBody);

        Map<String, String> result = restClient.post(url, requestBody, Map.class);

        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));
        HttpRequest capturedRequest = requestCaptor.getValue();

        assertEquals(URI.create(url), capturedRequest.uri());
        assertEquals(jsonRequestBody, extractBody(capturedRequest.bodyPublisher()));
        assertEquals("value", result.get("key"));
    }

    private String extractBody(Optional<BodyPublisher> optional) {
    	if(!optional.isPresent()) {
    		return null;
    	}
        try {
            var byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            optional.get().subscribe(new Flow.Subscriber<ByteBuffer>() {
                @Override
                public void onSubscribe(Subscription subscription) {
                    subscription.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(ByteBuffer item) {
                    byteArrayOutputStream.write(item.array(), 0, item.remaining());
                }

                @Override
                public void onError(Throwable throwable) {
                    throw new UncheckedIOException(new IOException(throwable));
                }

                @Override
                public void onComplete() {
                }
            });
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}