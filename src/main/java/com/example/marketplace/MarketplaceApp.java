package com.example.marketplace;

import com.example.marketplace.dao.InicializadorDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MarketplaceApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar la vista de Login al inicio
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();


            // Obtener dimensiones de la pantalla
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());


            primaryStage.setTitle("Marketplace");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/marketplace/images/icon.png")));
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Cambiar a true para permitir ajuste
            primaryStage.setMaximized(true); // Maximizar automáticamente
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error al cargar la aplicación:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== INICIANDO MARKETPLACE ===\n");

        // ⚠️ IMPORTANTE: Inicializar BD ANTES de lanzar JavaFX
        try {
            InicializadorDB.inicializar();
            System.out.println("\n=== Base de datos lista ===\n");
        } catch (Exception e) {
            System.err.println("❌ Error fatal al inicializar BD:");
            e.printStackTrace();
            System.exit(1);
        }

        launch(args);
    }
}