package com.example.marketplace.controller;

import com.example.marketplace.model.Usuario;

public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioActual;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public boolean isLoggedIn() {
        return usuarioActual != null;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public int getIdUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getId() : -1;
    }

    public String getNombreUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getNombre() : "Invitado";
    }
}