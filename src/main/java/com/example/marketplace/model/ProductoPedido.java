package com.example.marketplace.model;

public class ProductoPedido {
    private int id_pedido;
    private int id_producto;
    private int cantidad;
    private double precio_unitario;
    private double subtotal;


    public ProductoPedido() {}

    public ProductoPedido(int id_pedido, int id_producto, int cantidad, double precio_unitario, double subtotal ) {
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;

    }

    public int getId_pedido() {return id_pedido;}
    public int getId_producto() {return id_producto;}
    public int getCantidad() {return cantidad;}
    public double getPrecio_unitario() {return precio_unitario;}
    public double getSubtotal() {return subtotal;}

    public void setId_pedido(int id_pedido) {this.id_pedido = id_pedido;}
    public void setId_producto(int id_producto) {this.id_producto = id_producto;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}
    public void setPrecio_unitario(double precio_unitario) {this.precio_unitario = precio_unitario;}
    public void setSubtotal(double subtotal) {this.subtotal = subtotal;}






}
