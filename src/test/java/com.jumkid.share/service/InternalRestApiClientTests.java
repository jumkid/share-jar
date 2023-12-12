package com.jumkid.share.service;

import com.jumkid.share.user.UserProfileManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.jumkid.share.util.Constants.JOURNEY_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InternalRestApiClientTests {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserProfileManager userProfileManager;
    @Mock
    private HttpServletRequest httpServletRequest;

    private InternalRestApiClient internalRestApiClient;

    @Before
    public void setUp() {
        this.internalRestApiClient = new InternalRestApiClient(restTemplate, userProfileManager, httpServletRequest);

        when(httpServletRequest.getHeader(JOURNEY_ID)).thenReturn("abed-1234");
        when(userProfileManager.getAccessToken()).thenReturn("bdadgadgwerqweriukjnvj");
    }

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    @Test
    public void whenGivenServiceURIAndObjectType_shouldHandleGetSuccessfully() throws URISyntaxException {
        //given
        URI baseUri = new URI("http://api.jumkid.com");
        String responseBody = "hello world!";
        when(restTemplate.exchange(eq(baseUri),
                eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.status(200).body(responseBody));
        //when
        String response = internalRestApiClient.get(baseUri, String.class);
        //then
        assertEquals(responseBody, response, "response does not match");
    }
}
