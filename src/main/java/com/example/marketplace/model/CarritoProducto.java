package com.example.marketplace.model;

public class CarritoProducto {
    private int id_carrito;
    private int id_producto;
    private int cantidad;
    private double subtotal;

    // Constructor vacío
    public CarritoProducto() {
    }

    // Constructor completo
    public CarritoProducto(int id_carrito, int id_producto, int cantidad, double subtotal) {
        this.id_carrito = id_carrito;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getid_carrito() {
        return id_carrito;
    }

    public void setid_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
    }

    public int getid_producto() {
        return id_producto;
    }

    public void setid_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CarritoProducto{" +
                "id_carrito=" + id_carrito +
                ", id_producto=" + id_producto +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}