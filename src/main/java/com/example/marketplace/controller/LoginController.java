package com.example.marketplace.controller;

import com.example.marketplace.model.Usuario;
import com.example.marketplace.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnLogin;
    @FXML private Button btnVolverCatalogo;
    @FXML private Hyperlink linkRegistro;
    @FXML private Label lblMensaje;

    private UsuarioService usuarioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    @FXML
    public void initialize() {
        lblMensaje.setText("");
    }

    @FXML
    private void handleLogin() {
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Por favor completa todos los campos");
            return;
        }

        try {
            Usuario usuario = usuarioService.login(correo, contrasena);

            if (usuario != null) {
                // Guardar sesión
                SessionManager.getInstance().setUsuarioActual(usuario);

                // Ir al catálogo
                irACatalogo();
            } else {
                mostrarError("Correo o contraseña incorrectos");
            }
        } catch (Exception e) {
            mostrarError("Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/RegistroView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogin.getScene().getWindow();

            // Mantener el tamaño actual de la ventana
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Registro de Usuario");

        } catch (Exception e) {
            mostrarError("Error al abrir registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVolverCatalogo() {
        irACatalogo();
    }

    private void irACatalogo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogin.getScene().getWindow();

            // Mantener el tamaño actual de la ventana
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Marketplace - Catálogo");

        } catch (Exception e) {
            mostrarError("Error al abrir catálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: red;");
    }
}