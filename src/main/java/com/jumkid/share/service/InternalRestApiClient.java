package com.jumkid.share.service;

import com.jumkid.share.security.exception.InternalRestApiException;
import com.jumkid.share.user.UserProfileManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.jumkid.share.util.Constants.JOURNEY_ID;

@Slf4j
@Component
public class InternalRestApiClient {

    private final RestTemplate restTemplate;

    private final UserProfileManager userProfileManager;

    private final HttpServletRequest httpServletRequest;

    public InternalRestApiClient(RestTemplate restTemplate,
                                 UserProfileManager userProfileManager,
                                 HttpServletRequest httpServletRequest) {
        this.restTemplate = restTemplate;
        this.userProfileManager = userProfileManager;
        this.httpServletRequest = httpServletRequest;
    }

    public <T> T get(URI serviceURI, Class<T> objectType) throws InternalRestApiException {
        return internalJsonDataExchange(serviceURI, objectType);
    }

    public <T> List<T> getList(URI serviceURI, String payload) throws InternalRestApiException {
        return jsonDataExchangeForLists(serviceURI, HttpMethod.GET, payload);
    }

    public <T> T post(URI serviceURI, MultiValueMap<String, String> params, Class<T> objectType)
            throws InternalRestApiException {
        return internalFormDataExchange(serviceURI, HttpMethod.POST, params, objectType);
    }

    public <T> T post(URI serviceURI, String payload, Class<T> objectType) throws InternalRestApiException {
        return internalJsonDataExchange(serviceURI, HttpMethod.POST, payload, objectType);
    }

    public <T> T put(URI serviceURI, MultiValueMap<String, String> params, Class<T> objectType)
            throws InternalRestApiException {
        return internalFormDataExchange(serviceURI, HttpMethod.PUT, params, objectType);
    }

    public <T> T put(URI serviceURI, String payload, Class<T> objectType) throws InternalRestApiException {
        return internalJsonDataExchange(serviceURI, HttpMethod.PUT, payload, objectType);
    }

    public <T> void delete(URI serviceURI, Class<T> objectType) throws InternalRestApiException {
        HttpHeaders httpHeaders = createHttpHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        doDataExchange(serviceURI, HttpMethod.DELETE, entity, objectType);
    }

    private <T> T internalJsonDataExchange(URI serviceURI, Class<T> objectType)
            throws InternalRestApiException{
        HttpHeaders httpHeaders = createHttpHeaders(null);
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        return doDataExchange(serviceURI, HttpMethod.GET, entity, objectType);
    }

    private <T> T internalFormDataExchange(URI serviceURI, HttpMethod httpMethod,
                                       MultiValueMap<String, String> params, Class<T> objectType)
            throws InternalRestApiException{
        HttpHeaders httpHeaders = createHttpHeaders(null);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        return doDataExchange(serviceURI, httpMethod, entity, objectType);
    }

    private <T> T internalJsonDataExchange(URI serviceURI, HttpMethod httpMethod, String payload, Class<T> objectType)
            throws InternalRestApiException{
        HttpHeaders httpHeaders = createHttpHeaders(null);
        HttpEntity<String> entity = new HttpEntity<>(payload, httpHeaders);
        return doDataExchange(serviceURI, httpMethod, entity, objectType);
    }

    private <T> T doDataExchange(URI serviceURI,
                                 HttpMethod httpMethod,
                                 HttpEntity<?> entity,
                                 Class<T> objectType) throws InternalRestApiException {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(serviceURI, httpMethod, entity, objectType);
            if (responseEntity != null) {
                logIfStatusCodeIsError(responseEntity.getStatusCode());
                return responseEntity.getBody();
            } else {
                logIfResponseIsNull(serviceURI, httpMethod, entity);
                throw new InternalRestApiException("Response is null");
            }
        } catch (HttpClientErrorException ex) {
            throw new InternalRestApiException(ex.getStatusCode(), ex.getMessage());
        } catch (ResourceAccessException ex) {
            log.error("Could not connect to the rest api service.");
            throw new InternalRestApiException(ex.getMessage());
        }
    }

    private <T> List<T> jsonDataExchangeForLists(URI serviceURI, HttpMethod httpMethod, String payload)
            throws InternalRestApiException {
        HttpHeaders httpHeaders = createHttpHeaders(null);
        HttpEntity<String> entity = new HttpEntity<>(payload, httpHeaders);
        try {
            // Don't replace List<T> with <> as IDEA suggests!
            ResponseEntity<List<T>> responseEntity = restTemplate.exchange(serviceURI, httpMethod, entity,
                    new ParameterizedTypeReference<List<T>>() {});
            if (responseEntity != null) {
                logIfStatusCodeIsError(responseEntity.getStatusCode());
                return responseEntity.getBody();
            } else {
                logIfResponseIsNull(serviceURI, httpMethod, entity);
                throw new InternalRestApiException("Response is null");
            }
        } catch (HttpClientErrorException | ResourceAccessException ex) {
            log.error("Could not connect to the rest-api service.");
            throw ex;
        }
    }

    private void logIfStatusCodeIsError(HttpStatusCode statusCode) {
        if (statusCode.isError()) {
            log.error("The rest-api service returned an unexpected response: '{}'.", statusCode);
        }
    }

    private void logIfResponseIsNull(URI serviceURI,
                                     HttpMethod httpMethod,
                                     HttpEntity<?> entity) {
        log.error("The rest-api service returned null response with URI: '{}', http method: {}, http entity: {}",
                serviceURI, httpMethod, entity);
    }

    private HttpHeaders createHttpHeaders(MediaType mediaType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(JOURNEY_ID, httpServletRequest.getHeader(JOURNEY_ID));
        if (mediaType != null) {
            httpHeaders.setAccept(Collections.singletonList(mediaType));
            httpHeaders.setContentType(mediaType);
        }
        httpHeaders.setBearerAuth(userProfileManager.getAccessToken());

        return httpHeaders;
    }
}
