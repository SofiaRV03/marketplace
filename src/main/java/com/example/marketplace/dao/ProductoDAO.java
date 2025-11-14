package com.example.marketplace.dao;

import com.example.marketplace.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductoDAO {

    public Producto crearProducto(Producto producto) {
        String sql="INSERT INTO producto (nombre, descripcion, precio, stock, imagen) VALUES (?,?,?,?,?)";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt= conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1,producto.getNombre_producto());
            pstmt.setString(2, producto.getDescripcion_producto());
            pstmt.setDouble(3,producto.getPrecio_producto());
            pstmt.setInt(4, producto.getStock_producto());
            pstmt.setString(5,producto.getImagen_producto());

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId_producto(rs.getInt(1));
                    }
                }
                return producto;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        try (Connection conexion = conexionDB.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM producto WHERE id_producto = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Producto> listarPorCategoria(List<Integer> idsCategoria) {
        List<Producto> lista = new ArrayList<>();

        if (idsCategoria == null || idsCategoria.isEmpty()) {
            return lista;
        }

        // Productos que tienen AL MENOS UNA de las categorías
        String sql = "SELECT DISTINCT p.* FROM producto p " +
                "INNER JOIN producto_categoria pc ON p.id_producto = pc.id_producto " +
                "WHERE pc.id_categoria IN (" + generarPlaceholders(idsCategoria.size()) + ")";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            int index = 1;
            for (Integer idCategoria : idsCategoria) {
                pstmt.setInt(index++, idCategoria);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por categorías: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Método auxiliar para generar placeholders (?, ?, ?)
    private String generarPlaceholders(int cantidad) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < cantidad; i++) {
            placeholders.append("?");
            if (i < cantidad - 1) {
                placeholders.append(",");
            }
        }
        return placeholders.toString();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean editarProducto(Producto producto) {
        String sql = "UPDATE producto SET nombre=?, descripcion=?, precio=?, stock=?, imagen=? WHERE id_producto=?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre_producto());
            pstmt.setString(2, producto.getDescripcion_producto());
            pstmt.setDouble(3, producto.getPrecio_producto());
            pstmt.setInt(4, producto.getStock_producto());
            pstmt.setString(5, producto.getImagen_producto());
            pstmt.setInt(6, producto.getId_producto());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarStock(int idProducto) {
        String sql = "UPDATE producto SET stock = stock - 1 WHERE id_producto = ? AND stock > 0";
        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);

            int filas = pstmt.executeUpdate();
            return filas > 0; // devuelve true si se actualizó (es decir, si el stock era > 0)
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Agregar estos métodos a la clase ProductoDAO

    /**
     * Lista productos de una categoría específica por nombre de categoría
     */
    public List<Producto> listarPorNombreCategoria(String nombreCategoria) {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT DISTINCT p.* FROM producto p " +
                "INNER JOIN producto_categoria pc ON p.id_producto = pc.id_producto " +
                "INNER JOIN categorias c ON pc.id_categoria = c.id_categoria " +
                "WHERE c.nombre = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setString(1, nombreCategoria);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Lista productos de una categoría específica por ID de categoría
     */
    public List<Producto> listarPorIdCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT DISTINCT p.* FROM producto p " +
                "INNER JOIN producto_categoria pc ON p.id_producto = pc.id_producto " +
                "WHERE pc.id_categoria = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCategoria);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por ID categoría: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

}

