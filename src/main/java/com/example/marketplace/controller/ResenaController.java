package com.example.marketplace.controller;

import com.example.marketplace.model.Producto;
import com.example.marketplace.model.Resena;
import com.example.marketplace.service.ProductoService;
import com.example.marketplace.service.ResenaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class ResenaController {

    @FXML private Label lblNombreProducto;
    @FXML private Label lblIdProducto;
    @FXML private Label lblCalificacion;
    @FXML private Label lblContador;
    @FXML private ImageView imgProducto;
    @FXML private TextArea txtComentario;
    @FXML private HBox contenedorEstrellas;
    @FXML private Button btnPublicar;
    @FXML private Button btnVolver;

    private ProductoService productoService;
    private ResenaService resenaService;
    private int idProducto;
    private int idPedido;
    private int calificacionSeleccionada = 0;
    private Label[] estrellas;

    public ResenaController() {
        this.productoService = new ProductoService();
        this.resenaService = new ResenaService();
    }

    @FXML
    public void initialize() {
        crearEstrellas();
        configurarContadorCaracteres();
    }

    /**
     * Configura el producto a reseñar
     */
    public void setProducto(int idProducto, int idPedido) {
        this.idProducto = idProducto;
        this.idPedido = idPedido;
        cargarDatosProducto();
    }

    /**
     * Crea las estrellas interactivas
     */
    private void crearEstrellas() {
        estrellas = new Label[5];
        contenedorEstrellas.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            final int estrella = i + 1;

            Label lblEstrella = new Label("★");
            lblEstrella.setStyle(
                    "-fx-font-size: 40px; " +
                            "-fx-text-fill: #dee2e6; " +
                            "-fx-cursor: hand;"
            );

            // Evento al pasar el mouse
            lblEstrella.setOnMouseEntered(e -> resaltarEstrellas(estrella));

            // Evento al hacer clic
            lblEstrella.setOnMouseClicked(e -> seleccionarCalificacion(estrella));

            estrellas[i] = lblEstrella;
            contenedorEstrellas.getChildren().add(lblEstrella);
        }

        // Evento al salir del contenedor
        contenedorEstrellas.setOnMouseExited(e -> {
            if (calificacionSeleccionada == 0) {
                resetearEstrellas();
            } else {
                resaltarEstrellas(calificacionSeleccionada);
            }
        });
    }

    /**
     * Resalta las estrellas hasta la posición indicada
     */
    private void resaltarEstrellas(int cantidad) {
        for (int i = 0; i < 5; i++) {
            if (i < cantidad) {
                estrellas[i].setStyle(
                        "-fx-font-size: 40px; " +
                                "-fx-text-fill: #ffc107; " +
                                "-fx-cursor: hand;"
                );
            } else {
                estrellas[i].setStyle(
                        "-fx-font-size: 40px; " +
                                "-fx-text-fill: #dee2e6; " +
                                "-fx-cursor: hand;"
                );
            }
        }
    }

    /**
     * Resetea las estrellas a su estado original
     */
    private void resetearEstrellas() {
        for (Label estrella : estrellas) {
            estrella.setStyle(
                    "-fx-font-size: 40px; " +
                            "-fx-text-fill: #dee2e6; " +
                            "-fx-cursor: hand;"
            );
        }
    }

    /**
     * Selecciona una calificación
     */
    private void seleccionarCalificacion(int cantidad) {
        calificacionSeleccionada = cantidad;
        resaltarEstrellas(cantidad);

        String[] textos = {
                "", // 0 no usado
                "Muy malo",
                "Malo",
                "Regular",
                "Bueno",
                "Excelente"
        };

        lblCalificacion.setText(textos[cantidad] + " (" + cantidad + " " +
                (cantidad == 1 ? "estrella" : "estrellas") + ")");
        lblCalificacion.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: #d98892; " +
                        "-fx-font-weight: 600;"
        );
    }

    /**
     * Configura el contador de caracteres
     */
    private void configurarContadorCaracteres() {
        txtComentario.textProperty().addListener((observable, oldValue, newValue) -> {
            int longitud = newValue.length();
            int maximo = 500;

            if (longitud > maximo) {
                txtComentario.setText(newValue.substring(0, maximo));
                longitud = maximo;
            }

            lblContador.setText(longitud + " / " + maximo + " caracteres");

            if (longitud >= maximo * 0.9) {
                lblContador.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c; -fx-font-weight: 600;");
            } else {
                lblContador.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
            }
        });
    }

    /**
     * Carga los datos del producto
     */
    private void cargarDatosProducto() {
        Producto producto = productoService.obtenerProductoPorId(idProducto);

        if (producto == null) {
            mostrarError("Error", "No se pudo cargar el producto");
            return;
        }

        lblNombreProducto.setText(producto.getNombre_producto());
        lblIdProducto.setText("ID: " + producto.getId_producto());

        // Cargar imagen del producto
        cargarImagen(producto);
    }

    /**
     * Carga la imagen del producto
     */
    private void cargarImagen(Producto producto) {
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
                imgProducto.setImage(imagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Publica la reseña
     */
    @FXML
    private void handlePublicar() {
        // Validar calificación
        if (calificacionSeleccionada == 0) {
            mostrarAdvertencia("Calificación requerida", "Por favor selecciona una calificación con estrellas");
            return;
        }

        // Validar comentario
        String comentario = txtComentario.getText().trim();
        if (comentario.isEmpty()) {
            mostrarAdvertencia("Comentario requerido", "Por favor escribe un comentario sobre el producto");
            return;
        }

        if (comentario.length() < 10) {
            mostrarAdvertencia("Comentario muy corto", "El comentario debe tener al menos 10 caracteres");
            return;
        }

        // Obtener usuario actual
        SessionManager session = SessionManager.getInstance();

        if (!session.isLoggedIn()) {
            mostrarError("Error", "Debes iniciar sesión para publicar una reseña");
            return;
        }

        // Crear la reseña
        Resena resena = resenaService.crearResena(
                session.getIdUsuarioActual(),
                idProducto,
                comentario,
                calificacionSeleccionada
        );

        if (resena != null) {
            mostrarExito("¡Reseña publicada!", "Tu reseña ha sido publicada exitosamente");
            volverADetallePedido();
        } else {
            mostrarError("Error", "No se pudo publicar la reseña. Es posible que ya hayas reseñado este producto.");
        }
    }

    /**
     * Vuelve al detalle del pedido
     */
    @FXML
    private void handleVolver() {
        volverADetallePedido();
    }

    /**
     * Navega de regreso al detalle del pedido
     */
    private void volverADetallePedido() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/DetallePedidoView.fxml"));
            Parent root = loader.load();

            DetallePedidoController controller = loader.getController();
            controller.cargarDetallePedido(idPedido);

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setTitle("Detalle del Pedido #" + idPedido);
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error", "No se pudo volver al detalle del pedido");
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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

    private void mostrarExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}