package com.example.marketplace.controller;

import com.example.marketplace.model.Pedido;
import com.example.marketplace.service.PedidoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialPedidosController {

    @FXML private Label lblUsuario;
    @FXML private Label lblCantidadPedidos;
    @FXML private VBox contenedorPedidos;
    @FXML private VBox mensajeSinPedidos;
    @FXML private Button btnVolverCatalogo;

    private PedidoService pedidoService;

    public HistorialPedidosController() {
        this.pedidoService = new PedidoService();
    }

    @FXML
    public void initialize() {
        SessionManager session = SessionManager.getInstance();

        if (!session.isLoggedIn()) {
            mostrarError("Error", "Debes iniciar sesión para ver el historial");
            handleVolverCatalogo();
            return;
        }

        lblUsuario.setText(session.getNombreUsuarioActual());
        cargarHistorialPedidos();
    }

    private void cargarHistorialPedidos() {
        SessionManager session = SessionManager.getInstance();

        try {
            List<Pedido> pedidos = pedidoService.obtenerHistorialPedidos(session.getIdUsuarioActual());

            if (pedidos == null || pedidos.isEmpty()) {
                mostrarMensajeSinPedidos();
                return;
            }

            // Actualizar contador
            lblCantidadPedidos.setText("Tienes " + pedidos.size() +
                    (pedidos.size() == 1 ? " pedido realizado" : " pedidos realizados"));

            // Ocultar mensaje de sin pedidos
            mensajeSinPedidos.setVisible(false);
            mensajeSinPedidos.setManaged(false);

            // Limpiar contenedor
            contenedorPedidos.getChildren().clear();

            // Crear tarjeta para cada pedido
            for (Pedido pedido : pedidos) {
                HBox tarjetaPedido = crearTarjetaPedido(pedido);
                contenedorPedidos.getChildren().add(tarjetaPedido);
            }

        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar el historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox crearTarjetaPedido(Pedido pedido) {
        HBox tarjeta = new HBox(20);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 24; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2); " +
                        "-fx-cursor: hand;"
        );
        tarjeta.setPrefHeight(100);

        // Icono del pedido
        VBox iconoContainer = new VBox();
        iconoContainer.setAlignment(Pos.CENTER);
        iconoContainer.setPrefWidth(60);
        iconoContainer.setStyle(
                "-fx-background-color: " + obtenerColorEstado(pedido.getEstado()) + "; " +
                        "-fx-background-radius: 12;"
        );

        Label icono = new Label(obtenerIconoEstado(pedido.getEstado()));
        icono.setStyle("-fx-font-size: 32px;");
        iconoContainer.getChildren().add(icono);

        // Información del pedido
        VBox infoPedido = new VBox(8);
        infoPedido.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoPedido, Priority.ALWAYS);

        Label numeroPedido = new Label("Pedido #" + pedido.getId_pedido());
        numeroPedido.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: 700; " +
                        "-fx-text-fill: #2c3e50;"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Label fecha = new Label("📅 " + pedido.getFecha_pedido().format(formatter));
        fecha.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: #6c757d;"
        );

        Label total = new Label(String.format("💰 Total: $%.0f", pedido.getTotal()));
        total.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: #d98892;"
        );

        infoPedido.getChildren().addAll(numeroPedido, fecha, total);

        // Estado del pedido
        VBox estadoContainer = new VBox(8);
        estadoContainer.setAlignment(Pos.CENTER_RIGHT);
        estadoContainer.setPrefWidth(150);

        Label lblEstado = new Label(pedido.getEstado());
        lblEstado.setStyle(
                "-fx-background-color: " + obtenerColorFondoEstado(pedido.getEstado()) + "; " +
                        "-fx-text-fill: " + obtenerColorTextoEstado(pedido.getEstado()) + "; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-padding: 6 16; " +
                        "-fx-background-radius: 16;"
        );

        Button btnVerDetalle = new Button("Ver Detalle");
        btnVerDetalle.setStyle(
                "-fx-background-color: linear-gradient(to right, #e8a5aa, #d98892); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-background-radius: 16; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 6 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 8, 0, 0, 2);"
        );
        btnVerDetalle.setOnAction(e -> verDetallePedido(pedido.getId_pedido()));

        estadoContainer.getChildren().addAll(lblEstado, btnVerDetalle);

        tarjeta.getChildren().addAll(iconoContainer, infoPedido, estadoContainer);

        // Efecto hover
        tarjeta.setOnMouseEntered(e -> {
            tarjeta.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 12; " +
                            "-fx-padding: 24; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 15, 0, 0, 4); " +
                            "-fx-cursor: hand; " +
                            "-fx-border-color: #d98892; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 12;"
            );
        });

        tarjeta.setOnMouseExited(e -> {
            tarjeta.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 12; " +
                            "-fx-padding: 24; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2); " +
                            "-fx-cursor: hand;"
            );
        });

        // Click en la tarjeta abre el detalle
        tarjeta.setOnMouseClicked(e -> {
            if (!(e.getTarget() instanceof Button)) {
                verDetallePedido(pedido.getId_pedido());
            }
        });

        return tarjeta;
    }

    private String obtenerIconoEstado(String estado) {
        switch (estado) {
            case "Pendiente": return "⏳";
            case "Procesando": return "📦";
            case "Enviado": return "🚚";
            case "Entregado": return "✅";
            case "Cancelado": return "❌";
            default: return "📋";
        }
    }

    private String obtenerColorEstado(String estado) {
        switch (estado) {
            case "Pendiente": return "#fff3cd";
            case "Procesando": return "#cfe2ff";
            case "Enviado": return "#d1e7dd";
            case "Entregado": return "#d1f2eb";
            case "Cancelado": return "#f8d7da";
            default: return "#e9ecef";
        }
    }

    private String obtenerColorFondoEstado(String estado) {
        switch (estado) {
            case "Pendiente": return "#fff3cd";
            case "Procesando": return "#cfe2ff";
            case "Enviado": return "#d1e7dd";
            case "Entregado": return "#d1f2eb";
            case "Cancelado": return "#f8d7da";
            default: return "#e9ecef";
        }
    }

    private String obtenerColorTextoEstado(String estado) {
        switch (estado) {
            case "Pendiente": return "#856404";
            case "Procesando": return "#084298";
            case "Enviado": return "#0f5132";
            case "Entregado": return "#0a3622";
            case "Cancelado": return "#842029";
            default: return "#495057";
        }
    }

    private void mostrarMensajeSinPedidos() {
        lblCantidadPedidos.setText("No tienes pedidos realizados");
        contenedorPedidos.getChildren().clear();
        mensajeSinPedidos.setVisible(true);
        mensajeSinPedidos.setManaged(true);
    }

    private void verDetallePedido(int idPedido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/DetallePedidoView.fxml"));
            Parent root = loader.load();

            // Pasar el ID del pedido al controller
            DetallePedidoController controller = loader.getController();
            controller.cargarDetallePedido(idPedido);

            Stage stage = (Stage) btnVolverCatalogo.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Detalle del Pedido #" + idPedido);
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir el detalle del pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVolverCatalogo() {
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
            mostrarError("Error", "No se pudo volver al catálogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}