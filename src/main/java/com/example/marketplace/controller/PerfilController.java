package com.example.marketplace.controller;

import com.example.marketplace.model.Usuario;
import com.example.marketplace.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PerfilController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;

    @FXML private PasswordField txtContrasenaActual;
    @FXML private PasswordField txtNuevaContrasena;
    @FXML private PasswordField txtConfirmarContrasena;


    @FXML private Label lblValidacionContrasena;
    @FXML private Button btnGuardarCambios;
    @FXML private Button btnCambiarContrasena;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnVolverCatalogo;
    @FXML private Label lblUsuario;

    private UsuarioService usuarioService;
    private Usuario usuarioActual;

    public PerfilController() {
        this.usuarioService = new UsuarioService();
    }

    @FXML
    public void initialize() {
        // Agregar listener para validar contraseña en tiempo real
        txtNuevaContrasena.textProperty().addListener((observable, oldValue, newValue) -> {
            validarContrasena(newValue);
        });

        SessionManager session = SessionManager.getInstance();


        lblUsuario.setText(session.getNombreUsuarioActual());
    }

    /**
     * Establece el usuario actual y carga sus datos
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarDatosUsuario();
    }

    /**
     * Carga los datos del usuario en los campos del formulario
     */
    private void cargarDatosUsuario() {
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre() != null ? usuarioActual.getNombre() : "");
            txtCorreo.setText(usuarioActual.getCorreo() != null ? usuarioActual.getCorreo() : "");
            txtDireccion.setText(usuarioActual.getDireccion() != null ? usuarioActual.getDireccion() : "");
            txtTelefono.setText(usuarioActual.getTelefono() != null ? usuarioActual.getTelefono() : "");
        }
    }

    /**
     * Guarda los cambios en la información personal del usuario
     */
    @FXML
    private void guardarCambios() {
        // Validar que los campos no estén vacíos
        if (txtNombre.getText().trim().isEmpty() ||
                txtCorreo.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Nombre y correo son obligatorios", Alert.AlertType.ERROR);
            return;
        }

        // Validar formato de correo
        if (!validarEmail(txtCorreo.getText().trim())) {
            mostrarAlerta("Error", "Ingresa un correo electrónico válido", Alert.AlertType.ERROR);
            return;
        }

        // Actualizar datos del usuario
        usuarioActual.setNombre(txtNombre.getText().trim());
        usuarioActual.setCorreo(txtCorreo.getText().trim());
        usuarioActual.setDireccion(txtDireccion.getText().trim());
        usuarioActual.setTelefono(txtTelefono.getText().trim());

        // Guardar en la base de datos
        boolean exito = usuarioService.actualizarUsuario(usuarioActual);

        if (exito) {
            mostrarAlerta("Éxito", "Tus datos han sido actualizados correctamente",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudieron guardar los cambios. Intenta nuevamente",
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Cambia la contraseña del usuario
     */
    @FXML
    private void cambiarContrasena() {
        String contrasenaActual = txtContrasenaActual.getText();
        String nuevaContrasena = txtNuevaContrasena.getText();
        String confirmarContrasena = txtConfirmarContrasena.getText();

        // Validar que los campos no estén vacíos
        if (contrasenaActual.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos de contraseña son obligatorios",
                    Alert.AlertType.ERROR);
            return;
        }

        // Verificar que la contraseña actual sea correcta
        if (!usuarioActual.getContrasena().equals(contrasenaActual)) {
            mostrarAlerta("Error", "La contraseña actual es incorrecta", Alert.AlertType.ERROR);
            txtContrasenaActual.clear();
            return;
        }

        // Validar que la nueva contraseña tenga al menos 6 caracteres
        if (nuevaContrasena.length() < 6) {
            mostrarAlerta("Error", "La nueva contraseña debe tener al menos 6 caracteres",
                    Alert.AlertType.ERROR);
            return;
        }

        // Verificar que las contraseñas nuevas coincidan
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
            txtConfirmarContrasena.clear();
            return;
        }

        // Actualizar la contraseña
        usuarioActual.setContrasena(nuevaContrasena);
        boolean exito = usuarioService.actualizarUsuario(usuarioActual);

        if (exito) {
            mostrarAlerta("Éxito", "Tu contraseña ha sido cambiada correctamente",
                    Alert.AlertType.INFORMATION);
            // Limpiar campos de contraseña
            txtContrasenaActual.clear();
            txtNuevaContrasena.clear();
            txtConfirmarContrasena.clear();
        } else {
            mostrarAlerta("Error", "No se pudo cambiar la contraseña. Intenta nuevamente",
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Valida la contraseña en tiempo real
     */
    private void validarContrasena(String contrasena) {
        if (contrasena.length() < 6) {
            lblValidacionContrasena.setText("La contraseña debe tener al menos 6 caracteres");
            lblValidacionContrasena.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            lblValidacionContrasena.setText("✓ Contraseña válida");
            lblValidacionContrasena.setStyle("-fx-text-fill: #27ae60;");
        }
    }

    /**
     * Valida el formato del email
     */
    private boolean validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Vuelve a la vista del catálogo
     */
    @FXML
    private void volverCatalogo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverCatalogo.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Marketplace - Catálogo");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    @FXML
    private void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Estás seguro que deseas cerrar sesión?");
        confirmacion.setContentText("Deberás iniciar sesión nuevamente para acceder");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                // Cargar la vista de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/view/Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cerrar la sesión", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Muestra una alerta con el mensaje especificado
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}