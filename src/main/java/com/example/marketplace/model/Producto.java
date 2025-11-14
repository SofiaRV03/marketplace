package com.example.marketplace.model;

import javafx.beans.property.*;

import java.util.List;

public class Producto {

    private int id_producto;
    private String nombre_producto;
    private String descripcion_producto;
    private double precio_producto;
    private int stock_producto;
    private String imagen_producto;
    private List<String> categorias;

    // Properties para JavaFX (necesarias para TableView)
    private IntegerProperty idProperty;
    private StringProperty nombreProperty;
    private StringProperty descripcionProperty;
    private DoubleProperty precioProperty;
    private IntegerProperty stockProperty;
    private StringProperty imagenProperty;

    public Producto() {
        initializeProperties();
    }

    public Producto(int id_producto, String nombre_producto, String descripcion_producto,
                    double precio_producto, int stock_producto, String imagen_producto) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.descripcion_producto = descripcion_producto;
        this.precio_producto = precio_producto;
        this.stock_producto = stock_producto;
        this.imagen_producto = imagen_producto;
        initializeProperties();
    }

    public Producto(String nombre_producto, String descripcion_producto,
                    double precio_producto, int stock_producto, String imagen_producto) {
        this.nombre_producto = nombre_producto;
        this.descripcion_producto = descripcion_producto;
        this.precio_producto = precio_producto;
        this.stock_producto = stock_producto;
        this.imagen_producto = imagen_producto;
        initializeProperties();
    }

    // Inicializar properties para JavaFX
    private void initializeProperties() {
        this.idProperty = new SimpleIntegerProperty(id_producto);
        this.nombreProperty = new SimpleStringProperty(nombre_producto);
        this.descripcionProperty = new SimpleStringProperty(descripcion_producto);
        this.precioProperty = new SimpleDoubleProperty(precio_producto);
        this.stockProperty = new SimpleIntegerProperty(stock_producto);
        this.imagenProperty = new SimpleStringProperty(imagen_producto);
    }

    // Sincronizar properties con valores primitivos
    private void syncProperties() {
        if (idProperty != null) idProperty.set(id_producto);
        if (nombreProperty != null) nombreProperty.set(nombre_producto);
        if (descripcionProperty != null) descripcionProperty.set(descripcion_producto);
        if (precioProperty != null) precioProperty.set(precio_producto);
        if (stockProperty != null) stockProperty.set(stock_producto);
        if (imagenProperty != null) imagenProperty.set(imagen_producto);
    }

    // Getters normales
    public int getId_producto() {return id_producto;}
    public String getNombre_producto() {return nombre_producto;}
    public String getDescripcion_producto() {return descripcion_producto;}
    public double getPrecio_producto() {return precio_producto;}
    public int getStock_producto() {return stock_producto;}
    public String getImagen_producto() {return imagen_producto;}
    public List<String> getCategorias() {
        return categorias;
    }

    // Setters normales
    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
        if (idProperty != null) idProperty.set(id_producto);
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
        if (nombreProperty != null) nombreProperty.set(nombre_producto);
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
        if (descripcionProperty != null) descripcionProperty.set(descripcion_producto);
    }

    public void setPrecio_producto(double precio_producto) {
        this.precio_producto = precio_producto;
        if (precioProperty != null) precioProperty.set(precio_producto);
    }

    public void setStock_producto(int stock_producto) {
        this.stock_producto = stock_producto;
        if (stockProperty != null) stockProperty.set(stock_producto);
    }

    public void setImagen_producto(String imagen_producto) {
        this.imagen_producto = imagen_producto;
        if (imagenProperty != null) imagenProperty.set(imagen_producto);
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    // Properties para JavaFX TableView
    public IntegerProperty idProperty() {
        if (idProperty == null) {
            idProperty = new SimpleIntegerProperty(id_producto);
        }
        return idProperty;
    }

    public StringProperty nombreProperty() {
        if (nombreProperty == null) {
            nombreProperty = new SimpleStringProperty(nombre_producto);
        }
        return nombreProperty;
    }

    public StringProperty descripcionProperty() {
        if (descripcionProperty == null) {
            descripcionProperty = new SimpleStringProperty(descripcion_producto);
        }
        return descripcionProperty;
    }

    public DoubleProperty precioProperty() {
        if (precioProperty == null) {
            precioProperty = new SimpleDoubleProperty(precio_producto);
        }
        return precioProperty;
    }

    public IntegerProperty stockProperty() {
        if (stockProperty == null) {
            stockProperty = new SimpleIntegerProperty(stock_producto);
        }
        return stockProperty;
    }

    public StringProperty imagenProperty() {
        if (imagenProperty == null) {
            imagenProperty = new SimpleStringProperty(imagen_producto);
        }
        return imagenProperty;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id_producto +
                ", nombre=" + nombre_producto +
                ", descripcion=" + descripcion_producto +
                ", precio=" + precio_producto +
                ", stock=" + stock_producto +
                ", imagen=" + imagen_producto +
                '}';
    }
}