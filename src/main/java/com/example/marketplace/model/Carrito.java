package com.example.marketplace.model;
import java.time.LocalDateTime;

public class Carrito {
    private int id_carrito;
    private int id_usuario;
    private LocalDateTime fecha_creacion;
    private String estado; // "activo", "finalizado", "abandonado"

    // Constructor vacío
    public Carrito() {
    }

    // Constructor sin ID (para crear)
    public Carrito(int id_usuario, String estado) {
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.fecha_creacion = LocalDateTime.now();
    }

    // Constructor completo
    public Carrito(int id_carrito, LocalDateTime fecha_creacion, int id_usuario, String estado) {
        this.id_carrito = id_carrito;
        this.fecha_creacion = fecha_creacion;
        this.id_usuario = id_usuario;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
    }

    public LocalDateTime getfecha_creacion() {
        return fecha_creacion;
    }

    public void setfecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getid_usuario() {
        return id_usuario;
    }

    public void setid_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "idCarrito=" + id_carrito +
                ", fecha_creacion=" + fecha_creacion +
                ", id_usuario=" + id_usuario +
                ", estado='" + estado + '\'' +
                '}';
    }
}