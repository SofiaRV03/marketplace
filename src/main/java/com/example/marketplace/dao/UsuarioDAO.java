package com.example.marketplace.dao;

import com.example.marketplace.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario crearUsuario(Usuario usuario) {
        // Verificar si ya existe el correo
        if (buscarPorCorreo(usuario.getCorreo()) != null) {
            return null; // si ya existe, devolvemos null
        }

        String sql = "INSERT INTO usuario (nombre, correo, contrasena, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getTelefono());

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                // Obtener el id generado
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        usuario.setId(idGenerado);
                    }
                }
                return usuario; // devolvemos el usuario completo con id
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
            return null;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conexion = conexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fecha_registro").toLocalDateTime(),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return lista;
    }

    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fecha_registro").toLocalDateTime(),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return null; // no encontrado
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fecha_registro").toLocalDateTime(),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return null; // no encontrado
    }

    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int filas = pstmt.executeUpdate();
            return filas > 0; // true si eliminó, false si no
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
            return false;
        }
    }

    public boolean editarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, correo = ?, contrasena = ?, direccion = ?, telefono = ? WHERE id_usuario = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getTelefono());
            pstmt.setInt(6, usuario.getId());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarTipo(Usuario usuario, String tipo) {
        String sql = "UPDATE usuario SET tipo = ? WHERE id_usuario = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tipo);
            pstmt.setInt(2, usuario.getId());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}