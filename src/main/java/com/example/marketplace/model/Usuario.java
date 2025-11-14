package com.example.marketplace.model;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;
    private LocalDateTime fechaRegistro;
    private String direccion;
    private String telefono; // Campo adicional para el teléfono

    // Constructor vacío
    public Usuario() {
    }

    // Constructor completo (sin teléfono - para compatibilidad)
    public Usuario(int id, String nombre, String correo, String contrasena, String tipo, LocalDateTime fechaRegistro, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.fechaRegistro = fechaRegistro;
        this.direccion = direccion;
    }

    // Constructor completo CON teléfono
    public Usuario(int id, String nombre, String correo, String contrasena, String tipo, LocalDateTime fechaRegistro, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.fechaRegistro = fechaRegistro;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Constructor para registro (sin ID)
    public Usuario(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = "cliente"; // Por defecto
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}