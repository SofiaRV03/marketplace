package com.example.marketplace.controller;

import com.example.marketplace.model.Pedido;
import com.example.marketplace.model.Producto;
import com.example.marketplace.service.PedidoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetallePedidoController {

    @FXML private Label lblNumeroPedido;
    @FXML private Label lblFechaPedido;
    @FXML private Label lblEstadoPedido;
    @FXML private Label lblTotalPedido;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotal;
    @FXML private VBox contenedorProductos;
    @FXML private Button btnVolverHistorial;
    @FXML private Button btnVolverCatalogo;

    private PedidoService pedidoService;
    private int idPedidoActual;
    private String estadoPedidoActual;

    public DetallePedidoController() {
        this.pedidoService = new PedidoService();
    }

    @FXML
    public void initialize() {
        // La carga se hace desde el método público cargarDetallePedido
    }

    public void cargarDetallePedido(int idPedido) {
        this.idPedidoActual = idPedido;

        try {
            // Obtener información del pedido
            Pedido pedido = pedidoService.obtenerDetallePedido(idPedido);

            if (pedido == null) {
                mostrarError("Error", "No se encontró el pedido");
                handleVolverHistorial();
                return;
            }

            // Guardar el estado del pedido
            this.estadoPedidoActual = pedido.getEstado();

            // Mostrar información del pedido
            lblNumeroPedido.setText("Pedido #" + pedido.getId_pedido());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            lblFechaPedido.setText("Fecha: " + pedido.getFecha_pedido().format(formatter));

            lblEstadoPedido.setText(pedido.getEstado());
            aplicarEstiloEstado(pedido.getEstado());

            lblTotalPedido.setText(String.format("Total: $%.0f", pedido.getTotal()));
            lblSubtotal.setText(String.format("$%.0f", pedido.getTotal()));
            lblTotal.setText(String.format("$%.0f", pedido.getTotal()));

            // Cargar productos del pedido
            cargarProductosPedido(idPedido);

        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar el detalle del pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarProductosPedido(int idPedido) {
        try {
            List<Producto> productos = pedidoService.obtenerDetalleProductosPedido(idPedido);

            if (productos == null || productos.isEmpty()) {
                Label lblSinProductos = new Label("No se encontraron productos en este pedido");
                lblSinProductos.setStyle("-fx-text-fill: #6c757d; -fx-font-style: italic;");
                contenedorProductos.getChildren().add(lblSinProductos);
                return;
            }

            contenedorProductos.getChildren().clear();

            for (Producto producto : productos) {
                HBox tarjetaProducto = crearTarjetaProducto(producto);
                contenedorProductos.getChildren().add(tarjetaProducto);
            }

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox crearTarjetaProducto(Producto producto) {
        HBox tarjeta = new HBox(20);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);"
        );
        tarjeta.setPrefHeight(120);

        // Imagen del producto
        VBox imgContainer = new VBox();
        imgContainer.setAlignment(Pos.CENTER);
        imgContainer.setPrefSize(80, 80);
        imgContainer.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        imageView.setPreserveRatio(true);

        try {
            if (producto.getImagen_producto() != null && !producto.getImagen_producto().isEmpty()) {
                Image imagen;
                String rutaImagen = producto.getImagen_producto();

                if (rutaImagen.startsWith("http://") || rutaImagen.startsWith("https://")) {
                    imagen = new Image(rutaImagen, true);
                } else if (rutaImagen.startsWith("file:")) {
                    imagen = new Image(rutaImagen, true);
                } else {
                    File archivoExterno = new File("imagenes_productos", rutaImagen);
                    if (archivoExterno.exists()) {
                        imagen = new Image(archivoExterno.toURI().toString());
                    } else {
                        String recurso = "/com/example/marketplace/images/" + rutaImagen;
                        imagen = new Image(getClass().getResourceAsStream(recurso));
                    }
                }
                imageView.setImage(imagen);
            } else {
                cargarImagenPlaceholder(imageView);
            }
        } catch (Exception e) {
            cargarImagenPlaceholder(imageView);
        }

        imgContainer.getChildren().add(imageView);

        // Información del producto
        VBox infoProducto = new VBox(8);
        infoProducto.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoProducto, Priority.ALWAYS);

        Label nombre = new Label(producto.getNombre_producto());
        nombre.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: #2c3e50;"
        );
        nombre.setWrapText(true);

        Label descripcion = new Label(producto.getDescripcion_producto());
        descripcion.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: #6c757d;"
        );
        descripcion.setWrapText(true);
        descripcion.setMaxWidth(400);

        infoProducto.getChildren().addAll(nombre, descripcion);

        // Cantidad y precio
        VBox precioContainer = new VBox(6);
        precioContainer.setAlignment(Pos.CENTER_RIGHT);
        precioContainer.setPrefWidth(200);

        Label cantidad = new Label("Cantidad: " + producto.getStock_producto());
        cantidad.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: #6c757d; " +
                        "-fx-font-weight: 600;"
        );

        Label precioUnitario = new Label(String.format("$%.0f c/u", producto.getPrecio_producto()));
        precioUnitario.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: #2c3e50;"
        );

        Label subtotal = new Label(String.format("Subtotal: $%.0f",
                producto.getPrecio_producto() * producto.getStock_producto()));
        subtotal.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: 700; " +
                        "-fx-text-fill: #d98892;"
        );

        // Botón de reseña (solo si el pedido está entregado)
        if ("Entregado".equalsIgnoreCase(estadoPedidoActual)) {
            Button btnResena = new Button("⭐ Hacer Reseña");
            btnResena.setStyle(
                    "-fx-background-color: #fff3cd; " +
                            "-fx-text-fill: #856404; " +
                            "-fx-font-size: 12px; " +
                            "-fx-font-weight: 600; " +
                            "-fx-border-color: #ffc107; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-radius: 8; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 6 16; " +
                            "-fx-pref-width: 140;"
            );

            // Efectos hover
            btnResena.setOnMouseEntered(e -> btnResena.setStyle(
                    "-fx-background-color: #ffc107; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 12px; " +
                            "-fx-font-weight: 600; " +
                            "-fx-border-color: #ffc107; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-radius: 8; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 6 16; " +
                            "-fx-pref-width: 140;"
            ));

            btnResena.setOnMouseExited(e -> btnResena.setStyle(
                    "-fx-background-color: #fff3cd; " +
                            "-fx-text-fill: #856404; " +
                            "-fx-font-size: 12px; " +
                            "-fx-font-weight: 600; " +
                            "-fx-border-color: #ffc107; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-radius: 8; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 6 16; " +
                            "-fx-pref-width: 140;"
            ));

            btnResena.setOnAction(e -> abrirVistaResena(producto.getId_producto()));

            precioContainer.getChildren().addAll(cantidad, precioUnitario, subtotal, btnResena);
        } else {
            precioContainer.getChildren().addAll(cantidad, precioUnitario, subtotal);
        }

        tarjeta.getChildren().addAll(imgContainer, infoProducto, precioContainer);

        return tarjeta;
    }

    private void abrirVistaResena(int idProducto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/ResenaView.fxml"));
            Parent root = loader.load();

            ResenaController controller = loader.getController();
            controller.setProducto(idProducto, idPedidoActual);

            Stage stage = (Stage) btnVolverHistorial.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Hacer Reseña");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir la vista de reseña: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void aplicarEstiloEstado(String estado) {
        String colorFondo = obtenerColorFondoEstado(estado);
        String colorTexto = obtenerColorTextoEstado(estado);

        lblEstadoPedido.setStyle(
                "-fx-background-color: " + colorFondo + "; " +
                        "-fx-text-fill: " + colorTexto + "; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20;"
        );
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

    private void cargarImagenPlaceholder(ImageView imageView) {
        try {
            Image placeholder = new Image("https://via.placeholder.com/70x70?text=Producto", true);
            imageView.setImage(placeholder);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el placeholder");
        }
    }

    @FXML
    private void handleVolverHistorial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/HistorialPedidosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverHistorial.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Historial de Pedidos");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo volver al historial: " + e.getMessage());
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