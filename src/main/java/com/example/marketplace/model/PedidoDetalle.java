package com.example.marketplace.model;

import javafx.beans.property.*;

/**
 * Clase que combina información del pedido con detalles del producto
 * Útil para mostrar en tablas JavaFX el historial de pedidos
 */
public class PedidoDetalle {

    private int id_pedido;
    private int id_producto;
    private String nombre_producto;
    private String imagen_producto;
    private int cantidad;
    private double precio_unitario;
    private double subtotal;

    // Properties para JavaFX
    private IntegerProperty idPedidoProperty;
    private IntegerProperty idProductoProperty;
    private StringProperty nombreProductoProperty;
    private StringProperty imagenProductoProperty;
    private IntegerProperty cantidadProperty;
    private DoubleProperty precioUnitarioProperty;
    private DoubleProperty subtotalProperty;

    public PedidoDetalle() {
        initializeProperties();
    }

    public PedidoDetalle(int id_pedido, int id_producto, String nombre_producto,
                         String imagen_producto, int cantidad, double precio_unitario, double subtotal) {
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.imagen_producto = imagen_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
        initializeProperties();
    }

    private void initializeProperties() {
        this.idPedidoProperty = new SimpleIntegerProperty(id_pedido);
        this.idProductoProperty = new SimpleIntegerProperty(id_producto);
        this.nombreProductoProperty = new SimpleStringProperty(nombre_producto);
        this.imagenProductoProperty = new SimpleStringProperty(imagen_producto);
        this.cantidadProperty = new SimpleIntegerProperty(cantidad);
        this.precioUnitarioProperty = new SimpleDoubleProperty(precio_unitario);
        this.subtotalProperty = new SimpleDoubleProperty(subtotal);
    }

    // Getters
    public int getId_pedido() { return id_pedido; }
    public int getId_producto() { return id_producto; }
    public String getNombre_producto() { return nombre_producto; }
    public String getImagen_producto() { return imagen_producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecio_unitario() { return precio_unitario; }
    public double getSubtotal() { return subtotal; }

    // Setters
    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
        if (idPedidoProperty != null) idPedidoProperty.set(id_pedido);
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
        if (idProductoProperty != null) idProductoProperty.set(id_producto);
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
        if (nombreProductoProperty != null) nombreProductoProperty.set(nombre_producto);
    }

    public void setImagen_producto(String imagen_producto) {
        this.imagen_producto = imagen_producto;
        if (imagenProductoProperty != null) imagenProductoProperty.set(imagen_producto);
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        if (cantidadProperty != null) cantidadProperty.set(cantidad);
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
        if (precioUnitarioProperty != null) precioUnitarioProperty.set(precio_unitario);
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
        if (subtotalProperty != null) subtotalProperty.set(subtotal);
    }

    // Properties para JavaFX
    public IntegerProperty idPedidoProperty() {
        if (idPedidoProperty == null) {
            idPedidoProperty = new SimpleIntegerProperty(id_pedido);
        }
        return idPedidoProperty;
    }

    public IntegerProperty idProductoProperty() {
        if (idProductoProperty == null) {
            idProductoProperty = new SimpleIntegerProperty(id_producto);
        }
        return idProductoProperty;
    }

    public StringProperty nombreProductoProperty() {
        if (nombreProductoProperty == null) {
            nombreProductoProperty = new SimpleStringProperty(nombre_producto);
        }
        return nombreProductoProperty;
    }

    public StringProperty imagenProductoProperty() {
        if (imagenProductoProperty == null) {
            imagenProductoProperty = new SimpleStringProperty(imagen_producto);
        }
        return imagenProductoProperty;
    }

    public IntegerProperty cantidadProperty() {
        if (cantidadProperty == null) {
            cantidadProperty = new SimpleIntegerProperty(cantidad);
        }
        return cantidadProperty;
    }

    public DoubleProperty precioUnitarioProperty() {
        if (precioUnitarioProperty == null) {
            precioUnitarioProperty = new SimpleDoubleProperty(precio_unitario);
        }
        return precioUnitarioProperty;
    }

    public DoubleProperty subtotalProperty() {
        if (subtotalProperty == null) {
            subtotalProperty = new SimpleDoubleProperty(subtotal);
        }
        return subtotalProperty;
    }

    @Override
    public String toString() {
        return "PedidoDetalle{" +
                "idPedido=" + id_pedido +
                ", idProducto=" + id_producto +
                ", nombreProducto='" + nombre_producto + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precio_unitario +
                ", subtotal=" + subtotal +
                '}';
    }
}