package com.sumativafs3.demo.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResourceNotFoundExceptionTest {
    @Test
    void testConstructorAndGetters() {
        String resourceName = "Usuario";
        String fieldName = "id";
        Object fieldValue = 1L;

        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        assertEquals(resourceName, exception.getResourceName());
        assertEquals(fieldName, exception.getFieldName());
        assertEquals(fieldValue, exception.getFieldValue());
        assertEquals(
                "Usuario no encontrado con id : '1'",
                exception.getMessage());
    }
}
