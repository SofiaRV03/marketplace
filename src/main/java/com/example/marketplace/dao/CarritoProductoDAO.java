package com.example.marketplace.dao;



import com.example.marketplace.model.CarritoProducto;
import com.example.marketplace.model.Producto;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoProductoDAO {

    // Agregar producto al carrito
    public boolean agregarProducto(CarritoProducto carritoProducto) {
        // Verificar si el producto ya existe en el carrito
        if (existeProductoEnCarrito(carritoProducto.getid_carrito(), carritoProducto.getid_producto())) {
            return actualizarCantidad(carritoProducto.getid_carrito(), carritoProducto.getid_producto(),
                    carritoProducto.getCantidad());
        }

        String sql = "INSERT INTO carrito_producto (id_carrito, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, carritoProducto.getid_carrito());
            pstmt.setInt(2, carritoProducto.getid_producto());
            pstmt.setInt(3, carritoProducto.getCantidad());
            pstmt.setDouble(4, carritoProducto.getSubtotal());

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verificar si un producto ya existe en el carrito
    private boolean existeProductoEnCarrito(int idCarrito, int idProducto) {
        String sql = "SELECT COUNT(*) FROM carrito_producto WHERE id_carrito = ? AND id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idProducto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarCantidad(int idCarrito, int idProducto, int nuevaCantidad) {
        // SQL que establece la cantidad directamente (no incremental)
        String sql = "UPDATE carrito_producto " +
                "SET cantidad = ?, " +
                "subtotal = ? * (SELECT precio FROM producto WHERE id_producto = ?) " +
                "WHERE id_carrito = ? AND id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, nuevaCantidad);        // Establece la nueva cantidad
            pstmt.setInt(2, nuevaCantidad);        // Usa la misma cantidad para calcular subtotal
            pstmt.setInt(3, idProducto);           // ID del producto para obtener su precio
            pstmt.setInt(4, idCarrito);            // ID del carrito
            pstmt.setInt(5, idProducto);           // ID del producto para el WHERE

            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar producto del carrito
    public boolean eliminarProducto(int idCarrito, int idProducto) {
        String sql = "DELETE FROM carrito_producto WHERE id_carrito = ? AND id_producto = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            pstmt.setInt(2, idProducto);

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Vaciar carrito
    public boolean vaciarCarrito(int idCarrito) {
        String sql = "DELETE FROM carrito_producto WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Calcular total del carrito
    public double calcularTotalCarrito(int idCarrito) {
        double total = 0.0;
        String sql = "SELECT SUM(subtotal) as total FROM carrito_producto WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Contar productos en el carrito
    public int contarProductos(int idCarrito) {
        int count = 0;
        String sql = "SELECT COUNT(*) as total FROM carrito_producto WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<CarritoProducto> obtenerDetallesCarrito(int idCarrito) {
        List<CarritoProducto> productos = new ArrayList<>();
        String sql = "SELECT id_carrito, id_producto, cantidad, subtotal " +
                "FROM carrito_producto " +
                "WHERE id_carrito = ?";

        try (Connection conexion = conexionDB.getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, idCarrito);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CarritoProducto cp = new CarritoProducto(
                        rs.getInt("id_carrito"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                );
                productos.add(cp);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener detalles del carrito: " + e.getMessage());
            e.printStackTrace();
        }

        return productos;
    }
}