package com.example.marketplace.service;

import com.example.marketplace.dao.PedidoDAO;
import com.example.marketplace.model.Pedido;
import com.example.marketplace.model.ProductoPedido;
import com.example.marketplace.model.Producto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PedidoService {
    private PedidoDAO pedidoDAO = new PedidoDAO();

    /**
     * Crear un pedido desde el carrito de compras
     * @param idUsuario ID del usuario que realiza la compra
     * @param carrito Mapa con productos y cantidades (id_producto -> cantidad)
     * @param productos Lista de productos con sus detalles
     * @return Pedido creado o null si falla
     */
    public Pedido crearPedidoDesdeCarrito(int idUsuario, Map<Integer, Integer> carrito, List<Producto> productos) {
        try {
            // Calcular el total del pedido
            double total = 0.0;
            for (Producto producto : productos) {
                int cantidad = carrito.getOrDefault(producto.getId_producto(), 0);
                total += producto.getPrecio_producto() * cantidad;
            }

            // Crear el pedido
            Pedido pedido = new Pedido(
                    idUsuario,
                    LocalDateTime.now(),
                    total,
                    "Pendiente"
            );

            // Crear lista de ProductoPedido
            List<ProductoPedido> productosPedido = new java.util.ArrayList<>();
            for (Producto producto : productos) {
                int cantidad = carrito.getOrDefault(producto.getId_producto(), 0);
                if (cantidad > 0) {
                    double subtotal = producto.getPrecio_producto() * cantidad;
                    ProductoPedido pp = new ProductoPedido(
                            0, // Se asignará al crear el pedido
                            producto.getId_producto(),
                            cantidad,
                            producto.getPrecio_producto(),
                            subtotal
                    );
                    productosPedido.add(pp);
                }
            }

            // Guardar en la base de datos
            return pedidoDAO.crearPedidoCompleto(pedido, productosPedido);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crear pedido con lista de ProductoPedido ya preparada
     */
    public Pedido crearPedido(int idUsuario, List<ProductoPedido> productos, double total) {
        Pedido pedido = new Pedido(
                idUsuario,
                LocalDateTime.now(),
                total,
                "Pendiente"
        );
        return pedidoDAO.crearPedidoCompleto(pedido, productos);
    }

    /**
     * Obtener historial de pedidos de un usuario
     */
    public List<Pedido> obtenerHistorialPedidos(int idUsuario) {
        return pedidoDAO.obtenerPedidosPorUsuario(idUsuario);
    }

    /**
     * Obtener detalle de un pedido específico
     */
    public Pedido obtenerDetallePedido(int idPedido) {
        return pedidoDAO.buscarPedidoPorId(idPedido);
    }

    /**
     * Obtener productos de un pedido
     */
    public List<ProductoPedido> obtenerProductosDePedido(int idPedido) {
        return pedidoDAO.obtenerProductosDePedido(idPedido);
    }

    /**
     * Obtener información completa de productos de un pedido
     */
    public List<Producto> obtenerDetalleProductosPedido(int idPedido) {
        return pedidoDAO.obtenerDetalleProductosPedido(idPedido);
    }

    /**
     * Actualizar estado de un pedido
     * Estados posibles: "Pendiente", "Procesando", "Enviado", "Entregado", "Cancelado"
     */
    public boolean actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        // Validar estados permitidos
        List<String> estadosValidos = List.of("Pendiente", "Procesando", "Enviado", "Entregado", "Cancelado");
        if (!estadosValidos.contains(nuevoEstado)) {
            return false;
        }
        return pedidoDAO.actualizarEstadoPedido(idPedido, nuevoEstado);
    }

    /**
     * Cancelar un pedido (solo si está pendiente)
     */
    public boolean cancelarPedido(int idPedido) {
        return pedidoDAO.cancelarPedido(idPedido);
    }

    /**
     * Obtener todos los pedidos (para administradores)
     */
    public List<Pedido> obtenerTodosPedidos() {
        return pedidoDAO.obtenerTodosPedidos();
    }

    /**
     * Verificar si un usuario puede cancelar un pedido
     */
    public boolean puedeCancelarPedido(int idPedido, int idUsuario) {
        Pedido pedido = pedidoDAO.buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }
        // Solo puede cancelar si es su pedido y está en estado Pendiente
        return pedido.getId_usuario() == idUsuario && "Pendiente".equals(pedido.getEstado());
    }

    /**
     * Contar pedidos por estado
     */
    public int contarPedidosPorEstado(String estado) {
        return pedidoDAO.contarPedidosPorEstado(estado);
    }

    /**
     * Obtener total de ventas
     */
    public double obtenerTotalVentas() {
        return pedidoDAO.obtenerTotalVentas();
    }

    /**
     * Verificar si hay suficiente stock antes de crear el pedido
     */
    public boolean verificarStockDisponible(List<ProductoPedido> productos, List<Producto> productosInfo) {
        for (ProductoPedido pp : productos) {
            for (Producto prod : productosInfo) {
                if (prod.getId_producto() == pp.getId_producto()) {
                    if (prod.getStock_producto() < pp.getCantidad()) {
                        return false; // No hay suficiente stock
                    }
                }
            }
        }
        return true;
    }

    /**
     * Obtener estadísticas generales de pedidos
     */
    public EstadisticasPedidos obtenerEstadisticas() {
        int pendientes = contarPedidosPorEstado("Pendiente");
        int procesando = contarPedidosPorEstado("Procesando");
        int enviados = contarPedidosPorEstado("Enviado");
        int entregados = contarPedidosPorEstado("Entregado");
        int cancelados = contarPedidosPorEstado("Cancelado");
        double totalVentas = obtenerTotalVentas();

        return new EstadisticasPedidos(pendientes, procesando, enviados, entregados, cancelados, totalVentas);
    }

    // Clase interna para las estadísticas
    public static class EstadisticasPedidos {
        private int pendientes;
        private int procesando;
        private int enviados;
        private int entregados;
        private int cancelados;
        private double totalVentas;

        public EstadisticasPedidos(int pendientes, int procesando, int enviados,
                                   int entregados, int cancelados, double totalVentas) {
            this.pendientes = pendientes;
            this.procesando = procesando;
            this.enviados = enviados;
            this.entregados = entregados;
            this.cancelados = cancelados;
            this.totalVentas = totalVentas;
        }

        // Getters
        public int getPendientes() { return pendientes; }
        public int getProcesando() { return procesando; }
        public int getEnviados() { return enviados; }
        public int getEntregados() { return entregados; }
        public int getCancelados() { return cancelados; }
        public double getTotalVentas() { return totalVentas; }
        public int getTotalPedidos() {
            return pendientes + procesando + enviados + entregados + cancelados;
        }
    }
}