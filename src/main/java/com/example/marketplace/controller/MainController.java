package com.example.marketplace.controller;

import com.example.marketplace.model.Producto;
import com.example.marketplace.model.Categoria;
import com.example.marketplace.service.CategoriaService;
import com.example.marketplace.service.ProductoCategoriaService;
import com.example.marketplace.service.ProductoService;
import com.example.marketplace.service.CarritoService;
import com.example.marketplace.utils.MensajeInfoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML private VBox mainContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane productGrid;
    @FXML private Label statusLabel;
    @FXML private Label lblUsuario;
    @FXML private Button btnCarrito;
    @FXML private Button btnLogin;
    @FXML private Button btnCerrarSesion;
    @FXML private Label lblContadorCarrito;
    @FXML private Button btnPedidos;
    @FXML private Button btnNuevoProducto;
    @FXML private Button btnUsuarios;
    private Button btnCategoriaActual; // Para mantener referencia al botón activo
    private String categoriaActual = "Todo"; // Categoría seleccionada actualmente
    @FXML private Button btnCatTodo;
    @FXML private Button btnCatElectronica;
    @FXML private Button btnCatRopa;
    @FXML private Button btnCatHogar;
    @FXML private Button btnCatCocina;
    @FXML private Button btnCatDeporte;
    @FXML private Button btnCatJoyeria;
    @FXML private Button btnCatTaylorSwift;
    @FXML private Button btnCatInfantil;
    @FXML private Button btnCatMascotas;
    @FXML private Button btnCatMusica;
    @FXML private Button btnPedidosAdmin;
    @FXML private Button btnPerfil;

    private ProductoService productoService;
    private CarritoService carritoService;
    private CategoriaService categoriaService;
    private ProductoCategoriaService productoCategoriaService;
    private ObservableList<Producto> productos;
    private static final int COLUMNAS = 4;

    public MainController() {
        this.productoService = new ProductoService();
        this.carritoService = new CarritoService();
        this.categoriaService = new CategoriaService();
        this.productoCategoriaService = new ProductoCategoriaService();
        this.productos = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        System.out.println("=== CONTROLADOR INICIALIZADO ===");
        System.out.println("btnCarrito: " + btnCarrito);

        if (btnCarrito != null) {
            System.out.println("Asignando evento al botón carrito...");
            btnCarrito.setOnAction(event -> handleCarrito());
        } else {
            System.err.println("ERROR: btnCarrito es NULL!");
        }

        if (btnUsuarios != null) {
            btnUsuarios.setOnAction(event -> handleUsuarios());
        } else {
            System.err.println("ERROR: btnUsuarios es NULL!");
        }

        configurarGrid();
        actualizarUIUsuario();
        cargarProductos();
        actualizarContadorCarrito();

        // Configurar el botón "Todo" como inicial
        btnCategoriaActual = btnCatTodo;

        System.out.println("Botones de categoría configurados");
    }

    private void actualizarUIUsuario() {
        SessionManager session = SessionManager.getInstance();

        if (session.isLoggedIn()) {
            lblUsuario.setText(session.getNombreUsuarioActual());
            btnLogin.setVisible(false);
            btnLogin.setManaged(false);
            btnCerrarSesion.setVisible(true);
            btnCerrarSesion.setManaged(true);
            btnCarrito.setDisable(false);
            btnPedidos.setVisible(true);
            btnPedidos.setManaged(true);
            if(session.getUsuarioActual().getTipo().equals("admin")){
                btnNuevoProducto.setVisible(true);
                btnNuevoProducto.setManaged(true);
                btnCarrito.setVisible(false);
                btnCarrito.setManaged(false);
                btnUsuarios.setVisible(true);
                btnUsuarios.setManaged(true);
                btnPedidos.setVisible(false);
                btnPedidos.setManaged(false);
                btnPedidosAdmin.setVisible(true);
                btnPedidosAdmin.setManaged(true);
                btnPerfil.setDisable(false);




            } else if (session.getUsuarioActual().getTipo().equals("cliente")) {
                btnNuevoProducto.setVisible(false);
                btnNuevoProducto.setManaged(false);

            }
        } else {
            lblUsuario.setText("Invitado");
            btnLogin.setVisible(true);
            btnLogin.setManaged(true);
            btnCerrarSesion.setVisible(false);
            btnCerrarSesion.setManaged(false);
            btnCarrito.setDisable(false); // Lo dejamos habilitado pero redirige al login
            btnPedidos.setVisible(false);
            btnPedidos.setManaged(false);
            btnNuevoProducto.setDisable(false);
            btnNuevoProducto.setManaged(false);
            btnPerfil.setDisable(false);
        }
    }

    private void actualizarContadorCarrito() {
        SessionManager session = SessionManager.getInstance();

        if (session.isLoggedIn()) {
            int cantidadProductos = carritoService.contarProductosEnCarrito(session.getIdUsuarioActual());
            lblContadorCarrito.setText(String.valueOf(cantidadProductos));

            if (cantidadProductos > 0) {
                lblContadorCarrito.setVisible(true);
            } else {
                lblContadorCarrito.setVisible(false);
            }
        } else {
            lblContadorCarrito.setVisible(false);
        }
    }

    private void configurarGrid() {
        if (productGrid == null) {
            productGrid = new GridPane();
        }
        productGrid.setHgap(20); // AUMENTADO de 15 a 20
        productGrid.setVgap(20); // AUMENTADO de 15 a 20
        productGrid.setPadding(new Insets(20)); // REDUCIDO de 20 a 15

        if (scrollPane != null) {
            scrollPane.setContent(productGrid);
            scrollPane.setFitToWidth(true);
        }
    }

    private void cargarProductos() {
        try {
            statusLabel.setText("Cargando productos...");
            productos.clear();

            List<Producto> lista = productoService.obtenerProductos();
            productos.addAll(lista);

            mostrarProductosEnGrid();
            statusLabel.setText(productos.size() + " productos disponibles");
        } catch (Exception e) {
            mostrarError("Error al cargar productos", e.getMessage());
            statusLabel.setText("Error al cargar productos");
            e.printStackTrace();
        }
    }

    private void mostrarProductosEnGrid() {
        int columnIndex = 0;
        int rowIndex = 0;

        // Calcula dinámicamente las columnas según el ancho disponible
        scrollPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            int columnas = calcularColumnas(newWidth.doubleValue());
            reorganizarProductos(columnas);
        });

        // Carga inicial
        for (Producto producto : productos) {
            VBox tarjeta = crearTarjetaProducto(producto);
            productGrid.add(tarjeta, columnIndex, rowIndex);

            // Calcula columnas iniciales
            int maxColumns = calcularColumnas(scrollPane.getWidth());
            columnIndex++;
            if (columnIndex >= maxColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    private int calcularColumnas(double anchoPantalla) {
        double anchoTarjeta = 260; // REDUCIDO de 280 a 260
        double espacioHorizontal = 20; // AUMENTADO de 16 a 20 para mejor separación
        double padding = 40; // padding left + right del GridPane

        double anchoDisponible = anchoPantalla - padding;
        int columnas = (int) (anchoDisponible / (anchoTarjeta + espacioHorizontal));

        return Math.max(2, Math.min(columnas, 6)); // Mínimo 2, máximo 6 columnas
    }

    private void reorganizarProductos(int columnas) {
        productGrid.getChildren().clear();
        int columnIndex = 0;
        int rowIndex = 0;

        for (Producto producto : productos) {
            VBox tarjeta = crearTarjetaProducto(producto);
            productGrid.add(tarjeta, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex >= columnas) {
                columnIndex = 0;
                rowIndex++;
            }
        }
        }





    private VBox crearTarjetaProducto(Producto producto) {
        VBox tarjeta = new VBox(12);
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e8e8e8;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 16;" +
                        "-fx-cursor: hand;"
        );
        tarjeta.setPrefWidth(240); // AUMENTADO de 220 a 240
        tarjeta.setMaxWidth(240);  // AUMENTADO de 220 a 240
        tarjeta.setAlignment(Pos.TOP_CENTER);

        // Click en cualquier parte de la tarjeta muestra detalles
        SessionManager session = SessionManager.getInstance();
        if (session.isLoggedIn() && session.getUsuarioActual().getTipo().equals("cliente")){

            tarjeta.setOnMouseClicked(e -> {
                if (!(e.getTarget() instanceof Button)) {
                    mostrarDetallesProducto(producto);
                }
            });

        }



        // Contenedor de imagen
        VBox imgContainer = new VBox();
        imgContainer.setAlignment(Pos.CENTER);
        imgContainer.setPrefHeight(220); // AUMENTADO de 200 a 220
        imgContainer.setMaxHeight(220);
        imgContainer.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 8;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200); // AUMENTADO de 180 a 200
        imageView.setFitHeight(200); // AUMENTADO de 180 a 200
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

        // Nombre del producto
        Label lblNombre = new Label(producto.getNombre_producto());
        lblNombre.setWrapText(true);
        lblNombre.setMaxWidth(220); // AUMENTADO de 200 a 220
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: #1a1a1a;");
        lblNombre.setAlignment(Pos.CENTER_LEFT);

        // Precio
        Label lblPrecio = new Label(String.format("$%.2f", producto.getPrecio_producto()));
        lblPrecio.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: #2c2c2c;");

        // Stock
        Label lblStock = new Label("Stock: " + producto.getStock_producto());
        lblStock.setStyle("-fx-font-size: 12px; -fx-text-fill: #7a7a7a;");
        if (producto.getStock_producto() < 10) {
            lblStock.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c; -fx-font-weight: 600;");
        }

        // Espaciador flexible
        Region spacer = new Region();
        spacer.setPrefHeight(8);

        // Botón agregar al carrito
        Button btnAgregar = new Button("Agregar al Carrito");
        btnAgregar.setStyle(
                "-fx-background-color: #2c2c2c;" +
                        "-fx-text-fill: white;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-pref-width: 220;" + // AUMENTADO de 200 a 220
                        "-fx-pref-height: 36;"
        );
        btnAgregar.setOnAction(e -> {
            e.consume();
            agregarAlCarrito(producto);
        });

        // Hover del botón agregar
        btnAgregar.setOnMouseEntered(e -> {
            btnAgregar.setStyle(
                    "-fx-background-color: #1a1a1a;" +
                            "-fx-text-fill: white;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: 500;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-pref-width: 220;" +
                            "-fx-pref-height: 36;"
            );
        });

        btnAgregar.setOnMouseExited(e -> {
            btnAgregar.setStyle(
                    "-fx-background-color: #2c2c2c;" +
                            "-fx-text-fill: white;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: 500;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-pref-width: 220;" +
                            "-fx-pref-height: 36;"
            );
        });

        // Botones de administración
        HBox botonesAdmin = new HBox(8);
        botonesAdmin.setAlignment(Pos.CENTER);

        // Botón editar
        Button btnEditar = new Button("Editar");
        btnEditar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #4a4a4a;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-pref-height: 30;"
        );
        btnEditar.setOnAction(e -> {
            e.consume();
            editarProducto(producto);
        });

        // Botón eliminar
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #e74c3c;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-color: #e74c3c;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-pref-height: 30;"
        );
        btnEliminar.setOnAction(e -> {
            e.consume();
            eliminarProducto(producto);
        });

        // Hover botón editar
        btnEditar.setOnMouseEntered(e -> {
            btnEditar.setStyle(
                    "-fx-background-color: #4a4a4a;" +
                            "-fx-text-fill: white;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 12px;" +
                            "-fx-border-color: #4a4a4a;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-pref-height: 30;"
            );
        });

        btnEditar.setOnMouseExited(e -> {
            btnEditar.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #4a4a4a;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 12px;" +
                            "-fx-border-color: #d0d0d0;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-pref-height: 30;"
            );
        });

        // Hover botón eliminar
        btnEliminar.setOnMouseEntered(e -> {
            btnEliminar.setStyle(
                    "-fx-background-color: #e74c3c;" +
                            "-fx-text-fill: white;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 12px;" +
                            "-fx-border-color: #e74c3c;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-pref-height: 30;"
            );
        });

        btnEliminar.setOnMouseExited(e -> {
            btnEliminar.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #e74c3c;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 12px;" +
                            "-fx-border-color: #e74c3c;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-pref-height: 30;"
            );
        });

        botonesAdmin.getChildren().addAll(btnEditar, btnEliminar);

        // Agregar todos los elementos a la tarjeta
        tarjeta.getChildren().addAll(
                imgContainer,
                lblNombre,
                lblPrecio,
                lblStock,
                spacer,
                btnAgregar,
                botonesAdmin
        );

        // Efecto hover en la tarjeta
        tarjeta.setOnMouseEntered(e -> {
            tarjeta.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #2c2c2c;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 16;" +
                            "-fx-cursor: hand;"
            );
        });

        tarjeta.setOnMouseExited(e -> {
            tarjeta.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #e8e8e8;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 12;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 16;" +
                            "-fx-cursor: hand;"
            );
        });


        if (session.isLoggedIn()) {
            if (session.getUsuarioActual().getTipo().equals("admin")) {
                botonesAdmin.setVisible(true);
                botonesAdmin.setManaged(true);
                btnAgregar.setVisible(false);
                btnAgregar.setManaged(false);
            } else if (session.getUsuarioActual().getTipo().equals("cliente")) {
                botonesAdmin.setVisible(false);
                botonesAdmin.setManaged(false);
            }
        } else {
            botonesAdmin.setVisible(false);
            botonesAdmin.setManaged(false);
        }

        return tarjeta;
    }

    private void agregarAlCarrito(Producto producto) {
        SessionManager session = SessionManager.getInstance();

        if (!session.isLoggedIn()) {
            mostrarDialogoLogin();
            return;
        }

        // Mostrar diálogo de cantidad
        mostrarDialogoCantidad(producto);
    }

    private void mostrarDialogoLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MensajeLoginRequerido.fxml"));
            Parent root = loader.load();

            MensajeLoginRequeridoController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(new Scene(root));
            dialogStage.getScene().setFill(Color.TRANSPARENT);

            dialogStage.showAndWait();

            if (controller.debeIrALogin()) {
                irALogin();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error", "No se pudo cargar el diálogo");
        }
    }

    private void mostrarDialogoCantidad(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/MensajeCantidad.fxml"));
            Parent root = loader.load();

            MensajeCantidadController controller = loader.getController();
            controller.setProducto(producto.getNombre_producto(), producto.getStock_producto());

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(new Scene(root));
            dialogStage.getScene().setFill(Color.TRANSPARENT);

            dialogStage.showAndWait();

            if (controller.fueAgregado()) {
                SessionManager session = SessionManager.getInstance();
                int cantidad = controller.getCantidadSeleccionada();

                boolean agregado = carritoService.agregarProductoAlCarrito(
                        session.getIdUsuarioActual(),
                        producto.getId_producto(),
                        cantidad
                );

                if (agregado) {
                    MensajeInfoUtil.mostrarProductoAgregado(
                            producto.getNombre_producto(),
                            cantidad,
                            producto.getPrecio_producto() * cantidad
                    );
                    actualizarContadorCarrito();
                } else {
                    MensajeInfoUtil.mostrarError(
                            "Error",
                            "No se pudo agregar el producto al carrito"
                    );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error", "No se pudo cargar el diálogo");
        }
    }

    @FXML
    public void handleCarrito() {
        SessionManager session = SessionManager.getInstance();

        if (!session.isLoggedIn()) {
            if (!session.isLoggedIn()) {
                mostrarDialogoLogin();
                return;
            }
        }

        // Cambiar a la escena del carrito
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/CarritoView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnCarrito.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("🛒 Mi Carrito de Compras");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error al abrir carrito", "No se pudo abrir el carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        irALogin();
    }

    private void irALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogin.getScene().getWindow();

            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");

            // Centrar en la pantalla
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir el login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrarSesion() {
        SessionManager.getInstance().cerrarSesion();
        irALogin();
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText();

        if (searchText == null || searchText.trim().isEmpty()) {
            cargarProductos();
            return;
        }

        try {
            statusLabel.setText("Buscando: " + searchText);
            productGrid.getChildren().clear();
            productos.clear();

            List<Producto> resultados = productoService.buscarPorNombre(searchText);
            productos.addAll(resultados);

            int columnas = calcularColumnas(scrollPane.getWidth());
            reorganizarProductos(columnas);
            statusLabel.setText(productos.size() + " productos encontrados");
        } catch (Exception e) {
            mostrarError("Error en la búsqueda", e.getMessage());
            statusLabel.setText("Error en la búsqueda");
            e.printStackTrace();
        }
    }

//    @FXML
//    private void handleAddProduct() {
//        try {
//            Dialog<Producto> dialog = crearDialogoProducto(null);
//            dialog.showAndWait().ifPresent(producto -> {
//                Producto productoCreado = productoService.registrarProducto(
//                        producto.getNombre_producto(),
//                        producto.getDescripcion_producto(),
//                        producto.getPrecio_producto(),
//                        producto.getStock_producto(),
//                        producto.getImagen_producto(),
//                        new ArrayList<>()
//                );
//
//                if (productoCreado != null) {
//                    mostrarInfo("Éxito", "Producto creado correctamente");
//                    cargarProductos();
//                } else {
//                    mostrarError("Error", "No se pudo crear el producto");
//                }
//            });
//        } catch (Exception e) {
//            mostrarError("Error al crear producto", e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @FXML
    private void handleRefresh() {
        // Resetear a "Todo"
        if (!categoriaActual.equals("Todo")) {
            handleCategoriaTodo();
        } else {
            cargarProductos();
        }
        actualizarContadorCarrito();
    }

    private void editarProducto(Producto producto) {
        try {
            Dialog<Producto> dialog = crearDialogoProducto(producto);
            dialog.showAndWait().ifPresent(productoEditado -> {
                if (productoService.actualizarProducto(productoEditado)) {
                    mostrarInfo("Éxito", "Producto actualizado correctamente");
                    cargarProductos();
                } else {
                    mostrarError("Error", "No se pudo actualizar el producto");
                }
            });
        } catch (Exception e) {
            mostrarError("Error al actualizar producto", e.getMessage());
            e.printStackTrace();
        }
    }

    private void eliminarProducto(Producto producto) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro de eliminar este producto?");
        confirmacion.setContentText(producto.getNombre_producto());

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (productoService.eliminarProducto(producto.getId_producto())) {
                        mostrarInfo("Éxito", "Producto eliminado correctamente");
                        cargarProductos();
                    } else {
                        mostrarError("Error", "No se pudo eliminar el producto");
                    }
                } catch (Exception e) {
                    mostrarError("Error al eliminar producto", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void mostrarDetallesProducto(Producto producto) {
        if (producto == null) return;

        try {
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/marketplace/DetalleProductoView.fxml")
            );
            Parent root = loader.load();

            // Obtener el controlador y pasar el producto
            DetalleProductoController controller = loader.getController();
            controller.setProducto(producto);


            Stage stage = (Stage) btnCarrito.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Detalle del Producto - " + producto.getNombre_producto());
            stage.centerOnScreen();




        } catch (Exception e) {
            e.printStackTrace();
            // Fallback al diálogo original en caso de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar la vista de detalles");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }


// Método actualizado para crearDialogoProducto con selección de categorías

    private Dialog<Producto> crearDialogoProducto(Producto productoExistente) {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle(productoExistente == null ? "Nuevo Producto" : "Editar Producto");
        dialog.setHeaderText(productoExistente == null ? "Ingresa los datos del nuevo producto" : "Modifica los datos del producto");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Campos existentes
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");

        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Descripción detallada");
        txtDescripcion.setPrefRowCount(3);
        txtDescripcion.setWrapText(true);

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("0.00");

        TextField txtStock = new TextField();
        txtStock.setPromptText("0");

        TextField txtImagen = new TextField();
        txtImagen.setPromptText("URL de la imagen o nombre del archivo");
        txtImagen.setPrefWidth(250);

        Button btnSeleccionarImagen = new Button("📁 Seleccionar");
        btnSeleccionarImagen.setStyle("-fx-cursor: hand;");
        btnSeleccionarImagen.setOnAction(e -> {
            String imagenSeleccionada = seleccionarImagen(txtImagen.getText());
            if (imagenSeleccionada != null) {
                txtImagen.setText(imagenSeleccionada);
            }
        });

        HBox imagenBox = new HBox(5);
        imagenBox.getChildren().addAll(txtImagen, btnSeleccionarImagen);

        // ===== NUEVO: Sección de Categorías =====
        Label lblCategorias = new Label("Categorías:");
        lblCategorias.setStyle("-fx-font-weight: bold;");

        // Obtener todas las categorías disponibles
        List<String> categoriasDisponibles = categoriaService.obtenerNombreCategorias();

        // Contenedor para los checkboxes de categorías
        VBox categoriasContainer = new VBox(8);
        categoriasContainer.setStyle(
                "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 10; " +
                        "-fx-background-color: #fafafa;"
        );
        categoriasContainer.setMaxHeight(150);

        ScrollPane scrollCategorias = new ScrollPane(categoriasContainer);
        scrollCategorias.setFitToWidth(true);
        scrollCategorias.setMaxHeight(150);
        scrollCategorias.setStyle("-fx-background-color: transparent;");

        // Lista para mantener referencia a los checkboxes
        List<CheckBox> checkBoxesCategorias = new ArrayList<>();

        // Crear checkbox para cada categoría
        for (String categoria : categoriasDisponibles) {
            CheckBox checkBox = new CheckBox(categoria);
            checkBox.setStyle("-fx-font-size: 13px;");
            checkBoxesCategorias.add(checkBox);
            categoriasContainer.getChildren().add(checkBox);
        }

        // Si no hay categorías, mostrar mensaje
        if (categoriasDisponibles.isEmpty()) {
            Label lblSinCategorias = new Label("No hay categorías disponibles");
            lblSinCategorias.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            categoriasContainer.getChildren().add(lblSinCategorias);
        }

        // Cargar datos si es edición
        if (productoExistente != null) {
            txtNombre.setText(productoExistente.getNombre_producto());
            txtDescripcion.setText(productoExistente.getDescripcion_producto());
            txtPrecio.setText(String.valueOf(productoExistente.getPrecio_producto()));
            txtStock.setText(String.valueOf(productoExistente.getStock_producto()));
            txtImagen.setText(productoExistente.getImagen_producto());

            // Marcar las categorías del producto existente
            try {
                String categoriasProducto = productoService.obtenerCategoriasProducto(productoExistente.getId_producto());
                if (categoriasProducto != null && !categoriasProducto.isEmpty()) {
                    String[] categoriasArray = categoriasProducto.split(", ");
                    for (CheckBox checkBox : checkBoxesCategorias) {
                        for (String cat : categoriasArray) {
                            if (checkBox.getText().equals(cat.trim())) {
                                checkBox.setSelected(true);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al cargar categorías del producto: " + e.getMessage());
            }
        }

        // Agregar todos los campos al grid
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(txtDescripcion, 1, 1);
        grid.add(new Label("Precio:"), 0, 2);
        grid.add(txtPrecio, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(txtStock, 1, 3);
        grid.add(new Label("Imagen:"), 0, 4);
        grid.add(imagenBox, 1, 4);
        grid.add(lblCategorias, 0, 5);
        grid.add(scrollCategorias, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    // Validaciones
                    if (txtNombre.getText().trim().isEmpty()) {
                        mostrarError("Error de validación", "El nombre es requerido");
                        return null;
                    }

                    double precio = Double.parseDouble(txtPrecio.getText());
                    int stock = Integer.parseInt(txtStock.getText());

                    if (precio <= 0) {
                        mostrarError("Error de validación", "El precio debe ser mayor a 0");
                        return null;
                    }

                    if (stock < 0) {
                        mostrarError("Error de validación", "El stock no puede ser negativo");
                        return null;
                    }

                    // Obtener categorías seleccionadas
                    List<String> categoriasSeleccionadas = new ArrayList<>();
                    for (CheckBox checkBox : checkBoxesCategorias) {
                        if (checkBox.isSelected()) {
                            categoriasSeleccionadas.add(checkBox.getText());
                        }
                    }

                    List<Integer> idCategoriasSeleccionadas=new ArrayList<>();
                    for (String nombreCategoria : categoriasSeleccionadas) {
                        Categoria categoria = categoriaService.obtenerCategoriaPorNombre(nombreCategoria);
                        idCategoriasSeleccionadas.add(categoria.getId_categoria());
                    }





                    // Crear o actualizar producto
                    if (productoExistente != null) {
                        // Actualizar producto existente
                        productoExistente.setNombre_producto(txtNombre.getText());
                        productoExistente.setDescripcion_producto(txtDescripcion.getText());
                        productoExistente.setPrecio_producto(precio);
                        productoExistente.setStock_producto(stock);
                        productoExistente.setImagen_producto(txtImagen.getText());

                        // Actualizar categorías
                        if (!categoriasSeleccionadas.isEmpty()) {
                            productoCategoriaService.actualizarCategoriasDeProducto(
                                    productoExistente.getId_producto(),
                                    idCategoriasSeleccionadas
                            );
                        }

                        return productoExistente;
                    } else {
                        // Crear nuevo producto
                        Producto nuevoProducto = new Producto(
                                txtNombre.getText(),
                                txtDescripcion.getText(),
                                precio,
                                stock,
                                txtImagen.getText()
                        );

                        // Las categorías se manejarán en handleAddProduct
                        nuevoProducto.setCategorias(categoriasSeleccionadas);

                        return nuevoProducto;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error de formato", "Verifica que precio y stock sean números válidos");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    // ===== MÉTODO ACTUALIZADO handleAddProduct =====
    @FXML
    private void handleAddProduct() {
        try {
            Dialog<Producto> dialog = crearDialogoProducto(null);
            dialog.showAndWait().ifPresent(producto -> {
                // Obtener las categorías seleccionadas
                List<String> nombresCategoriasSeleccionadas = producto.getCategorias();

                // 2. Convierte los nombres a IDs consultando la base de datos
                List<Integer> idCategoriasSeleccionadas = new ArrayList<>();
                for (String nombreCategoria : nombresCategoriasSeleccionadas) {
                    Categoria categoria = categoriaService.obtenerCategoriaPorNombre(nombreCategoria);
                    if (categoria != null) {
                        idCategoriasSeleccionadas.add(categoria.getId_categoria());
                    }
                }

                // Paso 1: Crear el producto SIN categorías
                Producto productoCreado = productoService.registrarProducto(
                        producto.getNombre_producto(),
                        producto.getDescripcion_producto(),
                        producto.getPrecio_producto(),
                        producto.getStock_producto(),
                        producto.getImagen_producto(),
                        new ArrayList<>() // Lista vacía, no guardamos categorías aquí
                );

                if (productoCreado != null) {
                    // Paso 2: Asociar las categorías al producto en la tabla producto_categoria
                    if (nombresCategoriasSeleccionadas != null && !nombresCategoriasSeleccionadas.isEmpty()) {
                        boolean categoriasAsociadas = productoCategoriaService.actualizarCategoriasDeProducto(
                                productoCreado.getId_producto(),
                                idCategoriasSeleccionadas
                        );

                        if (categoriasAsociadas) {
                            mostrarInfo("Éxito", "Producto creado correctamente con sus categorías");
                        } else {
                            mostrarAdvertencia("Producto creado",
                                    "El producto fue creado pero hubo un problema al asociar las categorías");
                        }
                    } else {
                        mostrarInfo("Éxito", "Producto creado correctamente");
                    }

                    cargarProductos();
                } else {
                    mostrarError("Error", "No se pudo crear el producto");
                }
            });
        } catch (Exception e) {
            mostrarError("Error al crear producto", e.getMessage());
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

    private void cargarImagenPlaceholder(ImageView imageView, String texto) {
        try {
            Image placeholder = new Image("https://via.placeholder.com/180x180?text=" + texto, true);
            imageView.setImage(placeholder);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el placeholder");
        }
    }

    private String seleccionarImagen(String imagenActual) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Archivos de Imagen", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"
        );
        fileChooser.getExtensionFilters().add(imageFilter);

        if (imagenActual != null && !imagenActual.isEmpty() && !imagenActual.startsWith("http")) {
            try {
                File archivoActual = new File(imagenActual);
                if (archivoActual.exists()) {
                    fileChooser.setInitialDirectory(archivoActual.getParentFile());
                }
            } catch (Exception e) {
                // Usar directorio por defecto
            }
        }

        File archivoSeleccionado = fileChooser.showOpenDialog(new Stage());

        if (archivoSeleccionado != null) {
            try {
                Path destino = copiarImagenADirectorio(archivoSeleccionado);
                return destino.getFileName().toString();
            } catch (IOException e) {
                mostrarError("Error al copiar imagen", e.getMessage());
                e.printStackTrace();
                return "file:" + archivoSeleccionado.getAbsolutePath();
            }
        }

        return imagenActual;
    }

    private Path copiarImagenADirectorio(File archivo) throws IOException {
        Path directorioImagenes = Paths.get("imagenes_productos");
        if (!Files.exists(directorioImagenes)) {
            Files.createDirectories(directorioImagenes);
        }

        String nombreArchivo = archivo.getName();
        Path destino = directorioImagenes.resolve(nombreArchivo);

        int contador = 1;
        while (Files.exists(destino)) {
            String nombre = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.'));
            nombreArchivo = nombre + "_" + contador + extension;
            destino = directorioImagenes.resolve(nombreArchivo);
            contador++;
        }

        Files.copy(archivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

        return destino;
    }

    @FXML
    private void irAHistorialPedidos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/HistorialPedidosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnCarrito.getScene().getWindow();
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
    public void handleUsuarios() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/UsuariosCrudView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnUsuarios.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Usuarios");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error al abrir usuarios", "No se pudo abrir los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarBotonesCategorias() {
        // Buscar el VBox de categorías
        VBox categoriasContainer = (VBox) mainContainer.lookup("#categoriasContainer");

        if (categoriasContainer != null) {
            System.out.println("=== Configurando botones de categorías ===");

            // Iterar sobre los hijos del contenedor de categorías
            categoriasContainer.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    String textoCompleto = btn.getText();

                    System.out.println("Texto del botón: '" + textoCompleto + "'");

                    // Extraer el nombre de la categoría (remover emoji y espacios extra)
                    String categoria = textoCompleto.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "").trim();

                    System.out.println("Categoría extraída: '" + categoria + "'");

                    // Asignar evento de click
                    btn.setOnAction(e -> {
                        System.out.println("Click en categoría: " + categoria);
                        handleFiltroCategoria(btn, categoria);
                    });

                    // Agregar efectos hover
                    btn.setOnMouseEntered(e -> {
                        if (btn != btnCategoriaActual) {
                            btn.setStyle(
                                    "-fx-background-color: #f8f9fa;" +
                                            "-fx-text-fill: #2c3e50;" +
                                            "-fx-font-size: 13px;" +
                                            "-fx-font-weight: 500;" +
                                            "-fx-border-color: transparent;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-border-radius: 10;" +
                                            "-fx-background-radius: 10;" +
                                            "-fx-cursor: hand;" +
                                            "-fx-padding: 10 16;" +
                                            "-fx-alignment: CENTER-LEFT;"
                            );
                        }
                    });

                    btn.setOnMouseExited(e -> {
                        if (btn != btnCategoriaActual) {
                            btn.setStyle(
                                    "-fx-background-color: transparent;" +
                                            "-fx-text-fill: #6c757d;" +
                                            "-fx-font-size: 13px;" +
                                            "-fx-font-weight: 500;" +
                                            "-fx-border-color: transparent;" +
                                            "-fx-border-width: 1;" +
                                            "-fx-border-radius: 10;" +
                                            "-fx-background-radius: 10;" +
                                            "-fx-cursor: hand;" +
                                            "-fx-padding: 10 16;" +
                                            "-fx-alignment: CENTER-LEFT;"
                            );
                        }
                    });

                    // Si es "Todo", guardarlo como botón actual
                    if (categoria.equals("Todo")) {
                        btnCategoriaActual = btn;
                        System.out.println("Botón 'Todo' configurado como actual");
                    }
                }
            });

            System.out.println("=== Configuración de botones completada ===");
        } else {
            System.err.println("ERROR: No se encontró el contenedor de categorías (#categoriasContainer)");
        }
    }



    @FXML
    private void handleFiltroCategoria(Button botonClicked, String categoria) {
        // Restaurar estilo del botón anterior (estilo sidebar)
        if (btnCategoriaActual != null) {
            btnCategoriaActual.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #6c757d;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: 500;" +
                            "-fx-border-color: transparent;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 10 16;" +
                            "-fx-alignment: CENTER-LEFT;"
            );
        }

        // Aplicar estilo activo al botón clickeado (estilo sidebar)
        botonClicked.setStyle(
                "-fx-background-color: linear-gradient(to right, #e8a5aa, #d98892);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 16;" +
                        "-fx-alignment: CENTER-LEFT;"
        );

        // Actualizar botón actual
        btnCategoriaActual = botonClicked;
        categoriaActual = categoria;

        // Filtrar productos
        filtrarProductosPorCategoria(categoria);
    }

    private void filtrarProductosPorCategoria(String categoria) {
        try {
            System.out.println("=== Filtrando productos ===");
            System.out.println("Categoría seleccionada: '" + categoria + "'");

            statusLabel.setText("Filtrando por: " + categoria);
            productos.clear();

            List<Producto> resultados;

            if (categoria.equals("Todo")) {
                // Mostrar todos los productos
                System.out.println("Mostrando todos los productos");
                resultados = productoService.obtenerProductos();
            } else {
                // Filtrar por categoría específica
                System.out.println("Buscando productos de la categoría: " + categoria);
                resultados = productoService.obtenerProductosPorCategoria(categoria);
            }

            System.out.println("Productos encontrados: " + resultados.size());

            productos.addAll(resultados);

            // Reorganizar grid con los productos filtrados
            int columnas = calcularColumnas(scrollPane.getWidth());
            reorganizarProductos(columnas);

            // Actualizar mensaje de estado
            if (categoria.equals("Todo")) {
                statusLabel.setText(productos.size() + " productos disponibles");
            } else {
                statusLabel.setText(productos.size() + " productos en " + categoria);
            }

            System.out.println("=== Filtrado completado ===");

        } catch (Exception e) {
            System.err.println("ERROR en filtrarProductosPorCategoria:");
            e.printStackTrace();
            mostrarError("Error al filtrar productos", e.getMessage());
            statusLabel.setText("Error en el filtrado");
        }
    }

    @FXML
    private void handleCategoriaTodo() {
        System.out.println("Click en TODO");
        handleFiltroCategoria(btnCatTodo, "Todo");
    }

    @FXML
    private void handleCategoriaElectronica() {
        System.out.println("Click en Electrónica");
        handleFiltroCategoria(btnCatElectronica, "Electrónica");
    }

    @FXML
    private void handleCategoriaRopa() {
        System.out.println("Click en Ropa");
        handleFiltroCategoria(btnCatRopa, "Ropa");
    }

    @FXML
    private void handleCategoriaHogar() {
        System.out.println("Click en Hogar");
        handleFiltroCategoria(btnCatHogar, "Hogar");
    }

    @FXML
    private void handleCategoriaCocina() {
        System.out.println("Click en Cocina");
        handleFiltroCategoria(btnCatCocina, "Cocina");
    }

    @FXML
    private void handleCategoriaDeporte() {
        System.out.println("Click en Deporte");
        handleFiltroCategoria(btnCatDeporte, "Deporte");
    }

    @FXML
    private void handleCategoriaJoyeria() {
        System.out.println("Click en Joyería");
        handleFiltroCategoria(btnCatJoyeria, "Joyería");
    }

    @FXML
    private void handleCategoriaTaylorSwift() {
        System.out.println("Click en Taylor Swift");
        handleFiltroCategoria(btnCatTaylorSwift, "Taylor Swift");
    }

    @FXML
    private void handleCategoriaInfantil() {
        System.out.println("Click en Infantil");
        handleFiltroCategoria(btnCatInfantil, "Infantil");
    }

    @FXML
    private void handleCategoriaMascotas() {
        System.out.println("Click en Mascotas");
        handleFiltroCategoria(btnCatMascotas, "Mascotas");
    }

    @FXML
    private void handleCategoriaMusica() {
        System.out.println("Click en Musica");
        handleFiltroCategoria(btnCatMusica, "Musica");
    }

    @FXML
    private void irAPedidosAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/AdminPedidosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnCarrito.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Pedidos");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron abrir los pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePerfil() {
        SessionManager session = SessionManager.getInstance();

        if (!session.isLoggedIn()) {
            if (!session.isLoggedIn()) {
                mostrarDialogoLogin();
                return;
            }
        }

        // Cambiar a la escena del carrito
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/PerfilView.fxml"));
            Parent root = loader.load();

            PerfilController controller = loader.getController();
            controller.setUsuarioActual(session.getUsuarioActual());

            Stage stage = (Stage) btnCarrito.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Perfil");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error al abrir perfil", "No se pudo abrir el perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }






}