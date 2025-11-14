package com.example.marketplace.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Utilidad para crear ventanas en pantalla completa de manera consistente
 */
public class WindowHelper {

    /**
     * Abre una nueva ventana en pantalla completa
     * @param root El contenido de la ventana
     * @param title El título de la ventana
     * @return El Stage creado
     */
    public static Stage openFullScreen(Parent root, String title) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        Stage stage = new Stage();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        stage.setScene(scene);
        stage.setTitle(title);
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setResizable(true);
        stage.setMaximized(true);

        return stage;
    }

    /**
     * Cambia la escena del stage principal manteniendo pantalla completa
     * @param stage El stage actual
     * @param root El nuevo contenido
     * @param title El nuevo título
     */
    public static void changeSceneFullScreen(Stage stage, Parent root, String title) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        // No es necesario volver a maximizar si ya lo está
    }

    /**
     * Obtiene las dimensiones de la pantalla
     * @return Rectangle2D con las dimensiones
     */
    public static Rectangle2D getScreenBounds() {
        return Screen.getPrimary().getVisualBounds();
    }
}