package com.sumativafs3.demo.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
public class CustomAuthenticationEntryPointTest {

    private CustomAuthenticationEntryPoint entryPoint;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AuthenticationException authException;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        entryPoint = new CustomAuthenticationEntryPoint();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        response.setCharacterEncoding("UTF-8");
        authException = new BadCredentialsException("Bad credentials");
        objectMapper = new ObjectMapper();
    }

    @Test
    void commence_ShouldReturnUnauthorizedResponse() throws IOException {
    // When
    entryPoint.commence(request, response, authException);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
    
        // Parse JSON response
        Map<String, Object> responseBody = objectMapper.readValue(
                response.getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<Map<String, Object>>() {
                });

        // Validate response contents
        assertEquals(401, responseBody.get("status"));
        assertEquals("No autorizado", responseBody.get("error"));
        assertEquals("Necesitas iniciar sesión para acceder a este recurso",
                responseBody.get("message"),
                "El mensaje no coincide debido a problemas de codificación");
        assertNotNull(responseBody.get("timestamp"));
        assertEquals(request.getServletPath(), responseBody.get("path"));
    }
}