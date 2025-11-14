package com.example.marketplace.controller;

import com.example.marketplace.model.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemController {

    @FXML
    private ImageView imagen_producto;

    @FXML
    private Label nombre_producto;

    @FXML
    private Label precio_producto;

    private Producto producto;

    public void setData(Producto producto) {
        this.producto = producto;
        nombre_producto.setText(producto.getNombre_producto());
        precio_producto.setText(String.valueOf(producto.getPrecio_producto()));
        Image imagen= new Image(getClass().getResourceAsStream("/images/"+producto.getImagen_producto()));
        imagen_producto.setImage(imagen);
    }
}
