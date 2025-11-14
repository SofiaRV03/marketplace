package com.example.marketplace.dao;

import com.example.marketplace.model.ProductoCategoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoCategoriaDAO {

    // Asignar una categoría a un producto
    public boolean agregarCategoriaAProducto(int idProducto, int idCategoria) {
        String sql = "INSERT INTO producto_categoria (id_producto, id_categoria) VALUES (?, ?)";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            pstmt.setInt(2, idCategoria);

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Asignar múltiples categorías a un producto
    public boolean agregarCategoriasAProducto(int idProducto, List<Integer> idsCategoria) {
        String sql = "INSERT INTO producto_categoria (id_producto, id_categoria) VALUES (?, ?)";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            for (int idCategoria : idsCategoria) {
                pstmt.setInt(1, idProducto);
                pstmt.setInt(2, idCategoria);
                pstmt.addBatch();
            }

            int[] resultados = pstmt.executeBatch();
            return resultados.length > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar una categoría específica de un producto
    public boolean eliminarCategoriaDeProducto(int idProducto, int idCategoria) {
        String sql = "DELETE FROM producto_categoria WHERE id_producto = ? AND id_categoria = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            pstmt.setInt(2, idCategoria);

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar todas las categorías de un producto
    public boolean eliminarTodasCategoriasDeProducto(int idProducto) {
        String sql = "DELETE FROM producto_categoria WHERE id_producto = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todos los IDs de categorías de un producto
    public List<Integer> obtenerCategoriasDeProducto(int idProducto) {
        List<Integer> idsCategoria = new ArrayList<>();
        String sql = "SELECT id_categoria FROM producto_categoria WHERE id_producto = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                idsCategoria.add(rs.getInt("id_categoria"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idsCategoria;
    }

    // Obtener todos los IDs de productos de una categoría
    public List<Integer> obtenerProductosDeCategoria(int idCategoria) {
        List<Integer> idsProducto = new ArrayList<>();
        String sql = "SELECT id_producto FROM producto_categoria WHERE id_categoria = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCategoria);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                idsProducto.add(rs.getInt("id_producto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idsProducto;
    }

    // Verificar si un producto tiene una categoría específica
    public boolean productoTieneCategoria(int idProducto, int idCategoria) {
        String sql = "SELECT COUNT(*) FROM producto_categoria WHERE id_producto = ? AND id_categoria = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            pstmt.setInt(2, idCategoria);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Actualizar las categorías de un producto (elimina las anteriores y agrega las nuevas)
    public boolean actualizarCategoriasDeProducto(int idProducto, List<Integer> nuevasCategoria) {
        try (Connection conexion = conexionDB.getConnection()) {
            conexion.setAutoCommit(false);

            try {
                // Eliminar categorías anteriores
                String sqlDelete = "DELETE FROM producto_categoria WHERE id_producto = ?";
                try (PreparedStatement pstmtDelete = conexion.prepareStatement(sqlDelete)) {
                    pstmtDelete.setInt(1, idProducto);
                    pstmtDelete.executeUpdate();
                }

                // Insertar nuevas categorías
                String sqlInsert = "INSERT INTO producto_categoria (id_producto, id_categoria) VALUES (?, ?)";
                try (PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert)) {
                    for (int idCategoria : nuevasCategoria) {
                        pstmtInsert.setInt(1, idProducto);
                        pstmtInsert.setInt(2, idCategoria);
                        pstmtInsert.addBatch();
                    }
                    pstmtInsert.executeBatch();
                }

                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}