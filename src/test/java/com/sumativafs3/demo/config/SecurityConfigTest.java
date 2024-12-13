package com.sumativafs3.demo.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.sumativafs3.demo.security.CustomAccessDeniedHandler;
import com.sumativafs3.demo.security.CustomAuthenticationEntryPoint;

class SecurityConfigTest {

    @Mock
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    private CustomAccessDeniedHandler accessDeniedHandler;

    @InjectMocks
    private SecurityConfig securityConfig;

    private MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRequest = new MockHttpServletRequest();
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(mockRequest);

        assertNotNull(config);
        List<String> allowedOrigins = config.getAllowedOrigins();
        assertTrue(allowedOrigins.contains("http://localhost:4200"));
    }

    @Test
    void testCorsMethods() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(mockRequest);
        List<String> allowedMethods = config.getAllowedMethods();

        assertNotNull(allowedMethods);
        assertTrue(allowedMethods.containsAll(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")));
    }

    @Test
    void testCorsHeaders() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(mockRequest);
        List<String> allowedHeaders = config.getAllowedHeaders();

        assertNotNull(allowedHeaders);
        assertTrue(allowedHeaders.containsAll(
                Arrays.asList("Origin", "Content-Type", "Accept", "Authorization")));
        assertTrue(config.getAllowCredentials());
    }

    @Test
    void testRateLimiter() {
        assertNotNull(securityConfig.rateLimiter());
    }
}