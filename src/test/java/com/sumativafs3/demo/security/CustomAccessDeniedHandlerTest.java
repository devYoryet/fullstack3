package com.sumativafs3.demo.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;

class CustomAccessDeniedHandlerTest {

    @Test
    void handleAccessDeniedTest() throws IOException {
        // Crear mocks para HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException exception = new AccessDeniedException(
                "No tienes permisos suficientes para acceder a este recurso");

        // Configurar el request
        when(request.getServletPath()).thenReturn("/api/protected");

        // Crear el handler
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

        // Ejecutar el método handle
        accessDeniedHandler.handle(request, response, exception);

        // Verificar que la respuesta es correcta
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals("application/json", response.getContentType());

        // Convertir el outputStream a String y verificar contenido
        String responseContent = response.getContentAsString();
        assertTrue(responseContent.contains("\"error\":\"Acceso denegado\""));
        assertTrue(
                responseContent.contains("\"message\":\"No tienes permisos suficientes para acceder a este recurso\""));
        assertTrue(responseContent.contains("\"status\":403"));
        assertTrue(responseContent.contains("\"path\":\"/api/protected\""));
    }
}
