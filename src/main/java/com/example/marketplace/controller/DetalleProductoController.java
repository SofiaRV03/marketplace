package com.example.marketplace.controller;

import com.example.marketplace.model.Producto;
import com.example.marketplace.model.Resena;
import com.example.marketplace.service.ProductoService;
import com.example.marketplace.service.CarritoService;
import com.example.marketplace.service.ResenaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DetalleProductoController implements Initializable {

    @FXML private Label lblNombre;
    @FXML private Label lblId;
    @FXML private Label lblPrecio;
    @FXML private Label lblStock;
    @FXML private Label lblDescripcion;
    @FXML private Label lblCategorias;
    @FXML private ImageView imgProducto;
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarCarrito;
    @FXML private HBox contenedorMiniaturas;
    @FXML private HBox contenedorColores;
    @FXML private HBox contenedorCantidad;
    @FXML private ScrollPane scrollPaneContenido;

    private Producto producto;
    private ProductoService productoService;
    private ResenaService resenaService;
    private int cantidadSeleccionada = 1;
    private Label lblCantidad;
    private CarritoService carritoService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productoService = new ProductoService();
        resenaService = new ResenaService();
        carritoService = new CarritoService();
        configurarEfectosHover();
    }

    private void configurarEfectosHover() {
        // Efecto hover en botón volver
        btnVolver.setOnMouseEntered(e -> btnVolver.setStyle(
                "-fx-background-color: #d98892;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-border-color: #d98892;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 20;"
        ));

        btnVolver.setOnMouseExited(e -> btnVolver.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #d98892;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-border-color: #d98892;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 20;"
        ));

        // Efecto hover en botón agregar al carrito
        btnAgregarCarrito.setOnMouseEntered(e -> {
            if (!btnAgregarCarrito.isDisabled()) {
                btnAgregarCarrito.setStyle(
                        "-fx-background-color: #c97782;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 15px;" +
                                "-fx-font-weight: 600;" +
                                "-fx-background-radius: 12;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 14 32;" +
                                "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.5), 16, 0, 0, 6);"
                );
            }
        });

        btnAgregarCarrito.setOnMouseExited(e -> {
            if (!btnAgregarCarrito.isDisabled()) {
                btnAgregarCarrito.setStyle(
                        "-fx-background-color: #d98892;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 15px;" +
                                "-fx-font-weight: 600;" +
                                "-fx-background-radius: 12;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 14 32;" +
                                "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 12, 0, 0, 4);"
                );
            }
        });
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        cargarDatosProducto();
        cargarResenasProducto();
    }

    private void cargarDatosProducto() {
        if (producto == null) {
            System.out.println("Error: producto es null");
            return;
        }

        try {
            // Información básica
            lblNombre.setText(producto.getNombre_producto() != null ?
                    producto.getNombre_producto() : "Producto sin nombre");

            lblId.setText("ID: " + producto.getId_producto());

            lblPrecio.setText(String.format("$%,.0f", producto.getPrecio_producto()));

            // Descripción
            String descripcion = producto.getDescripcion_producto();
            if (descripcion != null && !descripcion.trim().isEmpty()) {
                lblDescripcion.setText(descripcion);
                lblDescripcion.setStyle(
                        "-fx-font-size: 13px; " +
                                "-fx-text-fill: #495057; " +
                                "-fx-line-spacing: 4px;"
                );
            } else {
                lblDescripcion.setText("Sin descripción disponible");
                lblDescripcion.setStyle(
                        "-fx-font-size: 13px; " +
                                "-fx-text-fill: #adb5bd; " +
                                "-fx-line-spacing: 4px;" +
                                "-fx-font-style: italic;"
                );
            }

            // Configurar spinner de cantidad según el stock
            configurarSelectorCantidad();

            // Stock con indicador visual
            int stock = producto.getStock_producto();
            if (stock == 0) {
                lblStock.setText("Sin stock disponible");
                lblStock.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #dc3545;");
                btnAgregarCarrito.setDisable(true);
                contenedorCantidad.setDisable(true);
                contenedorCantidad.setOpacity(0.5);
                btnAgregarCarrito.setText("🚫  Producto Agotado");
                btnAgregarCarrito.setStyle(
                        "-fx-background-color: #6c757d;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 15px;" +
                                "-fx-font-weight: 600;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 14 32;" +
                                "-fx-opacity: 0.6;"
                );
            } else if (stock < 10) {
                lblStock.setText("¡Solo " + stock + " unidades disponibles!");
                lblStock.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #ffc107;");
            } else {
                lblStock.setText(stock + " unidades en stock");
                lblStock.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #155724;");
            }

            // Cargar imagen del producto
            cargarImagenProducto();

            // Cargar categorías
            cargarCategorias();

        } catch (Exception e) {
            System.err.println("Error al cargar datos del producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarResenasProducto() {
        try {
            // Obtener reseñas del producto
            List<Resena> resenas = resenaService.obtenerResenasPorProducto(producto.getId_producto());
            System.out.println(resenas);
            double promedioCalificacion = resenaService.obtenerPromedioCalificacion(producto.getId_producto());
            int totalResenas = resenas.size();

            // Buscar el contenedor principal dentro del ScrollPane
            VBox contenidoPrincipal = (VBox) scrollPaneContenido.getContent();
            if (contenidoPrincipal == null) {
                return;
            }

            // Crear sección de reseñas
            VBox seccionResenas = crearSeccionResenas(resenas, promedioCalificacion, totalResenas);

            // Agregar la sección de reseñas al final
            contenidoPrincipal.getChildren().add(seccionResenas);

        } catch (Exception e) {
            System.err.println("Error al cargar reseñas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox crearSeccionResenas(List<Resena> resenas, double promedio, int total) {
        VBox seccion = new VBox(20);
        seccion.setMaxWidth(1200);
        seccion.setAlignment(Pos.CENTER);
        VBox.setMargin(seccion, new Insets(30, 0, 0, 0));

        // Título y estadísticas
        VBox headerResenas = new VBox(10);
        headerResenas.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-padding: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);"
        );

        Label tituloResenas = new Label("Reseñas de Clientes");
        tituloResenas.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: 700;" +
                        "-fx-text-fill: #2c3e50;"
        );

        // Mostrar promedio y total
        HBox estadisticas = new HBox(15);
        estadisticas.setAlignment(Pos.CENTER_LEFT);

        Label lblPromedio = new Label(String.format("%.1f de 5 estrellas", promedio));
        lblPromedio.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-text-fill: #d98892;"
        );

        // Estrellas visuales
        HBox estrellasPromedio = crearEstrellasVisuales(promedio);

        Label lblTotal = new Label("(" + total + " " + (total == 1 ? "reseña" : "reseñas") + ")");
        lblTotal.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #6c757d;"
        );

        estadisticas.getChildren().addAll(lblPromedio, estrellasPromedio, lblTotal);
        headerResenas.getChildren().addAll(tituloResenas, estadisticas);

        seccion.getChildren().add(headerResenas);

        // Lista de reseñas
        if (resenas.isEmpty()) {
            VBox sinResenas = new VBox(10);
            sinResenas.setAlignment(Pos.CENTER);
            sinResenas.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 16;" +
                            "-fx-padding: 40;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);"
            );

            Label lblSinResenas = new Label("📝");
            lblSinResenas.setStyle("-fx-font-size: 40px;");

            Label lblMensaje = new Label("Aún no hay reseñas para este producto");
            lblMensaje.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #6c757d;" +
                            "-fx-font-style: italic;"
            );

            sinResenas.getChildren().addAll(lblSinResenas, lblMensaje);
            seccion.getChildren().add(sinResenas);
        } else {
            VBox listaResenas = new VBox(15);

            for (Resena resena : resenas) {
                VBox tarjetaResena = crearTarjetaResena(resena);
                listaResenas.getChildren().add(tarjetaResena);
            }

            seccion.getChildren().add(listaResenas);
        }

        return seccion;
    }

    private VBox crearTarjetaResena(Resena resena) {
        VBox tarjeta = new VBox(12);
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);"
        );

        // Header con nombre, fecha y estrellas
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox infoUsuario = new VBox(4);
        HBox.setHgrow(infoUsuario, Priority.ALWAYS);

        Label lblUsuario = new Label(resena.getNombreUsuario());
        lblUsuario.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: 700;" +
                        "-fx-text-fill: #2c3e50;"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Label lblFecha = new Label(resena.getFecha_resena().format(formatter));
        lblFecha.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #6c757d;"
        );

        infoUsuario.getChildren().addAll(lblUsuario, lblFecha);

        // Estrellas de calificación
        HBox estrellas = crearEstrellasVisuales(resena.getCalificacion());

        header.getChildren().addAll(infoUsuario, estrellas);

        // Comentario
        Label lblComentario = new Label(resena.getComentario());
        lblComentario.setWrapText(true);
        lblComentario.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #495057;" +
                        "-fx-line-spacing: 2px;"
        );

        tarjeta.getChildren().addAll(header, lblComentario);

        return tarjeta;
    }

    private HBox crearEstrellasVisuales(double calificacion) {
        HBox contenedor = new HBox(2);
        contenedor.setAlignment(Pos.CENTER_LEFT);

        for (int i = 1; i <= 5; i++) {
            Label estrella = new Label("★");

            if (i <= calificacion) {
                // Estrella completa
                estrella.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffc107;");
            } else if (i - 0.5 <= calificacion) {
                // Media estrella (aproximación visual)
                estrella.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffc107; -fx-opacity: 0.5;");
            } else {
                // Estrella vacía
                estrella.setStyle("-fx-font-size: 16px; -fx-text-fill: #dee2e6;");
            }

            contenedor.getChildren().add(estrella);
        }

        return contenedor;
    }

    private void configurarSelectorCantidad() {
        int stockDisponible = producto.getStock_producto();

        if (stockDisponible > 0) {
            // Limpiar el contenedor
            contenedorCantidad.getChildren().clear();

            // Botón decrementar
            Button btnMenos = new Button("−");
            btnMenos.setPrefSize(36, 36);
            btnMenos.setStyle(
                    "-fx-background-color: #f8f9fa;" +
                            "-fx-text-fill: #2c3e50;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: transparent;" +
                            "-fx-cursor: hand;"
            );

            // Label cantidad
            lblCantidad = new Label(String.valueOf(cantidadSeleccionada));
            lblCantidad.setPrefWidth(60);
            lblCantidad.setAlignment(javafx.geometry.Pos.CENTER);
            lblCantidad.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-font-weight: 600;" +
                            "-fx-text-fill: #2c3e50;"
            );

            // Botón incrementar
            Button btnMas = new Button("+");
            btnMas.setPrefSize(36, 36);
            btnMas.setStyle(
                    "-fx-background-color: #d98892;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: transparent;" +
                            "-fx-cursor: hand;"
            );

            // Efectos hover
            btnMenos.setOnMouseEntered(e -> btnMenos.setStyle(
                    "-fx-background-color: #e9ecef;" +
                            "-fx-text-fill: #2c3e50;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: transparent;" +
                            "-fx-cursor: hand;"
            ));

            btnMenos.setOnMouseExited(e -> btnMenos.setStyle(
                    "-fx-background-color: #f8f9fa;" +
                            "-fx-text-fill: #2c3e50;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: transparent;" +
                            "-fx-cursor: hand;"
            ));

            btnMas.setOnMouseEntered(e -> btnMas.setStyle(
                    "-fx-background-color: #c97782;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: transparent;" +
                            "-fx-cursor: hand;"
            ));

            btnMas.setOnMouseExited(e -> btnMas.setStyle(
                    "-fx-background-color: #d98892;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: transparent;" +
                            "-fx-cursor: hand;"
            ));

            // Acciones de los botones
            btnMenos.setOnAction(e -> {
                if (cantidadSeleccionada > 1) {
                    cantidadSeleccionada--;
                    lblCantidad.setText(String.valueOf(cantidadSeleccionada));
                }
            });

            btnMas.setOnAction(e -> {
                int maxCantidad = Math.min(stockDisponible, 99);
                if (cantidadSeleccionada < maxCantidad) {
                    cantidadSeleccionada++;
                    lblCantidad.setText(String.valueOf(cantidadSeleccionada));
                }
            });

            contenedorCantidad.getChildren().addAll(btnMenos, lblCantidad, btnMas);

        } else {
            contenedorCantidad.setDisable(true);
            contenedorCantidad.setOpacity(0.5);
        }
    }

    private void cargarImagenProducto() {
        try {
            if (producto.getImagen_producto() != null && !producto.getImagen_producto().isEmpty()) {
                Image imagen;
                String rutaImagen = producto.getImagen_producto();

                if (rutaImagen.startsWith("http://") || rutaImagen.startsWith("https://")) {
                    imagen = new Image(rutaImagen, true);
                } else if (rutaImagen.startsWith("file:")) {
                    imagen = new Image(rutaImagen, true);
                } else {
                    java.io.File archivoExterno = new java.io.File("imagenes_productos", rutaImagen);
                    if (archivoExterno.exists()) {
                        imagen = new Image(archivoExterno.toURI().toString());
                    } else {
                        String recurso = "/com/example/marketplace/images/" + rutaImagen;
                        InputStream imageStream = getClass().getResourceAsStream(recurso);

                        if (imageStream != null) {
                            imagen = new Image(imageStream);
                        } else {
                            imageStream = getClass().getResourceAsStream(
                                    "/com/example/marketplace/images/productos/" + producto.getId_producto() + ".png"
                            );

                            if (imageStream != null) {
                                imagen = new Image(imageStream);
                            } else {
                                cargarImagenPlaceholder("Sin+Imagen");
                                return;
                            }
                        }
                    }
                }

                if (!imagen.isError()) {
                    imgProducto.setImage(imagen);
                } else {
                    cargarImagenPlaceholder("Error");
                }
            } else {
                cargarImagenPlaceholder("Sin+Imagen");
            }
        } catch (Exception e) {
            cargarImagenPlaceholder("Error");
            System.err.println("Error cargando imagen: " + producto.getImagen_producto());
            e.printStackTrace();
        }
    }

    private void cargarImagenPlaceholder(String mensaje) {
        try {
            String placeholderUrl = "https://via.placeholder.com/320x320/f0f0f0/999999?text=" + mensaje;
            Image placeholderImage = new Image(placeholderUrl, true);
            imgProducto.setImage(placeholderImage);
        } catch (Exception e) {
            System.err.println("Error al cargar placeholder: " + e.getMessage());
        }
    }

    private void cargarCategorias() {
        try {
            String categorias = productoService.obtenerCategoriasProducto(producto.getId_producto());

            if (categorias != null && !categorias.trim().isEmpty() && !categorias.equals("Sin categoría")) {
                lblCategorias.setText(categorias);
                lblCategorias.setStyle(
                        "-fx-font-size: 12px; " +
                                "-fx-text-fill: #495057; " +
                                "-fx-font-weight: 500;"
                );
            } else {
                lblCategorias.setText("Sin categorías asignadas");
                lblCategorias.setStyle(
                        "-fx-font-size: 12px; " +
                                "-fx-text-fill: #6c757d; " +
                                "-fx-font-weight: 500; " +
                                "-fx-font-style: italic;"
                );
            }
        } catch (Exception e) {
            lblCategorias.setText("No disponible");
            lblCategorias.setStyle(
                    "-fx-font-size: 12px; " +
                            "-fx-text-fill: #6c757d; " +
                            "-fx-font-weight: 500;"
            );
            System.err.println("Error al cargar categorías: " + e.getMessage());
        }
    }

    @FXML
    private void handleVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolver.getScene().getWindow();
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

    @FXML
    private void handleAgregarCarrito() {
        if (producto != null && producto.getStock_producto() > 0) {

            System.out.println("=== Agregando al carrito ===");
            System.out.println("Producto: " + producto.getNombre_producto());
            System.out.println("Cantidad: " + cantidadSeleccionada);
            System.out.println("Precio unitario: $" + String.format("%,.0f", producto.getPrecio_producto()));
            System.out.println("Total: $" + String.format("%,.0f", producto.getPrecio_producto() * cantidadSeleccionada));
            System.out.println("===========================");

            SessionManager session = SessionManager.getInstance();
            boolean agregado = carritoService.agregarProductoAlCarrito(
                    session.getIdUsuarioActual(),
                    producto.getId_producto(),
                    cantidadSeleccionada
            );

            mostrarConfirmacion();
        } else {
            System.out.println("No se puede agregar el producto al carrito (sin stock)");
        }
    }

    private void mostrarConfirmacion() {
        String textoOriginal = btnAgregarCarrito.getText();
        btnAgregarCarrito.setText("✓ Agregado al carrito");
        btnAgregarCarrito.setStyle(
                "-fx-background-color: #28a745;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 14 32;"
        );

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> {
                    btnAgregarCarrito.setText(textoOriginal);
                    btnAgregarCarrito.setStyle(
                            "-fx-background-color: #d98892;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 15px;" +
                                    "-fx-font-weight: 600;" +
                                    "-fx-background-radius: 12;" +
                                    "-fx-cursor: hand;" +
                                    "-fx-padding: 14 32;" +
                                    "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 12, 0, 0, 4);"
                    );
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}