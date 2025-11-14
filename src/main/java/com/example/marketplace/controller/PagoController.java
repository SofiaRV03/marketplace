package com.example.marketplace.controller;

import com.example.marketplace.model.CarritoProducto;
import com.example.marketplace.model.Pago;
import com.example.marketplace.model.Producto;
import com.example.marketplace.service.CarritoService;
import com.example.marketplace.service.PagoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class PagoController {

    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtCodigoPostal;

    @FXML private RadioButton rbTarjeta;
    @FXML private RadioButton rbContraentrega;
    private ToggleGroup metodoPagoGroup;

    @FXML private VBox contenedorTarjeta;
    @FXML private TextField txtNumeroTarjeta;
    @FXML private TextField txtFechaCaducidad;
    @FXML private TextField txtCVV;

    @FXML private VBox lblMensajeContraentrega;
    @FXML private VBox resumenPedido;
    @FXML private Label lblSubtotal;
    @FXML private Label lblEnvio;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmarCompra;

    private CarritoService carritoService;
    private PagoService pagoService;
    private List<CarritoProducto> productosCarrito;
    private double subtotal;
    private double costoEnvio;

    public PagoController() {
        this.carritoService = new CarritoService();
        this.pagoService = new PagoService();
    }

    @FXML
    public void initialize() {
        try {
            // Configurar grupo de botones de radio
            metodoPagoGroup = new ToggleGroup();
            rbTarjeta.setToggleGroup(metodoPagoGroup);
            rbContraentrega.setToggleGroup(metodoPagoGroup);

            // Por defecto seleccionar tarjeta
            rbTarjeta.setSelected(true);

            // Agregar efectos hover a los botones
            configurarEfectosHover();

            // Listeners para mostrar/ocultar campos
            rbTarjeta.setOnAction(e -> mostrarCamposTarjeta());
            rbContraentrega.setOnAction(e -> mostrarMensajeContraentrega());

            // Formatear entrada de tarjeta
            txtNumeroTarjeta.textProperty().addListener((obs, old, newVal) -> {
                if (newVal != null && newVal.length() > 0) {
                    String limpio = newVal.replaceAll("[^0-9]", "");
                    if (limpio.length() > 16) {
                        limpio = limpio.substring(0, 16);
                    }
                    String formateado = formatearNumeroTarjeta(limpio);
                    if (!newVal.equals(formateado)) {
                        txtNumeroTarjeta.setText(formateado);
                    }
                }
            });

            // Formatear fecha de caducidad
            txtFechaCaducidad.textProperty().addListener((obs, old, newVal) -> {
                if (newVal != null && newVal.length() > 0) {
                    String limpio = newVal.replaceAll("[^0-9]", "");
                    if (limpio.length() > 4) {
                        limpio = limpio.substring(0, 4);
                    }
                    String formateado = formatearFecha(limpio);
                    if (!newVal.equals(formateado)) {
                        txtFechaCaducidad.setText(formateado);
                    }
                }
            });

            // Limitar CVV a 3-4 dígitos
            txtCVV.textProperty().addListener((obs, old, newVal) -> {
                if (newVal != null && newVal.length() > 4) {
                    txtCVV.setText(newVal.substring(0, 4));
                }
            });

            cargarDatosCarrito();
            mostrarCamposTarjeta();

        } catch (Exception e) {
            System.err.println("Error en initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarEfectosHover() {
        // Efecto hover para btnConfirmarCompra
        String estiloNormalConfirmar = "-fx-background-color: linear-gradient(to right, #e8a5aa, #d98892); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.4), 10, 0, 0, 3);";
        String estiloHoverConfirmar = "-fx-background-color: linear-gradient(to right, #c77783, #c77783); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.6), 12, 0, 0, 3);";

        btnConfirmarCompra.setOnMouseEntered(e -> btnConfirmarCompra.setStyle(estiloHoverConfirmar));
        btnConfirmarCompra.setOnMouseExited(e -> btnConfirmarCompra.setStyle(estiloNormalConfirmar));
    }

    private String formatearNumeroTarjeta(String numero) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numero.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                sb.append(" ");
            }
            sb.append(numero.charAt(i));
        }
        return sb.toString();
    }

    private String formatearFecha(String fecha) {
        if (fecha.length() <= 2) {
            return fecha;
        }
        return fecha.substring(0, 2) + "/" + fecha.substring(2);
    }

    private void mostrarCamposTarjeta() {
        contenedorTarjeta.setVisible(true);
        contenedorTarjeta.setManaged(true);
        lblMensajeContraentrega.setVisible(false);
        lblMensajeContraentrega.setManaged(false);
    }

    private void mostrarMensajeContraentrega() {
        contenedorTarjeta.setVisible(false);
        contenedorTarjeta.setManaged(false);
        lblMensajeContraentrega.setVisible(true);
        lblMensajeContraentrega.setManaged(true);
    }

    private void cargarDatosCarrito() {
        try {
            SessionManager session = SessionManager.getInstance();

            if (!session.isLoggedIn()) {
                System.err.println("Usuario no está logueado");
                return;
            }

            productosCarrito = carritoService.obtenerDetallesCarrito(session.getIdUsuarioActual());

            if (productosCarrito == null || productosCarrito.isEmpty()) {
                System.err.println("El carrito está vacío");
                lblSubtotal.setText("$0");
                lblEnvio.setText("$0");
                lblTotal.setText("$0");
                return;
            }

            subtotal = carritoService.obtenerTotalCarrito(session.getIdUsuarioActual());
            costoEnvio = pagoService.calcularCostoEnvio(subtotal);
            double total = subtotal + costoEnvio;

            // Limpiar resumen anterior
            resumenPedido.getChildren().clear();

            // Agregar productos al resumen
            for (CarritoProducto cp : productosCarrito) {
                try {
                    Producto producto = carritoService.buscarProductoPorId(cp.getid_producto());
                    if (producto != null) {
                        resumenPedido.getChildren().add(crearItemResumen(producto, cp));
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar producto: " + e.getMessage());
                }
            }

            // Actualizar labels de totales
            lblSubtotal.setText(String.format("$%,.0f", subtotal));
            lblEnvio.setText(String.format("$%,.0f", costoEnvio));
            lblTotal.setText(String.format("$%,.0f", total));

        } catch (Exception e) {
            System.err.println("Error al cargar datos del carrito: " + e.getMessage());
            e.printStackTrace();
            lblSubtotal.setText("$0");
            lblEnvio.setText("$0");
            lblTotal.setText("$0");
        }
    }

    private HBox crearItemResumen(Producto producto, CarritoProducto cp) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 0));

        // Imagen del producto
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
            // Imagen por defecto
        }

        // Info del producto
        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nombre = new Label(producto.getNombre_producto());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        Label cantidad = new Label("Cantidad: " + cp.getCantidad());
        cantidad.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        info.getChildren().addAll(nombre, cantidad);

        // Precio
        Label precio = new Label(String.format("$%,.0f", cp.getSubtotal()));
        precio.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        item.getChildren().addAll(imgView, info, precio);
        return item;
    }

    @FXML
    private void handleConfirmarCompra() {
        // Validar datos de envío
        if (!validarDatosEnvio()) {
            return;
        }

        // Validar método de pago
        if (rbTarjeta.isSelected()) {
            if (!validarDatosTarjeta()) {
                return;
            }
        }

        // Crear objeto Pago
        Pago pago = new Pago();
        pago.setNombre_completo(txtNombreCompleto.getText().trim());
        pago.setDireccion(txtDireccion.getText().trim());
        pago.setCiudad(txtCiudad.getText().trim());
        pago.setCodigo_postal(txtCodigoPostal.getText().trim());

        if (rbTarjeta.isSelected()) {
            pago.setMetodo_pago("Tarjeta de Crédito/Débito");
            pago.setNumero_tarjeta(txtNumeroTarjeta.getText().replaceAll("\\s", ""));
            pago.setFecha_caducidad(txtFechaCaducidad.getText());
            pago.setCvv(txtCVV.getText());
        } else {
            pago.setMetodo_pago("Contraentrega");
        }

        // Procesar el pago
        SessionManager session = SessionManager.getInstance();
        String numeroPedido = pagoService.procesarPagoCompleto(
                session.getIdUsuarioActual(),
                productosCarrito,
                pago,
                subtotal,
                costoEnvio
        );

        if (numeroPedido != null) {
            // Vaciar carrito
            carritoService.vaciarCarrito(session.getIdUsuarioActual());

            // Ir a confirmación
            irAConfirmacionPedido(numeroPedido, subtotal + costoEnvio);
        } else {
            mostrarError("Error", "No se pudo procesar el pago. Por favor, intenta nuevamente.");
        }
    }

    private boolean validarDatosEnvio() {
        if (txtNombreCompleto.getText().trim().isEmpty()) {
            mostrarAdvertencia("Campo requerido", "Ingresa tu nombre completo");
            txtNombreCompleto.requestFocus();
            return false;
        }

        if (txtDireccion.getText().trim().isEmpty()) {
            mostrarAdvertencia("Campo requerido", "Ingresa tu dirección");
            txtDireccion.requestFocus();
            return false;
        }

        if (txtCiudad.getText().trim().isEmpty()) {
            mostrarAdvertencia("Campo requerido", "Ingresa tu ciudad");
            txtCiudad.requestFocus();
            return false;
        }

        if (txtCodigoPostal.getText().trim().isEmpty()) {
            mostrarAdvertencia("Campo requerido", "Ingresa tu código postal");
            txtCodigoPostal.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarDatosTarjeta() {
        String numeroTarjeta = txtNumeroTarjeta.getText().replaceAll("\\s", "");

        if (numeroTarjeta.isEmpty() || numeroTarjeta.length() < 15) {
            mostrarAdvertencia("Tarjeta inválida", "Ingresa un número de tarjeta válido");
            txtNumeroTarjeta.requestFocus();
            return false;
        }

        if (txtFechaCaducidad.getText().isEmpty() || !txtFechaCaducidad.getText().matches("\\d{2}/\\d{2}")) {
            mostrarAdvertencia("Fecha inválida", "Ingresa la fecha en formato MM/AA");
            txtFechaCaducidad.requestFocus();
            return false;
        }

        if (txtCVV.getText().isEmpty() || txtCVV.getText().length() < 3) {
            mostrarAdvertencia("CVV inválido", "Ingresa un CVV válido (3-4 dígitos)");
            txtCVV.requestFocus();
            return false;
        }

        return true;
    }

    private void irAConfirmacionPedido(String numeroPedido, double total) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/ConfirmacionPedidoView.fxml"));
            Parent root = loader.load();

            ConfirmacionPedidoController controller = loader.getController();
            controller.setDatosPedido(numeroPedido, productosCarrito, subtotal, costoEnvio, total);

            Stage stage = (Stage) btnConfirmarCompra.getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setTitle("Pedido Confirmado");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar la confirmación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVolverCarrito() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/marketplace/CarritoView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnConfirmarCompra.getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setTitle("Carrito de Compras");
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError("Error", "No se pudo volver al carrito: " + e.getMessage());
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

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}