package com.example.marketplace.utils;

import com.example.marketplace.controller.MensajeInfoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MensajeInfoUtil {

    /**
     * Muestra una notificación de producto agregado al carrito
     */
    public static void mostrarProductoAgregado(String nombreProducto, int cantidad, double precioTotal) {
        mostrarNotificacion((controller) ->
                controller.configurarNotificacionProducto(nombreProducto, cantidad, precioTotal)
        );
    }

    /**
     * Muestra una notificación de éxito genérica
     */
    public static void mostrarExito(String titulo, String mensaje) {
        mostrarNotificacion((controller) ->
                controller.configurarNotificacionExito(titulo, mensaje)
        );
    }

    /**
     * Muestra una notificación de información
     */
    public static void mostrarInfo(String titulo, String mensaje) {
        mostrarNotificacion((controller) ->
                controller.configurarNotificacionInfo(titulo, mensaje)
        );
    }

    /**
     * Muestra una notificación de advertencia
     */
    public static void mostrarAdvertencia(String titulo, String mensaje) {
        mostrarNotificacion((controller) ->
                controller.configurarNotificacionAdvertencia(titulo, mensaje)
        );
    }

    /**
     * Muestra una notificación de error
     */
    public static void mostrarError(String titulo, String mensaje) {
        mostrarNotificacion((controller) ->
                controller.configurarNotificacionError(titulo, mensaje)
        );
    }

    /**
     * Método interno para mostrar la notificación
     */
    private static void mostrarNotificacion(ConfiguradorNotificacion configurador) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MensajeInfoUtil.class.getResource("/com/example/marketplace/MensajeInfo.fxml")
            );
            Parent root = loader.load();
            MensajeInfoController controller = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setScene(scene);

            controller.setStage(stage);
            configurador.configurar(controller);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al mostrar notificación: " + e.getMessage());
        }
    }

    /**
     * Interfaz funcional para configurar la notificación
     */
    @FunctionalInterface
    private interface ConfiguradorNotificacion {
        void configurar(MensajeInfoController controller);
    }
}