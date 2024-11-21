// PasswordValidator.java (nueva clase)
package com.sumativafs3.demo.utils;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
    
    public static void validatePassword(String password) {
        List<String> errores = new ArrayList<>();
        
        // 1. Longitud mínima y máxima
        if (password.length() < 8 || password.length() > 20) {
            errores.add("La contraseña debe tener entre 8 y 20 caracteres");
        }
        
        // 2. Debe contener al menos un número
        if (!password.matches(".*\\d.*")) {
            errores.add("La contraseña debe contener al menos un número");
        }
        
        // 3. Debe contener al menos una letra
        if (!password.matches(".*[a-zA-Z].*")) {
            errores.add("La contraseña debe contener al menos una letra");
        }
        
        // 4. Debe contener al menos un carácter especial
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            errores.add("La contraseña debe contener al menos un carácter especial");
        }
        
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errores));
        }
    }
}