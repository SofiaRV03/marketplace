package com.example.marketplace.service;

import com.example.marketplace.dao.PagoDAO;
import com.example.marketplace.dao.PedidoDAO;
import com.example.marketplace.model.Pago;
import com.example.marketplace.model.Pedido;
import com.example.marketplace.model.ProductoPedido;
import com.example.marketplace.model.CarritoProducto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoService {

    private PagoDAO pagoDAO;
    private PedidoDAO pedidoDAO;

    public PagoService() {
        this.pagoDAO = new PagoDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    /**
     * Procesa el pago completo: crea el pedido, los detalles y registra el pago
     */
    public String procesarPagoCompleto(int idUsuario, List<CarritoProducto> items,
                                       Pago datosPago, double subtotal, double envio) {
        try {
            // 1. Calcular total
            double total = subtotal + envio;

            // 2. Crear el pedido usando tu estructura
            Pedido pedido = new Pedido();
            pedido.setId_usuario(idUsuario);
            pedido.setFecha_pedido(LocalDateTime.now());
            pedido.setTotal(total);
            pedido.setEstado("Pendiente");

            // 3. Convertir CarritoProducto a ProductoPedido
            List<ProductoPedido> productosParaPedido = new ArrayList<>();
            for (CarritoProducto item : items) {
                ProductoPedido pp = new ProductoPedido();
                pp.setId_producto(item.getid_producto());
                pp.setCantidad(item.getCantidad());

                // Calcular precio unitario y subtotal correctamente
                double precioUnitario = item.getSubtotal() / item.getCantidad();
                pp.setPrecio_unitario(precioUnitario);
                pp.setSubtotal(item.getSubtotal());

                productosParaPedido.add(pp);
            }

            // 4. Crear el pedido completo (pedido + productos + actualizar stock)
            Pedido pedidoCreado = pedidoDAO.crearPedidoCompleto(pedido, productosParaPedido);

            if (pedidoCreado == null || pedidoCreado.getId_pedido() <= 0) {
                System.err.println("Error: No se pudo crear el pedido");
                return null;
            }

            // 5. Registrar el pago
            datosPago.setId_pedido(pedidoCreado.getId_pedido());
            datosPago.setMonto(total);
            datosPago.setEstado_pago("Completado");

            boolean pagoRegistrado = pagoDAO.registrarPago(datosPago);

            if (pagoRegistrado) {
                // Generar número de pedido
                return generarNumeroPedido(pedidoCreado.getId_pedido());
            } else {
                System.err.println("Error: No se pudo registrar el pago");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al procesar pago: " + e.getMessage());
        }

        return null;
    }

    /**
     * Genera un número de pedido formateado
     */
    private String generarNumeroPedido(int idPedido) {
        return "PS-" + String.format("%08d", idPedido);
    }

    /**
     * Valida los datos de la tarjeta (simulación)
     */
    public boolean validarTarjeta(String numeroTarjeta, String fechaCaducidad, String cvv) {
        // Validaciones básicas
        if (numeroTarjeta == null || numeroTarjeta.replaceAll("\\s", "").length() < 15) {
            return false;
        }

        if (fechaCaducidad == null || !fechaCaducidad.matches("\\d{2}/\\d{2}")) {
            return false;
        }

        if (cvv == null || cvv.length() < 3) {
            return false;
        }

        return true;
    }

    /**
     * Valida los datos de dirección
     */
    public boolean validarDireccion(String nombreCompleto, String direccion,
                                    String ciudad, String codigoPostal) {
        return nombreCompleto != null && !nombreCompleto.trim().isEmpty() &&
                direccion != null && !direccion.trim().isEmpty() &&
                ciudad != null && !ciudad.trim().isEmpty() &&
                codigoPostal != null && !codigoPostal.trim().isEmpty();
    }

    /**
     * Obtiene un pago por ID de pedido
     */
    public Pago obtenerPagoPorPedido(int idPedido) {
        return pagoDAO.obtenerPorPedido(idPedido);
    }

    /**
     * Calcula el costo de envío (puede ser más complejo en producción)
     */
    public double calcularCostoEnvio(double subtotal) {
        // Ejemplo: $100.000 fijo o gratis para compras mayores a $500.000
        if (subtotal >= 500000) {
            return 0.0;
        }
        return 100000.0;
    }
}