package com.example.marketplace.model;

import java.time.LocalDateTime;

public class Pedido {
    private int id_pedido;
    private int id_usuario;
    private LocalDateTime fecha_pedido;
    private double total;
    private String estado;
    private String nombreUsuario; // Para mostrar el nombre del usuario en el listado

    // Constructor vacío
    public Pedido() {
    }

    // Constructor completo con nombreUsuario
    public Pedido(int id_pedido, int id_usuario, LocalDateTime fecha_pedido, double total, String estado, String nombreUsuario) {
        this.id_pedido = id_pedido;
        this.id_usuario = id_usuario;
        this.fecha_pedido = fecha_pedido;
        this.total = total;
        this.estado = estado;
        this.nombreUsuario = nombreUsuario;
    }

    // Constructor sin ID (para crear)
    public Pedido(int id_usuario, LocalDateTime fecha_pedido, double total, String estado) {
        this.id_usuario = id_usuario;
        this.fecha_pedido = fecha_pedido;
        this.total = total;
        this.estado = estado;
    }

    // Constructor original
    public Pedido(int id_pedido, int id_usuario, LocalDateTime fecha_pedido, double total, String estado) {
        this.id_pedido = id_pedido;
        this.id_usuario = id_usuario;
        this.fecha_pedido = fecha_pedido;
        this.total = total;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public LocalDateTime getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(LocalDateTime fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id_pedido=" + id_pedido +
                ", id_usuario=" + id_usuario +
                ", fecha_pedido=" + fecha_pedido +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                '}';
    }
}