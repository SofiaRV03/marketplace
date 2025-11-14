package com.example.marketplace.controller;

import com.example.marketplace.model.CarritoProducto;
import com.example.marketplace.model.Producto;
import com.example.marketplace.service.CarritoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ConfirmacionPedidoController {

    @FXML private Label lblNumeroPedido;
    @FXML private VBox listaProductos;
    @FXML private Label lblSubtotal;
    @FXML private Label lblEnvio;
    @FXML private Label lblTotal;
    @FXML private Button btnContinuarComprando;
    @FXML private Button btnVerHistorial;

    private CarritoService carritoService;

    public ConfirmacionPedidoController() {
        this.carritoService = new CarritoService();
    }

    @FXML
    public void initialize() {
        configurarEfectosHover();
    }

    private void configurarEfectosHover() {
        // Efecto hover para btnVerHistorial
        String estiloNormalHistorial = "-fx-background-color: linear-gradient(to right, #e8a5aa, #d98892); -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 600; -fx-padding: 12 25; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.4), 10, 0, 0, 3);";
        String estiloHoverHistorial = "-fx-background-color: linear-gradient(to right, #c77783, #c77783); -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 600; -fx-padding: 12 25; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.6), 12, 0, 0, 3);";

        btnVerHistorial.setOnMouseEntered(e -> btnVerHistorial.setStyle(estiloHoverHistorial));
        btnVerHistorial.setOnMouseExited(e -> btnVerHistorial.setStyle(estiloNormalHistorial));

        // Efecto hover para btnContinuarComprando
        String estiloNormalContinuar = "-fx-background-color: #f8f9fa; -fx-text-fill: #6c757d; -fx-font-size: 14px; -fx-font-weight: 600; -fx-padding: 12 25; -fx-background-radius: 10; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10; -fx-cursor: hand;";
        String estiloHoverContinuar = "-fx-background-color: #e9ecef; -fx-text-fill: #6c757d; -fx-font-size: 14px; -fx-font-weight: 600; -fx-padding: 12 25; -fx-background-radius: 10; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 10; -fx-cursor: hand;";

        btnContinuarComprando.setOnMouseEntered(e -> btnContinuarComprando.setStyle(estiloHoverContinuar));
        btnContinuarComprando.setOnMouseExited(e -> btnContinuarComprando.setStyle(estiloNormalContinuar));
    }

    public void setDatosPedido(String numeroPedido, List<CarritoProducto> productos,
                               double subtotal, double envio, double total) {
        lblNumeroPedido.setText(numeroPedido);

        // Cargar productos
        if (productos != null) {
            for (CarritoProducto cp : productos) {
                Producto producto = carritoService.buscarProductoPorId(cp.getid_producto());
                if (producto != null) {
                    listaProductos.getChildren().add(crearItemProducto(producto, cp));
                }
            }
        }

        // Establecer totales
        lblSubtotal.setText(String.format("$%,.0f", subtotal));
        lblEnvio.setText(String.format("$%,.0f", envio));
        lblTotal.setText(String.format("$%,.0f", total));
    }

    private HBox crearItemProducto(Producto producto, CarritoProducto cp) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15, 0, 15, 0));
        item.setStyle("-fx-border-color: #f0f0f0; -fx-border-width: 0 0 1 0;");

        // Imagen
        ImageView imgView = new ImageView();
        imgView.setFitWidth(60);
        imgView.setFitHeight(60);
        imgView.setPreserveRatio(true);

        try {
            if (producto.getImagen_producto() != null && !producto.getImagen_producto().isEmpty()) {
                String rutaImagen = producto.getImagen_producto();
                Image imagen;

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
                imgView.setImage(imagen);
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + e.getMessage());
        }

        // Información
        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nombre = new Label(producto.getNombre_producto());
        nombre.setStyle("-fx-font-size: 15px; -fx-font-weight: 600; -fx-text-fill: #2c3e50;");

        Label cantidad = new Label("(x" + cp.getCantidad() + ")");
        cantidad.setStyle("-fx-font-size: 13px; -fx-text-fill: #6c757d;");

        info.getChildren().addAll(nombre, cantidad);

        // Precio
        Label precio = new Label(String.format("$%,.0f", cp.getSubtotal()));
        precio.setStyle("-fx-font-size: 15px; -fx-font-weight: 600; -fx-text-fill: #2c3e50;");

        item.getChildren().addAll(imgView, info, precio);
        return item;
    }

    @FXML
    private void handleContinuarComprando() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnContinuarComprando.getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setTitle("Marketplace - Catálogo");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVerHistorial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/HistorialPedidosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVerHistorial.getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setTitle("Historial de Pedidos");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}