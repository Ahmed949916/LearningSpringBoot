package org.redmath.taskmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsrfControllerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CsrfController csrfController;

    @Test
    public void testGetCsrfToken() {

        String tokenValue = "test-csrf-token";
        String headerName = "X-CSRF-TOKEN";
        CsrfToken csrfToken = new DefaultCsrfToken(headerName, "_csrf", tokenValue);
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(csrfToken);


        ResponseEntity<Map<String, String>> response = csrfController.getCsrfToken(request);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenValue, response.getHeaders().getFirst(headerName));
        assertNotNull(response.getBody());
        assertEquals(tokenValue, response.getBody().get("token"));
    }
}