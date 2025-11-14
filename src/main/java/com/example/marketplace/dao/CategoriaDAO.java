package com.example.marketplace.dao;

import com.example.marketplace.model.Categoria;

import javax.smartcardio.CardTerminal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public Categoria crearCategoria(Categoria categoria) {
        // Verificar si ya existe el correo
        if (buscarPorNombre(categoria.getNombre_categoria()) != null) {
            return null; // si ya existe, devolvemos null
        }

        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, categoria.getNombre_categoria());

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                // Obtener el id generado
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        categoria.setId_categoria(idGenerado);
                    }
                }
                return categoria;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
            return null;
        }
    }

    public List<Categoria> listarCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Connection conexion = conexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria c = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return lista;
    }

    public List<String> obtenerNombreCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT nombre FROM categorias ORDER BY nombre";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
            e.printStackTrace();
        }

        return categorias;
    }

    public Categoria buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM categorias WHERE nombre = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return null; // no encontrado
    }
    public Categoria buscarIdPorNombre(String nombre) {
        String sql = "SELECT id_categoria FROM categorias WHERE nombre = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return null; // no encontrado
    }

    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // log para desarrollador
        }
        return null; // no encontrado
    }

    public boolean eliminarCategoria(int id) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
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


    public boolean editarCategoria(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id_categoria = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getNombre_categoria());
            pstmt.setInt(5, categoria.getId_categoria());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Categoria> obtenerCategoriasPorProducto(int id_producto) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT c.* FROM categorias c " +
                "INNER JOIN producto_categoria pc ON c.id_categoria = pc.id_categoria " +
                "WHERE pc.id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id_producto);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Categoria c = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                        // Agrega otros campos si los tienes
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categorías del producto: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

}
