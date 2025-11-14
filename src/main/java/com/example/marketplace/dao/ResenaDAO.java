package com.example.marketplace.dao;

import com.example.marketplace.model.Resena;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ResenaDAO {

    // Crear una nueva reseña
    public Resena crearResena(Resena resena) {
        String sql = "INSERT INTO resena (id_usuario,id_producto, comentario, calificacion, fecha_resena) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, resena.getId_usuario());
            pstmt.setInt(2, resena.getId_producto());
            pstmt.setString(3, resena.getComentario());
            pstmt.setInt(4, resena.getCalificacion());
            pstmt.setTimestamp(5, Timestamp.valueOf(resena.getFecha_resena()));

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    resena.setId_resena(rs.getInt(1));
                }
                return resena;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para mapear reseña
    private Resena mapearResena(ResultSet rs) throws SQLException {
        long fechaMillis = rs.getLong("fecha_resena");
        LocalDateTime fecha = Instant.ofEpochMilli(fechaMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Resena r = new Resena(
                rs.getInt("id_resena"),
                rs.getInt("id_usuario"),
                rs.getInt("id_producto"),
                rs.getString("comentario"),
                rs.getInt("calificacion"),
                fecha
        );

        try {
            String nombreUsuario = rs.getString("nombre_usuario");
            if (nombreUsuario != null) r.setNombreUsuario(nombreUsuario);
        } catch (SQLException ignored) {}

        try {
            String nombreProducto = rs.getString("nombre_producto");
            if (nombreProducto != null) r.setNombreProducto(nombreProducto);
        } catch (SQLException ignored) {}

        return r;
    }

    // Listar reseñas por producto con usuario (como en tu primer código)
    public List<Resena> listarResenasPorProducto(int idProducto) {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.nombre as nombre_usuario " +
                "FROM resena r " +
                "INNER JOIN usuario u ON r.id_usuario = u.id_usuario " +
                "WHERE r.id_producto = ? " +
                "ORDER BY r.fecha_resena DESC";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                lista.add(mapearResena(rs));
            }
            if (!hayResultados) {
                System.out.println(">> La consulta no devolvió ninguna fila");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(lista);
        return lista;

    }

    // Verificar si un usuario ya reseñó un producto
    public boolean usuarioYaResenoProducto(int idUsuario, int idProducto) {
        String sql = "SELECT COUNT(*) FROM resena WHERE id_usuario = ? AND id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idProducto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Obtener calificación promedio
    public double obtenerPromedioCalificacion(int idProducto) {
        String sql = "SELECT AVG(calificacion) as promedio FROM resena WHERE id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return rs.getDouble("promedio");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Contar reseñas
    public int contarResenas(int idProducto) {
        String sql = "SELECT COUNT(*) FROM resena WHERE id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Buscar reseña por ID
    public Resena buscarPorId(int idResena) {
        String sql = "SELECT r.*, u.nombre as nombre_usuario" +
                "FROM resena r " +
                "INNER JOIN usuario u ON r.id_usuario = u.id_usuario " +
                "WHERE r.id_resena = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idResena);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return mapearResena(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar reseña
    public boolean eliminarResena(int idResena) {
        String sql = "DELETE FROM resena WHERE id_resena = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idResena);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Listar reseñas de un usuario
    public List<Resena> listarResenasPorUsuario(int idUsuario) {
        List<Resena> lista = new ArrayList<>();
        String sql = "SELECT r.*, p.nombre_producto " +
                "FROM resena r " +
                "INNER JOIN producto p ON r.id_producto = p.id_producto " +
                "WHERE r.id_usuario = ? " +
                "ORDER BY r.fecha_resena DESC";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearResena(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
