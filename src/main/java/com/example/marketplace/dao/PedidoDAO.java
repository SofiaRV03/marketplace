package com.example.marketplace.dao;

import com.example.marketplace.model.Pedido;
import com.example.marketplace.model.ProductoPedido;
import com.example.marketplace.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Crear un pedido completo (pedido + productos)
    public Pedido crearPedidoCompleto(Pedido pedido, List<ProductoPedido> productos) {
        Connection conn = null;
        try {
            conn = conexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido
            String sqlPedido = "INSERT INTO pedido (id_usuario, fecha_pedido, total, estado) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);

            pstmtPedido.setInt(1, pedido.getId_usuario());
            pstmtPedido.setTimestamp(2, Timestamp.valueOf(pedido.getFecha_pedido()));
            pstmtPedido.setDouble(3, pedido.getTotal());
            pstmtPedido.setString(4, pedido.getEstado());

            int filas = pstmtPedido.executeUpdate();

            if (filas > 0) {
                // Obtener el ID generado del pedido
                ResultSet rs = pstmtPedido.getGeneratedKeys();
                if (rs.next()) {
                    int idPedidoGenerado = rs.getInt(1);
                    pedido.setId_pedido(idPedidoGenerado);

                    // 2. Insertar los productos del pedido
                    String sqlProducto = "INSERT INTO producto_pedido (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmtProducto = conn.prepareStatement(sqlProducto);

                    for (ProductoPedido pp : productos) {
                        pstmtProducto.setInt(1, idPedidoGenerado);
                        pstmtProducto.setInt(2, pp.getId_producto());
                        pstmtProducto.setInt(3, pp.getCantidad());
                        pstmtProducto.setDouble(4, pp.getPrecio_unitario());
                        pstmtProducto.setDouble(5, pp.getSubtotal());
                        pstmtProducto.addBatch();
                    }

                    pstmtProducto.executeBatch();

                    // 3. Actualizar el stock de los productos
                    String sqlUpdateStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
                    PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock);

                    for (ProductoPedido pp : productos) {
                        pstmtStock.setInt(1, pp.getCantidad());
                        pstmtStock.setInt(2, pp.getId_producto());
                        pstmtStock.addBatch();
                    }

                    pstmtStock.executeBatch();

                    conn.commit(); // Confirmar transacción
                    return pedido;
                }
            }

            conn.rollback(); // Si algo falla, revertir
            return null;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Obtener historial de pedidos de un usuario (CON nombre del usuario)
    public List<Pedido> obtenerPedidosPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre as nombre_usuario " +
                "FROM pedido p " +
                "LEFT JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.id_usuario = ? " +
                "ORDER BY p.fecha_pedido DESC";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_usuario"),
                        rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                        rs.getDouble("total"),
                        rs.getString("estado")
                );
                pedido.setNombreUsuario(rs.getString("nombre_usuario"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    // Obtener productos de un pedido específico
    public List<ProductoPedido> obtenerProductosDePedido(int idPedido) {
        List<ProductoPedido> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto_pedido WHERE id_pedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductoPedido pp = new ProductoPedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal")
                );
                productos.add(pp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    // Obtener detalles completos de un pedido (con información de productos)
    public List<Producto> obtenerDetalleProductosPedido(int idPedido) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, pp.cantidad, pp.precio_unitario, pp.subtotal " +
                "FROM producto p " +
                "INNER JOIN producto_pedido pp ON p.id_producto = pp.id_producto " +
                "WHERE pp.id_pedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_unitario"), // Precio al momento de la compra
                        rs.getInt("cantidad"), // Cantidad comprada (usamos el campo stock para esto)
                        rs.getString("imagen")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    // Buscar pedido por ID (CON nombre del usuario)
    public Pedido buscarPedidoPorId(int idPedido) {
        String sql = "SELECT p.*, u.nombre as nombre_usuario " +
                "FROM pedido p " +
                "LEFT JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.id_pedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_usuario"),
                        rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                        rs.getDouble("total"),
                        rs.getString("estado")
                );
                pedido.setNombreUsuario(rs.getString("nombre_usuario"));
                return pedido;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Actualizar estado de un pedido
    public boolean actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idPedido);

            int filas = pstmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todos los pedidos (para admin) - CON nombre del usuario
    public List<Pedido> obtenerTodosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre as nombre_usuario " +
                "FROM pedido p " +
                "LEFT JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "ORDER BY p.fecha_pedido DESC";

        try (Connection conn = conexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_usuario"),
                        rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                        rs.getDouble("total"),
                        rs.getString("estado")
                );
                pedido.setNombreUsuario(rs.getString("nombre_usuario"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    // Cancelar pedido (solo si está en estado "Pendiente")
    public boolean cancelarPedido(int idPedido) {
        Connection conn = null;
        try {
            conn = conexionDB.getConnection();
            conn.setAutoCommit(false);

            // Verificar que el pedido esté en estado "Pendiente"
            String sqlVerificar = "SELECT estado FROM pedido WHERE id_pedido = ?";
            PreparedStatement pstmtVerificar = conn.prepareStatement(sqlVerificar);
            pstmtVerificar.setInt(1, idPedido);
            ResultSet rs = pstmtVerificar.executeQuery();

            if (rs.next() && "Pendiente".equals(rs.getString("estado"))) {
                // 1. Obtener productos del pedido para devolver stock
                List<ProductoPedido> productos = obtenerProductosDePedido(idPedido);

                // 2. Devolver el stock
                String sqlDevolver = "UPDATE producto SET stock = stock + ? WHERE id_producto = ?";
                PreparedStatement pstmtDevolver = conn.prepareStatement(sqlDevolver);

                for (ProductoPedido pp : productos) {
                    pstmtDevolver.setInt(1, pp.getCantidad());
                    pstmtDevolver.setInt(2, pp.getId_producto());
                    pstmtDevolver.addBatch();
                }
                pstmtDevolver.executeBatch();

                // 3. Actualizar estado del pedido
                String sqlCancelar = "UPDATE pedido SET estado = 'Cancelado' WHERE id_pedido = ?";
                PreparedStatement pstmtCancelar = conn.prepareStatement(sqlCancelar);
                pstmtCancelar.setInt(1, idPedido);
                pstmtCancelar.executeUpdate();

                conn.commit();
                return true;
            }

            conn.rollback();
            return false;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Obtener estadísticas de pedidos (para dashboard admin)
    public int contarPedidosPorEstado(String estado) {
        String sql = "SELECT COUNT(*) as total FROM pedido WHERE estado = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Obtener total de ventas
    public double obtenerTotalVentas() {
        String sql = "SELECT SUM(total) as total_ventas FROM pedido WHERE estado != 'Cancelado'";

        try (Connection conn = conexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("total_ventas");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}