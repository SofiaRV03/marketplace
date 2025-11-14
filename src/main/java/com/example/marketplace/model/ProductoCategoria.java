package com.example.marketplace.model;

public class ProductoCategoria {
    private int idProducto;
    private int idCategoria;

    // Constructores
    public ProductoCategoria() {
    }

    public ProductoCategoria(int idProducto, int idCategoria) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "ProductoCategoria{" +
                "idProducto=" + idProducto +
                ", idCategoria=" + idCategoria +
                '}';
    }
}