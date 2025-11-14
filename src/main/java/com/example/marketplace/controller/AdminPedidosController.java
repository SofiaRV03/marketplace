package com.example.marketplace.controller;

import com.example.marketplace.model.Pedido;
import com.example.marketplace.service.PedidoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminPedidosController {

    @FXML private Label lblAdministrador;
    @FXML private Label lblCantidadPedidos;
    @FXML private VBox contenedorPedidos;
    @FXML private VBox mensajeSinPedidos;
    @FXML private Button btnVolverPanel;
    @FXML private TextField txtBuscarUsuario;
    @FXML private Button btnRefrescar;
    @FXML private ToggleButton btnTodos;
    @FXML private ToggleButton btnPendiente;
    @FXML private ToggleButton btnProcesando;
    @FXML private ToggleButton btnEnviado;
    @FXML private ToggleButton btnEntregado;

    private PedidoService pedidoService;
    private ToggleGroup grupoFiltros;
    private List<Pedido> todosPedidos;
    private List<Pedido> pedidosFiltrados;

    public AdminPedidosController() {
        this.pedidoService = new PedidoService();
        this.todosPedidos = new ArrayList<>();
        this.pedidosFiltrados = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        SessionManager session = SessionManager.getInstance();

        lblAdministrador.setText(session.getNombreUsuarioActual());

        // Configurar grupo de filtros
        grupoFiltros = new ToggleGroup();
        btnTodos.setToggleGroup(grupoFiltros);
        btnPendiente.setToggleGroup(grupoFiltros);
        btnProcesando.setToggleGroup(grupoFiltros);
        btnEnviado.setToggleGroup(grupoFiltros);
        btnEntregado.setToggleGroup(grupoFiltros);

        // Listeners para los filtros
        btnTodos.setOnAction(e -> aplicarFiltros());
        btnPendiente.setOnAction(e -> aplicarFiltros());
        btnProcesando.setOnAction(e -> aplicarFiltros());
        btnEnviado.setOnAction(e -> aplicarFiltros());
        btnEntregado.setOnAction(e -> aplicarFiltros());

        // Estilos para los toggle buttons
        aplicarEstilosToggleButtons();

        // Configurar búsqueda automática
        configurarBusqueda();

        cargarTodosPedidos();
    }

    private void configurarBusqueda() {
        txtBuscarUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltros();
        });
    }

    private void aplicarEstilosToggleButtons() {
        String estiloBase = "-fx-background-color: white; -fx-text-fill: #6c757d; " +
                "-fx-border-color: #dee2e6; -fx-border-width: 1.5; -fx-border-radius: 16; " +
                "-fx-background-radius: 16; -fx-font-size: 12px; -fx-font-weight: 600; " +
                "-fx-cursor: hand; -fx-padding: 8 20;";

        String estiloSeleccionado = "-fx-background-color: linear-gradient(to right, #e8a5aa, #d98892);" +
                "-fx-text-fill: white; -fx-border-color: transparent; -fx-border-width: 0; " +
                "-fx-border-radius: 16; -fx-background-radius: 16; -fx-font-size: 12px; " +
                "-fx-font-weight: 600; -fx-cursor: hand; -fx-padding: 8 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 8, 0, 0, 2);";

        ToggleButton[] botones = {btnTodos, btnPendiente, btnProcesando, btnEnviado, btnEntregado};

        for (ToggleButton btn : botones) {
            btn.setStyle(estiloBase);
            btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    btn.setStyle(estiloSeleccionado);
                } else {
                    btn.setStyle(estiloBase);
                }
            });
        }

        btnTodos.setStyle(estiloSeleccionado);
    }

    private void cargarTodosPedidos() {
        try {
            todosPedidos = pedidoService.obtenerTodosPedidos();

            if (todosPedidos == null || todosPedidos.isEmpty()) {
                mostrarMensajeSinPedidos();
                return;
            }

            pedidosFiltrados = new ArrayList<>(todosPedidos);
            aplicarFiltros();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar el historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void aplicarFiltros() {
        String busqueda = txtBuscarUsuario.getText().trim().toLowerCase();

        // Primero aplicar filtro de búsqueda
        if (busqueda.isEmpty()) {
            pedidosFiltrados = new ArrayList<>(todosPedidos);
        } else {
            pedidosFiltrados = todosPedidos.stream()
                    .filter(p -> {
                        String nombreUsuario = p.getNombreUsuario() != null ?
                                p.getNombreUsuario().toLowerCase() : "";
                        String idUsuario = String.valueOf(p.getId_usuario());
                        return nombreUsuario.contains(busqueda) || idUsuario.contains(busqueda);
                    })
                    .collect(Collectors.toList());
        }

        // Luego aplicar filtro de estado
        aplicarFiltroEstado();
        mostrarPedidos();
    }

    private void aplicarFiltroEstado() {
        if (btnPendiente.isSelected()) {
            pedidosFiltrados = pedidosFiltrados.stream()
                    .filter(p -> "Pendiente".equals(p.getEstado()))
                    .collect(Collectors.toList());
        } else if (btnProcesando.isSelected()) {
            pedidosFiltrados = pedidosFiltrados.stream()
                    .filter(p -> "Procesando".equals(p.getEstado()))
                    .collect(Collectors.toList());
        } else if (btnEnviado.isSelected()) {
            pedidosFiltrados = pedidosFiltrados.stream()
                    .filter(p -> "Enviado".equals(p.getEstado()))
                    .collect(Collectors.toList());
        } else if (btnEntregado.isSelected()) {
            pedidosFiltrados = pedidosFiltrados.stream()
                    .filter(p -> "Entregado".equals(p.getEstado()))
                    .collect(Collectors.toList());
        }
    }

    private void mostrarPedidos() {
        if (pedidosFiltrados.isEmpty()) {
            mostrarMensajeSinPedidos();
            return;
        }

        lblCantidadPedidos.setText(pedidosFiltrados.size() +
                (pedidosFiltrados.size() == 1 ? " pedido encontrado" : " pedidos encontrados"));

        mensajeSinPedidos.setVisible(false);
        mensajeSinPedidos.setManaged(false);

        contenedorPedidos.getChildren().clear();

        for (Pedido pedido : pedidosFiltrados) {
            HBox tarjetaPedido = crearTarjetaPedido(pedido);
            contenedorPedidos.getChildren().add(tarjetaPedido);
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

        String nombreUsuario = pedido.getNombreUsuario() != null ? pedido.getNombreUsuario() : "Usuario";
        Label usuario = new Label("👤 Usuario: " + nombreUsuario + " (ID: " + pedido.getId_usuario() + ")");
        usuario.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: #495057;"
        );

        infoPedido.getChildren().addAll(numeroPedido, fecha, usuario);

        // Total y estado
        VBox estadoContainer = new VBox(8);
        estadoContainer.setAlignment(Pos.CENTER_RIGHT);
        estadoContainer.setPrefWidth(180);

        Label total = new Label(String.format("💰 $%.0f", pedido.getTotal()));
        total.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: 700; " +
                        "-fx-text-fill: #d98892;"
        );

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

        estadoContainer.getChildren().addAll(total, lblEstado, btnVerDetalle);

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
        return obtenerColorEstado(estado);
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
        lblCantidadPedidos.setText("0 pedidos encontrados");
        contenedorPedidos.getChildren().clear();
        mensajeSinPedidos.setVisible(true);
        mensajeSinPedidos.setManaged(true);
    }

    private void verDetallePedido(int idPedido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/AdminDetallePedidoView.fxml"));
            Parent root = loader.load();

            AdminDetallePedidoController controller = loader.getController();
            controller.cargarDetallePedido(idPedido);

            Stage stage = (Stage) btnVolverPanel.getScene().getWindow();
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
    private void handleRefrescar() {
        cargarTodosPedidos();
        txtBuscarUsuario.clear();
    }

    @FXML
    private void handleVolverPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverPanel.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Panel de Administración");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo volver al panel: " + e.getMessage());
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