package com.example.marketplace.controller;

import com.example.marketplace.model.CarritoProducto;
import com.example.marketplace.model.Pedido;
import com.example.marketplace.model.Producto;
import com.example.marketplace.model.ProductoPedido;
import com.example.marketplace.service.CarritoService;
import com.example.marketplace.service.PedidoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CarritoController {

    @FXML private ListView<HBox> listaProductos;
    @FXML private Label lblTotal;
    @FXML private Label lblSubtotal;
    @FXML private Button btnFinalizarCompra;
    @FXML private Button btnVaciarCarrito;
    @FXML private Button btnVolverCatalogo;
    @FXML private Label lblUsuario;

    private CarritoService carritoService;
    private PedidoService pedidoService;

    public CarritoController() {
        this.carritoService = new CarritoService();
        this.pedidoService = new PedidoService();
    }

    @FXML
    public void initialize() {
        SessionManager session = SessionManager.getInstance();
        lblUsuario.setText(session.getNombreUsuarioActual());

        // Configurar ListView sin bordes ni fondo
        listaProductos.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        cargarCarrito();
    }

    private void cargarImagenPlaceholder(ImageView imageView, String texto) {
        try {
            Image placeholder = new Image("https://via.placeholder.com/180x180?text=" + texto, true);
            imageView.setImage(placeholder);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el placeholder");
        }
    }

    private void cargarCarrito() {
        SessionManager session = SessionManager.getInstance();

        if (!session.isLoggedIn()) {
            return;
        }

        try {
            listaProductos.getItems().clear();
            List<CarritoProducto> productosCarrito = carritoService.obtenerDetallesCarrito(
                    session.getIdUsuarioActual()
            );

            if (productosCarrito != null && !productosCarrito.isEmpty()) {
                for (CarritoProducto cp : productosCarrito) {
                    Producto producto = carritoService.buscarProductoPorId(cp.getid_producto());
                    if (producto != null) {
                        HBox tarjeta = crearTarjetaProducto(producto, cp);
                        listaProductos.getItems().add(tarjeta);
                    }
                }
            }

            actualizarTotal();
        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar el carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox crearTarjetaProducto(Producto producto, CarritoProducto cp) {
        HBox tarjeta = new HBox(20);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);"
        );
        tarjeta.setPrefHeight(120);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        VBox imgContainer = new VBox();
        imgContainer.setAlignment(Pos.CENTER);
        imgContainer.setPrefHeight(20);
        imgContainer.setMaxHeight(20);
        imgContainer.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);

        try {
            if (producto.getImagen_producto() != null && !producto.getImagen_producto().isEmpty()) {
                Image imagen;
                String rutaImagen = producto.getImagen_producto();

                if (rutaImagen.startsWith("http://") || rutaImagen.startsWith("https://")) {
                    imagen = new Image(rutaImagen, true);
                }
                else if (rutaImagen.startsWith("file:")) {
                    imagen = new Image(rutaImagen, true);
                }
                else {
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
                cargarImagenPlaceholder(imageView, "Sin+Imagen");
            }
        } catch (Exception e) {
            cargarImagenPlaceholder(imageView, "Error");
            System.err.println("Error cargando imagen: " + producto.getImagen_producto());
            e.printStackTrace();
        }

        imgContainer.getChildren().add(imageView);
        // Información del producto
        VBox info = new VBox(8);
        info.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nombre = new Label(producto.getNombre_producto());
        nombre.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: #2c3e50;"
        );

        Label precio = new Label(String.format("$%.0f", producto.getPrecio_producto()));
        precio.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-text-fill: #d98892; " +
                        "-fx-font-weight: 600;"
        );

        info.getChildren().addAll(nombre, precio);

        // Controles de cantidad
        HBox controlesCantidad = new HBox(10);
        controlesCantidad.setAlignment(Pos.CENTER);

        Button btnMenos = new Button("−");
        btnMenos.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-text-fill: #6c757d; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #e9ecef; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 32; " +
                        "-fx-pref-height: 32;"
        );
        btnMenos.setOnAction(e -> modificarCantidad(producto.getId_producto(), cp.getCantidad() - 1));

        Label lblCantidad = new Label(String.valueOf(cp.getCantidad()));
        lblCantidad.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: #2c3e50; " +
                        "-fx-min-width: 30; " +
                        "-fx-alignment: center;"
        );

        Button btnMas = new Button("+");
        btnMas.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-text-fill: #6c757d; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #e9ecef; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 32; " +
                        "-fx-pref-height: 32;"
        );
        btnMas.setOnAction(e -> modificarCantidad(producto.getId_producto(), cp.getCantidad() + 1));

        controlesCantidad.getChildren().addAll(btnMenos, lblCantidad, btnMas);

        // Botón eliminar
        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #e74c3c; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 32; " +
                        "-fx-pref-height: 32;"
        );
        btnEliminar.setOnAction(e -> eliminarProducto(producto));

        tarjeta.getChildren().addAll(imgContainer, info, controlesCantidad, btnEliminar);

        return tarjeta;
    }

    private void modificarCantidad(int idProducto, int nuevaCantidad) {
        // Si la cantidad es menor a 1, no hacer nada
        if (nuevaCantidad < 1) {
            return;
        }

        SessionManager session = SessionManager.getInstance();

        // Verificar stock disponible antes de actualizar
        Producto producto = carritoService.buscarProductoPorId(idProducto);

        if (producto == null) {
            mostrarError("Error", "Producto no encontrado");
            return;
        }

        // Validar que hay stock suficiente
        if (producto.getStock_producto() < nuevaCantidad) {
            mostrarAdvertencia(
                    "Stock insuficiente",
                    "Solo hay " + producto.getStock_producto() + " unidades disponibles"
            );
            return;
        }

        // Intentar actualizar la cantidad
        boolean actualizado = carritoService.actualizarCantidad(
                session.getIdUsuarioActual(),
                idProducto,
                nuevaCantidad
        );

        if (actualizado) {
            // Recargar el carrito para mostrar los cambios
            cargarCarrito();
        } else {
            mostrarError("Error", "No se pudo actualizar la cantidad");
        }
    }

    private void actualizarTotal() {
        SessionManager session = SessionManager.getInstance();
        double total = carritoService.obtenerTotalCarrito(session.getIdUsuarioActual());

        lblTotal.setText(String.format("$%.0f", total));
        lblSubtotal.setText(String.format("$%.0f", total));
    }

    private void eliminarProducto(Producto producto) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Producto");
        confirmacion.setHeaderText("¿Eliminar este producto del carrito?");
        confirmacion.setContentText(producto.getNombre_producto());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SessionManager session = SessionManager.getInstance();
                boolean eliminado = carritoService.eliminarProductoDelCarrito(
                        session.getIdUsuarioActual(),
                        producto.getId_producto()
                );

                if (eliminado) {
                    cargarCarrito();
                    mostrarInfo("Éxito", "Producto eliminado del carrito");
                } else {
                    mostrarError("Error", "No se pudo eliminar el producto");
                }
            }
        });
    }

    @FXML
    private void handleVaciarCarrito() {
        if (listaProductos.getItems().isEmpty()) {
            mostrarAdvertencia("Carrito vacío", "No hay productos en el carrito");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Vaciar Carrito");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("Se eliminarán todos los productos del carrito");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SessionManager session = SessionManager.getInstance();
                boolean vaciado = carritoService.vaciarCarrito(session.getIdUsuarioActual());

                if (vaciado) {
                    cargarCarrito();
                    mostrarInfo("Éxito", "Carrito vaciado correctamente");
                } else {
                    mostrarError("Error", "No se pudo vaciar el carrito");
                }
            }
        });
    }

    @FXML
    private void handleFinalizarCompra() {
        if (listaProductos.getItems().isEmpty()) {
            mostrarAdvertencia("Carrito vacío", "Agrega productos antes de finalizar la compra");
            return;
        }

        // Validar que haya stock suficiente antes de continuar
        SessionManager session = SessionManager.getInstance();
        List<CarritoProducto> productosCarrito = carritoService.obtenerDetallesCarrito(
                session.getIdUsuarioActual()
        );

        for (CarritoProducto cp : productosCarrito) {
            Producto producto = carritoService.buscarProductoPorId(cp.getid_producto());
            if (producto != null && producto.getStock_producto() < cp.getCantidad()) {
                mostrarError("Stock insuficiente",
                        String.format("El producto '%s' no tiene stock suficiente.\nDisponible: %d, Solicitado: %d",
                                producto.getNombre_producto(),
                                producto.getStock_producto(),
                                cp.getCantidad()));
                return;
            }
        }

        // Ir a la vista de pago
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/PagoView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnFinalizarCompra.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Finalizar Compra");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir la página de pago: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void irAHistorialPedidos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/HistorialPedidosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverCatalogo.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Historial de Pedidos");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir el historial: " + e.getMessage());
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
    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}