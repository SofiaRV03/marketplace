package com.example.marketplace.dao;

import com.example.marketplace.model.Pago;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    /**
     * Registra un nuevo pago en la base de datos
     */
    public boolean registrarPago(Pago pago) {
        String sql = "INSERT INTO pago (id_pedido, metodo_pago, monto, estado_pago, fecha_pago) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, pago.getId_pedido());
            pstmt.setString(2, pago.getMetodo_pago());
            pstmt.setDouble(3, pago.getMonto());
            pstmt.setString(4, pago.getEstado_pago());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    pago.setId_pago(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al registrar pago: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene un pago por ID de pedido
     */
    public Pago obtenerPorPedido(int idPedido) {
        String sql = "SELECT * FROM pago WHERE id_pedido = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Pago pago = new Pago();
                pago.setId_pago(rs.getInt("id_pago"));
                pago.setId_pedido(rs.getInt("id_pedido"));
                pago.setMetodo_pago(rs.getString("metodo_pago"));
                pago.setMonto(rs.getDouble("monto"));
                pago.setEstado_pago(rs.getString("estado_pago"));
                pago.setFecha_pago(rs.getTimestamp("fecha_pago"));
                return pago;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener pago: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtiene todos los pagos
     */
    public List<Pago> obtenerTodos() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago ORDER BY fecha_pago DESC";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Pago pago = new Pago();
                pago.setId_pago(rs.getInt("id_pago"));
                pago.setId_pedido(rs.getInt("id_pedido"));
                pago.setMetodo_pago(rs.getString("metodo_pago"));
                pago.setMonto(rs.getDouble("monto"));
                pago.setEstado_pago(rs.getString("estado_pago"));
                pago.setFecha_pago(rs.getTimestamp("fecha_pago"));
                pagos.add(pago);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener pagos: " + e.getMessage());
        }

        return pagos;
    }

    /**
     * Actualiza el estado de un pago
     */
    public boolean actualizarEstado(int idPago, String nuevoEstado) {
        String sql = "UPDATE pago SET estado_pago = ? WHERE id_pago = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idPago);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar estado de pago: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene el total de pagos realizados
     */
    public double obtenerTotalPagos() {
        String sql = "SELECT SUM(monto) as total FROM pago WHERE estado_pago = 'Completado'";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener total de pagos: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Obtiene pagos por método de pago
     */
    public List<Pago> obtenerPorMetodo(String metodoPago) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE metodo_pago = ? ORDER BY fecha_pago DESC";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, metodoPago);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pago pago = new Pago();
                pago.setId_pago(rs.getInt("id_pago"));
                pago.setId_pedido(rs.getInt("id_pedido"));
                pago.setMetodo_pago(rs.getString("metodo_pago"));
                pago.setMonto(rs.getDouble("monto"));
                pago.setEstado_pago(rs.getString("estado_pago"));
                pago.setFecha_pago(rs.getTimestamp("fecha_pago"));
                pagos.add(pago);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al obtener pagos por método: " + e.getMessage());
        }

        return pagos;
    }

    /**
     * Cuenta pagos por estado
     */
    public int contarPorEstado(String estadoPago) {
        String sql = "SELECT COUNT(*) as total FROM pago WHERE estado_pago = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estadoPago);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al contar pagos: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Elimina un pago (solo si es necesario)
     */
    public boolean eliminarPago(int idPago) {
        String sql = "DELETE FROM pago WHERE id_pago = ?";

        try (Connection conn = conexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPago);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al eliminar pago: " + e.getMessage());
        }

        return false;
    }
}