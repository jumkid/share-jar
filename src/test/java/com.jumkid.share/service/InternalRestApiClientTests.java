package com.jumkid.share.service;

import com.jumkid.share.user.UserProfileManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static com.jumkid.share.util.Constants.JOURNEY_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InternalRestApiClientTests {

    private RestTemplate restTemplate;
    private UserProfileManager userProfileManager;
    private HttpServletRequest httpServletRequest;

    private InternalRestApiClient internalRestApiClient;

    @BeforeAll
    public void setUp(@Mock RestTemplate restTemplate,
                      @Mock UserProfileManager userProfileManager,
                      @Mock HttpServletRequest httpServletRequest) {
        this.restTemplate = restTemplate;
        this.userProfileManager = userProfileManager;
        this.httpServletRequest = httpServletRequest;
        this.internalRestApiClient = new InternalRestApiClient(restTemplate, userProfileManager, httpServletRequest);

        when(httpServletRequest.getHeader(JOURNEY_ID)).thenReturn("abed-1234");
        when(userProfileManager.getAccessToken()).thenReturn("bdadgadgwerqweriukjnvj");
    }

    @Test
    @DisplayName("This is a dummy test case")
    @Disabled("this is just for test purpose")
    void dummyTest() {
        fail("a dummy function should never run");
    }

    @Test
    void whenGivenServiceURIAndObjectType_shouldHandleGetSuccessfully() throws URISyntaxException {
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
