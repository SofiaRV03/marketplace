package com.example.marketplace.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MensajeLoginRequeridoController {

    @FXML
    private Button btnIrLogin;

    @FXML
    private Button btnCancelar;

    private boolean irALogin = false;

    @FXML
    private void handleIrLogin() {
        irALogin = true;
        cerrarDialogo();
    }

    @FXML
    private void handleCancelar() {
        irALogin = false;
        cerrarDialogo();
    }

    @FXML
    private void handleMouseEntered() {
        btnIrLogin.setStyle("-fx-background-color: #D98892FF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 30 12 30; -fx-background-radius: 25; -fx-cursor: hand; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    @FXML
    private void handleMouseExited() {
        btnIrLogin.setStyle("-fx-background-color: #D98892FF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 30 12 30; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleMouseEnteredCancel() {
        btnCancelar.setStyle("-fx-background-color: #dfe6e9; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 30 12 30; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    @FXML
    private void handleMouseExitedCancel() {
        btnCancelar.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 30 12 30; -fx-background-radius: 25; -fx-cursor: hand;");
    }

    private void cerrarDialogo() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    public boolean debeIrALogin() {
        return irALogin;
    }
}