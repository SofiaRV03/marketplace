package com.example.marketplace.model;

import java.time.LocalDateTime;

public class Resena {
    private int id_resena;
    private int id_usuario;
    private int id_producto;
    private String comentario;
    private int calificacion; // De 1 a 5
    private LocalDateTime fecha_resena;

    // Campos adicionales para mostrar información
    private String nombreUsuario;
    private String nombreProducto;

    // Constructor vacío
    public Resena() {
    }

    // Constructor para crear nueva reseña
    public Resena(int id_usuario, int id_producto, String comentario, int calificacion) {
        this.id_usuario = id_usuario;
        this.id_producto = id_producto;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha_resena = LocalDateTime.now();
    }

    // Constructor completo
    public Resena(int id_resena, int id_usuario, int id_producto, String comentario,
                  int calificacion, LocalDateTime fecha_resena) {
        this.id_resena = id_resena;
        this.id_usuario = id_usuario;
        this.id_producto = id_producto;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha_resena = fecha_resena;
    }

    // Constructor con información adicional
    public Resena(int id_resena, int id_usuario, int id_producto, String comentario,
                  int calificacion, LocalDateTime fecha_resena, String nombreUsuario,
                  String nombreProducto) {
        this.id_resena = id_resena;
        this.id_usuario = id_usuario;
        this.id_producto = id_producto;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha_resena = fecha_resena;
        this.nombreUsuario = nombreUsuario;
        this.nombreProducto = nombreProducto;
    }

    // Getters y Setters
    public int getId_resena() {
        return id_resena;
    }

    public void setId_resena(int id_resena) {
        this.id_resena = id_resena;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        // Validar que esté entre 1 y 5
        if (calificacion < 1) {
            this.calificacion = 1;
        } else if (calificacion > 5) {
            this.calificacion = 5;
        } else {
            this.calificacion = calificacion;
        }
    }

    public LocalDateTime getFecha_resena() {
        return fecha_resena;
    }

    public void setFecha_resena(LocalDateTime fecha_resena) {
        this.fecha_resena = fecha_resena;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public String toString() {
        return "Resena{" +
                "id_resena=" + id_resena +
                ", id_usuario=" + id_usuario +
                ", id_producto=" + id_producto +
                ", comentario='" + comentario + '\'' +
                ", calificacion=" + calificacion +
                ", fecha_resena=" + fecha_resena +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                '}';
    }
}