package com.example.marketplace.controller;

import com.example.marketplace.model.Usuario;
import com.example.marketplace.dao.UsuarioDAO;
import com.example.marketplace.utils.MensajeInfoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class UsuariosCrudController implements Initializable {

    @FXML private Label lblAdmin;
    @FXML private TextField txtBuscar;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lbClientes;
    @FXML private Label lbAdmin;
    @FXML private FlowPane contenedorUsuarios;
    @FXML private Button btnVolver;
    @FXML private Button btnRefrescar;

    private UsuarioDAO usuarioDAO;
    private ObservableList<Usuario> listaUsuarios;
    private FilteredList<Usuario> listaFiltrada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioDAO = new UsuarioDAO();
        cargarUsuarios();
        configurarBusqueda();
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            listaUsuarios = FXCollections.observableArrayList(usuarios);
            listaFiltrada = new FilteredList<>(listaUsuarios, p -> true);

            actualizarEstadisticas();
            mostrarUsuariosEnTarjetas();
        } catch (Exception e) {
            e.printStackTrace();
            MensajeInfoUtil.mostrarError("Error", "No se pudieron cargar los usuarios");
        }
    }

    private void mostrarUsuariosEnTarjetas() {
        contenedorUsuarios.getChildren().clear();

        for (Usuario usuario : listaFiltrada) {
            VBox tarjeta = crearTarjetaUsuario(usuario);
            contenedorUsuarios.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaUsuario(Usuario usuario) {
        VBox tarjeta = new VBox(16);
        tarjeta.setAlignment(Pos.TOP_LEFT);
        tarjeta.setPrefWidth(340);
        tarjeta.setMaxWidth(340);
        tarjeta.setPadding(new Insets(24));
        tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        );

        // Hover effect
        tarjeta.setOnMouseEntered(e -> tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 20, 0, 0, 8); " +
                        "-fx-cursor: hand;"
        ));

        tarjeta.setOnMouseExited(e -> tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);"
        ));

        // Header con avatar y badge
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        // Avatar
        VBox avatar = new VBox();
        avatar.setAlignment(Pos.CENTER);
        avatar.setPrefSize(60, 60);
        avatar.setMaxSize(60, 60);

        String tipoCliente = usuario.getTipo() != null ? usuario.getTipo() : "Cliente";
        boolean esAdmin = "Admin".equalsIgnoreCase(tipoCliente);

        avatar.setStyle(
                "-fx-background-color: " + (esAdmin ? "#fff3cd" : "#d1ecf1") + "; " +
                        "-fx-background-radius: 50;"
        );

        Label iconoAvatar = new Label(esAdmin ? "⭐" : "👤");
        iconoAvatar.setStyle("-fx-font-size: 28px;");
        avatar.getChildren().add(iconoAvatar);

        // Info nombre y badge
        VBox infoHeader = new VBox(6);
        infoHeader.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoHeader, javafx.scene.layout.Priority.ALWAYS);

        Label lblNombre = new Label(usuario.getNombre());
        lblNombre.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: 700; " +
                        "-fx-text-fill: #2c3e50;"
        );
        lblNombre.setWrapText(true);

        // Badge tipo
        Label badge = new Label(tipoCliente);
        badge.setAlignment(Pos.CENTER);
        badge.setPadding(new Insets(4, 12, 4, 12));

        if (esAdmin) {
            badge.setStyle(
                    "-fx-background-color: #fff3cd; " +
                            "-fx-text-fill: #856404; " +
                            "-fx-font-weight: 700; " +
                            "-fx-font-size: 11px; " +
                            "-fx-background-radius: 10;"
            );
            badge.setText("⭐ " + tipoCliente);
        } else {
            badge.setStyle(
                    "-fx-background-color: #d1ecf1; " +
                            "-fx-text-fill: #0c5460; " +
                            "-fx-font-weight: 600; " +
                            "-fx-font-size: 11px; " +
                            "-fx-background-radius: 10;"
            );
        }

        infoHeader.getChildren().addAll(lblNombre, badge);
        header.getChildren().addAll(avatar, infoHeader);

        // Separador
        Region separador = new Region();
        separador.setPrefHeight(1);
        separador.setStyle("-fx-background-color: #e9ecef;");

        // Información del usuario
        VBox infoUsuario = new VBox(10);

        // Email
        HBox emailBox = crearFilaInfo("📧", usuario.getCorreo());

        // Dirección
        HBox direccionBox = crearFilaInfo("📍",
                usuario.getDireccion() != null && !usuario.getDireccion().isEmpty()
                        ? usuario.getDireccion()
                        : "No especificada");

        // Fecha de registro
        String fechaRegistro = "No disponible";
        if (usuario.getFechaRegistro() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaRegistro = usuario.getFechaRegistro().format(formatter);
        }
        HBox fechaBox = crearFilaInfo("📅", "Registro: " + fechaRegistro);

        // ID
        HBox idBox = crearFilaInfo("🆔", "ID: " + usuario.getId());

        infoUsuario.getChildren().addAll(emailBox, direccionBox, fechaBox, idBox);

        // Botón de acción
        Button btnCambiarTipo = new Button("Cambiar Tipo de Usuario");
        btnCambiarTipo.setMaxWidth(Double.MAX_VALUE);
        btnCambiarTipo.setStyle(
                "-fx-background-color: #d98892; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 12 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 8, 0, 0, 2);"
        );

        btnCambiarTipo.setOnMouseEntered(e -> btnCambiarTipo.setStyle(
                "-fx-background-color: #c97782; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 12 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.5), 10, 0, 0, 3);"
        ));

        btnCambiarTipo.setOnMouseExited(e -> btnCambiarTipo.setStyle(
                "-fx-background-color: #d98892; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 12 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(217, 136, 146, 0.3), 8, 0, 0, 2);"
        ));

        btnCambiarTipo.setOnAction(e -> editarTipoCliente(usuario));

        tarjeta.getChildren().addAll(header, separador, infoUsuario, btnCambiarTipo);

        return tarjeta;
    }

    private HBox crearFilaInfo(String icono, String texto) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);

        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 16px;");

        Label lblTexto = new Label(texto);
        lblTexto.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: #495057;"
        );
        lblTexto.setWrapText(true);
        HBox.setHgrow(lblTexto, javafx.scene.layout.Priority.ALWAYS);

        fila.getChildren().addAll(lblIcono, lblTexto);
        return fila;
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(usuario -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (usuario.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (usuario.getCorreo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            mostrarUsuariosEnTarjetas();
        });
    }

    private void actualizarEstadisticas() {
        int total = listaUsuarios.size();
        int clientes = (int) listaUsuarios.stream()
                .filter(u -> "Cliente".equalsIgnoreCase(u.getTipo()) ||
                        u.getTipo() == null)
                .count();
        int admin = (int) listaUsuarios.stream()
                .filter(u -> "Admin".equalsIgnoreCase(u.getTipo()))
                .count();

        lblTotalUsuarios.setText(String.valueOf(total));
        lbClientes.setText(String.valueOf(clientes));
        lbAdmin.setText(String.valueOf(admin));
    }

    private void editarTipoCliente(Usuario usuario) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Editar Tipo de Cliente");
        dialog.setHeaderText("Usuario: " + usuario.getNombre());

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        ComboBox<String> comboTipoCliente = new ComboBox<>();
        comboTipoCliente.getItems().addAll("cliente", "admin");
        comboTipoCliente.setValue(usuario.getTipo() != null ?
                usuario.getTipo() : "cliente");
        comboTipoCliente.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 10; " +
                        "-fx-min-width: 250;"
        );

        Label lblInfo = new Label("Seleccione el nuevo tipo de cliente:");
        lblInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #6c757d;");

        VBox content = new VBox(15);
        content.getChildren().addAll(lblInfo, comboTipoCliente);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 15;"
        );

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return comboTipoCliente.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(nuevoTipo -> {
            try {
                usuario.setTipo(nuevoTipo);
                boolean actualizado = usuarioDAO.editarTipo(usuario, nuevoTipo);

                if (actualizado) {
                    actualizarEstadisticas();
                    mostrarUsuariosEnTarjetas();
                } else {
                    MensajeInfoUtil.mostrarError(
                            "Error",
                            "No se pudo actualizar el tipo de cliente"
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                MensajeInfoUtil.mostrarError(
                        "Error",
                        "Error al actualizar: " + e.getMessage()
                );
            }
        });
    }

    @FXML
    private void handleBuscar() {
        // La búsqueda se maneja automáticamente con el listener
    }

    @FXML
    private void handleRefrescar() {
        cargarUsuarios();
        MensajeInfoUtil.mostrarExito("Actualizado", "La lista de usuarios se ha actualizado");
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

    public void setAdminNombre(String nombre) {
        lblAdmin.setText(nombre);
    }
}