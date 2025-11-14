package com.example.marketplace.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MensajeCantidadController {

    @FXML
    private Label lblProductoNombre;

    @FXML
    private Label lblStock;

    @FXML
    private TextField txtCantidad;

    @FXML
    private Label lblError;

    @FXML
    private Button btnDecrementar;

    @FXML
    private Button btnIncrementar;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;

    private int stockDisponible;
    private int cantidadSeleccionada = 0;
    private boolean agregado = false;

    public void initialize() {
        // Permitir solo números en el campo de cantidad
        txtCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCantidad.setText(oldValue);
            } else if (!newValue.isEmpty()) {
                validarCantidad(Integer.parseInt(newValue));
            }
        });
    }

    public void setProducto(String nombre, int stock) {
        lblProductoNombre.setText(nombre);
        this.stockDisponible = stock;
        lblStock.setText("Stock disponible: " + stock);
    }

    @FXML
    private void handleDecrementar() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad > 1) {
                txtCantidad.setText(String.valueOf(cantidad - 1));
                ocultarError();
            }
        } catch (NumberFormatException e) {
            txtCantidad.setText("1");
        }
    }

    @FXML
    private void handleIncrementar() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad < stockDisponible) {
                txtCantidad.setText(String.valueOf(cantidad + 1));
                ocultarError();
            } else {
                mostrarError("No hay suficiente stock disponible");
            }
        } catch (NumberFormatException e) {
            txtCantidad.setText("1");
        }
    }

    @FXML
    private void handleAgregar() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());

            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a 0");
                return;
            }

            if (cantidad > stockDisponible) {
                mostrarError("Solo hay " + stockDisponible + " unidades disponibles");
                return;
            }

            cantidadSeleccionada = cantidad;
            agregado = true;
            cerrarDialogo();

        } catch (NumberFormatException e) {
            mostrarError("Por favor ingresa un número válido");
        }
    }

    @FXML
    private void handleCancelar() {
        agregado = false;
        cerrarDialogo();
    }

    private void validarCantidad(int cantidad) {
        if (cantidad > stockDisponible) {
            mostrarError("Solo hay " + stockDisponible + " unidades disponibles");
        } else {
            ocultarError();
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void ocultarError() {
        lblError.setVisible(false);
    }

    // Métodos para efectos hover
    @FXML
    private void handleBtnDecMouseEntered() {
        btnDecrementar.setStyle("-fx-background-color: #dfe6e9; -fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-font-weight: bold; -fx-min-width: 50; -fx-min-height: 50; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleBtnDecMouseExited() {
        btnDecrementar.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-size: 24px; -fx-font-weight: bold; -fx-min-width: 50; -fx-min-height: 50; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleBtnIncMouseEntered() {
        btnIncrementar.setStyle("-fx-background-color: #d98892; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-min-width: 50; -fx-min-height: 50; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleBtnIncMouseExited() {
        btnIncrementar.setStyle("-fx-background-color: #d98892; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-min-width: 50; -fx-min-height: 50; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleBtnAgregarMouseEntered() {
        btnAgregar.setStyle("-fx-background-color: #d98892; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 14 35 14 35; -fx-background-radius: 25; -fx-cursor: hand; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    @FXML
    private void handleBtnAgregarMouseExited() {
        btnAgregar.setStyle("-fx-background-color: #d98892; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 14 35 14 35; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleBtnCancelMouseEntered() {
        btnCancelar.setStyle("-fx-background-color: #dfe6e9; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 25 12 25; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleBtnCancelMouseExited() {
        btnCancelar.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 25 12 25; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    private void cerrarDialogo() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    public boolean fueAgregado() {
        return agregado;
    }

    public int getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }
}