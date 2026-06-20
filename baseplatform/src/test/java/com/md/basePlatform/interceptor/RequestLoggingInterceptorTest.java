package com.md.basePlatform.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RequestLoggingInterceptorTest {

    @Test
    void should_set_request_id_when_header_missing() {
        RequestLoggingInterceptor interceptor = new RequestLoggingInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean proceed = interceptor.preHandle(request, response, new Object());

        assertTrue(proceed);
        assertNotNull(request.getAttribute(RequestLoggingInterceptor.ATTR_REQUEST_ID));
    }

    @Test
    void should_keep_header_request_id() {
        RequestLoggingInterceptor interceptor = new RequestLoggingInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestLoggingInterceptor.HEADER_REQUEST_ID, "rid-1");

        interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertEquals("rid-1", request.getAttribute(RequestLoggingInterceptor.ATTR_REQUEST_ID));
    }
}
