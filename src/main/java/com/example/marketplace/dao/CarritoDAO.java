package com.example.marketplace.dao;


import com.example.marketplace.model.Carrito;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAO {

    // Crear un nuevo carrito
    public Carrito crearCarrito(Carrito carrito) {
        String sql = "INSERT INTO carrito (id_usuario, fecha_creacion, estado) VALUES (?, ?, ?)";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            pstmt.setInt(1, carrito.getid_usuario());
            pstmt.setTimestamp(2, Timestamp.valueOf(carrito.getfecha_creacion()));
            pstmt.setString(3, carrito.getEstado());

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        carrito.setId_carrito(idGenerado);
                    }
                }
                return carrito;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Buscar carrito activo de un usuario
    public Carrito buscarCarritoActivoPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM carrito WHERE id_usuario = ? AND estado = 'activo' ORDER BY fecha_creacion DESC LIMIT 1";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Carrito(
                        rs.getInt("id_carrito"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getInt("id_usuario"),
                        rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Buscar carrito por ID
    public Carrito buscarPorId(int idCarrito) {
        String sql = "SELECT * FROM carrito WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Carrito(
                        rs.getInt("id_carrito"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getInt("id_usuario"),
                        rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Listar todos los carritos de un usuario
    public List<Carrito> listarCarritosPorUsuario(int idUsuario) {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE id_usuario = ? ORDER BY fecha_creacion DESC";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Carrito carrito = new Carrito(
                        rs.getInt("id_carrito"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getInt("id_usuario"),
                        rs.getString("estado")
                );
                lista.add(carrito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Actualizar estado del carrito
    public boolean actualizarEstado(int idCarrito, String nuevoEstado) {
        String sql = "UPDATE carrito SET estado = ? WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idCarrito);

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar carrito
    public boolean eliminarCarrito(int idCarrito) {
        String sql = "DELETE FROM carrito WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar carritos por usuario y estado
    public List<Carrito> listarCarritosPorUsuarioYEstado(int idUsuario, String estado) {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE id_usuario = ? AND estado = ? ORDER BY fecha_creacion DESC";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, estado);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Carrito carrito = new Carrito(
                        rs.getInt("id_carrito"),
                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                        rs.getInt("id_usuario"),
                        rs.getString("estado")
                );
                lista.add(carrito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}