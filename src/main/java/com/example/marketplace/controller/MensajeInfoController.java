package com.example.marketplace.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MensajeInfoController {

    @FXML
    private VBox contenedorNotificacion;

    @FXML
    private Label lblIcono;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblProducto;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblMensaje;

    private Stage stage;

    public void initialize() {
        // Configuración inicial si es necesaria
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Configura la notificación con los datos del producto
     */
    public void configurarNotificacionProducto(String nombreProducto, int cantidad, double precio) {
        lblTitulo.setText("¡Agregado!");
        lblIcono.setText("✓");
        lblProducto.setText(nombreProducto);
        lblCantidad.setText(cantidad + " unidad" + (cantidad > 1 ? "es" : ""));
        lblPrecio.setText(String.format("$%,.0f", precio));
        lblMensaje.setText("Agregado al carrito");

        // Estilo de éxito (verde)
        contenedorNotificacion.setStyle(
                "-fx-background-color: #d4edda; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #c3e6cb; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20;"
        );
        lblIcono.setTextFill(javafx.scene.paint.Color.web("#28a745"));
        aplicarEstiloTexto("#155724");

        iniciarTemporizador();
    }

    /**
     * Configura una notificación de éxito genérica
     */
    public void configurarNotificacionExito(String titulo, String mensaje) {
        lblTitulo.setText(titulo);
        lblIcono.setText("✓");
        lblProducto.setVisible(false);
        lblCantidad.setVisible(false);
        lblPrecio.setVisible(false);
        lblMensaje.setText(mensaje);

        contenedorNotificacion.setStyle(
                "-fx-background-color: #d4edda; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #c3e6cb; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20;"
        );
        lblIcono.setTextFill(javafx.scene.paint.Color.web("#28a745"));
        aplicarEstiloTexto("#155724");

        iniciarTemporizador();
    }

    /**
     * Configura una notificación de información
     */
    public void configurarNotificacionInfo(String titulo, String mensaje) {
        lblTitulo.setText(titulo);
        lblIcono.setText("ℹ");
        lblProducto.setVisible(false);
        lblCantidad.setVisible(false);
        lblPrecio.setVisible(false);
        lblMensaje.setText(mensaje);

        contenedorNotificacion.setStyle(
                "-fx-background-color: #d1ecf1; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #bee5eb; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20;"
        );
        lblIcono.setTextFill(javafx.scene.paint.Color.web("#17a2b8"));
        aplicarEstiloTexto("#0c5460");

        iniciarTemporizador();
    }

    /**
     * Configura una notificación de advertencia
     */
    public void configurarNotificacionAdvertencia(String titulo, String mensaje) {
        lblTitulo.setText(titulo);
        lblIcono.setText("⚠");
        lblProducto.setVisible(false);
        lblCantidad.setVisible(false);
        lblPrecio.setVisible(false);
        lblMensaje.setText(mensaje);

        contenedorNotificacion.setStyle(
                "-fx-background-color: #fff3cd; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #ffeaa7; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20;"
        );
        lblIcono.setTextFill(javafx.scene.paint.Color.web("#ffc107"));
        aplicarEstiloTexto("#856404");

        iniciarTemporizador();
    }

    /**
     * Configura una notificación de error
     */
    public void configurarNotificacionError(String titulo, String mensaje) {
        lblTitulo.setText(titulo);
        lblIcono.setText("✗");
        lblProducto.setVisible(false);
        lblCantidad.setVisible(false);
        lblPrecio.setVisible(false);
        lblMensaje.setText(mensaje);

        contenedorNotificacion.setStyle(
                "-fx-background-color: #f8d7da; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #f5c6cb; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20;"
        );
        lblIcono.setTextFill(javafx.scene.paint.Color.web("#dc3545"));
        aplicarEstiloTexto("#721c24");

        iniciarTemporizador();
    }

    /**
     * Aplica estilos de texto consistentes
     */
    private void aplicarEstiloTexto(String color) {
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        lblProducto.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        lblCantidad.setStyle("-fx-font-size: 14px; -fx-text-fill: " + color + ";");
        lblPrecio.setStyle("-fx-font-size: 14px; -fx-text-fill: " + color + ";");
        lblMensaje.setStyle("-fx-font-size: 13px; -fx-text-fill: " + color + "; -fx-font-style: italic;");
    }

    /**
     * Inicia el temporizador para cerrar automáticamente la notificación
     */
    private void iniciarTemporizador() {
        // Animación de entrada (fade in)
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contenedorNotificacion);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Pausa de 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> cerrarConAnimacion());
        pause.play();
    }

    /**
     * Cierra la ventana con animación de salida
     */
    private void cerrarConAnimacion() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), contenedorNotificacion);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            if (stage != null) {
                stage.close();
            }
        });
        fadeOut.play();
    }
}