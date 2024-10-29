package com.sumativafs3.demo.response;

public class LoginResponse {
    private String mensaje;

    public LoginResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}