package com.example.marketplace.controller;

import com.example.marketplace.model.Usuario;
import com.example.marketplace.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtConfirmarContrasena;
    @FXML private TextField txtDireccion;
    @FXML private Button btnRegistrar;
    @FXML private Hyperlink linkLogin;
    @FXML private Label lblMensaje;

    private UsuarioService usuarioService;

    public RegistroController() {
        this.usuarioService = new UsuarioService();
    }

    @FXML
    public void initialize() {
        lblMensaje.setText("");
    }

    @FXML
    private void handleRegistro() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText();
        String confirmarContrasena = txtConfirmarContrasena.getText();
        String direccion = txtDireccion.getText().trim();

        // Validaciones
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || direccion.isEmpty()) {
            mostrarError("Por favor completa todos los campos");
            return;
        }

        if (!correo.contains("@")) {
            mostrarError("Por favor ingresa un correo válido");
            return;
        }

        if (contrasena.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }

        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(nombre, correo, contrasena);

            if (nuevoUsuario != null) {
                // Actualizar dirección
                nuevoUsuario.setDireccion(direccion);
                usuarioService.actualizarUsuario(nuevoUsuario);

                mostrarExito("¡Registro exitoso! Redirigiendo al login...");

                // Esperar 1.5 segundos y volver al login
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(() -> volverAlLogin());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                mostrarError("El correo ya está registrado");
            }
        } catch (Exception e) {
            mostrarError("Error al registrar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVolverLogin() {
        volverAlLogin();
    }
    @FXML
    private void handleVolverCatalogo() {
        irACatalogo();
    }

    private void irACatalogo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnRegistrar.getScene().getWindow();

            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Marketplace - Catálogo");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error al abrir catálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void volverAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnRegistrar.getScene().getWindow();

            // Mantener el tamaño actual de la ventana
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");

            // Centrar en la pantalla
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error al volver al login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: red;");
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: green;");
    }
}