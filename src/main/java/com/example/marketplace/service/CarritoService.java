package com.example.marketplace.service;

import com.example.marketplace.dao.CarritoDAO;
import com.example.marketplace.dao.CarritoProductoDAO;
import com.example.marketplace.dao.ProductoDAO;
import com.example.marketplace.model.Carrito;
import com.example.marketplace.model.CarritoProducto;
import com.example.marketplace.model.Producto;

import java.util.List;

public class CarritoService {
    private CarritoDAO carritoDAO;
    private CarritoProductoDAO carritoProductoDAO;
    private ProductoDAO productoDAO;

    public CarritoService() {
        this.carritoDAO = new CarritoDAO();
        this.carritoProductoDAO = new CarritoProductoDAO();
        this.productoDAO = new ProductoDAO();
    }

    // Obtener o crear carrito activo para un usuario
    public Carrito obtenerCarritoActivo(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            carrito = new Carrito(idUsuario, "activo");
            carrito = carritoDAO.crearCarrito(carrito);
        }

        return carrito;
    }

    // Agregar producto al carrito
    public boolean agregarProductoAlCarrito(int idUsuario, int idProducto, int cantidad) {
        Producto producto = productoDAO.buscarPorId(idProducto);

        if (producto == null || producto.getStock_producto() < cantidad) {
            return false;
        }

        Carrito carrito = obtenerCarritoActivo(idUsuario);

        if (carrito == null) {
            return false;
        }

        double subtotal = producto.getPrecio_producto() * cantidad;
        CarritoProducto cp = new CarritoProducto(carrito.getId_carrito(), idProducto, cantidad, subtotal);

        return carritoProductoDAO.agregarProducto(cp);
    }

    // Obtener detalles del carrito (para que el Main los muestre)
    public List<CarritoProducto> obtenerDetallesCarrito(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            return null;
        }

        return carritoProductoDAO.obtenerDetallesCarrito(carrito.getId_carrito());
    }

    // Obtener carrito activo
    public Carrito obtenerCarritoActivoDeUsuario(int idUsuario) {
        return carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);
    }

    // Eliminar producto del carrito
    public boolean eliminarProductoDelCarrito(int idUsuario, int idProducto) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            return false;
        }

        return carritoProductoDAO.eliminarProducto(carrito.getId_carrito(), idProducto);
    }

    // Vaciar carrito
    public boolean vaciarCarrito(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            return false;
        }

        return carritoProductoDAO.vaciarCarrito(carrito.getId_carrito());
    }

    // Finalizar compra con descuento de stock
    public boolean finalizarCompra(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            System.err.println("No se encontró carrito activo para el usuario");
            return false;
        }

        // Obtener todos los productos del carrito
        List<CarritoProducto> productosCarrito = carritoProductoDAO.obtenerDetallesCarrito(carrito.getId_carrito());

        if (productosCarrito == null || productosCarrito.isEmpty()) {
            System.err.println("El carrito está vacío");
            return false;
        }

        // PASO 1: Validar que hay suficiente stock para todos los productos
        for (CarritoProducto cp : productosCarrito) {
            Producto producto = productoDAO.buscarPorId(cp.getid_producto());

            if (producto == null) {
                System.err.println("Producto no encontrado: " + cp.getid_producto());
                return false;
            }

            if (producto.getStock_producto() < cp.getCantidad()) {
                System.err.println("Stock insuficiente para: " + producto.getNombre_producto() +
                        " (Disponible: " + producto.getStock_producto() +
                        ", Requerido: " + cp.getCantidad() + ")");
                return false;
            }
        }

        // PASO 2: Descontar el stock de cada producto
        for (CarritoProducto cp : productosCarrito) {
            Producto producto = productoDAO.buscarPorId(cp.getid_producto());

            // Calcular nuevo stock
            int nuevoStock = producto.getStock_producto() - cp.getCantidad();
            producto.setStock_producto(nuevoStock);

            // Actualizar en la base de datos
            boolean actualizado = productoDAO.editarProducto(producto);

            if (!actualizado) {
                System.err.println("Error al actualizar stock del producto: " + producto.getNombre_producto());
                // Aquí podrías implementar un rollback si falla
                return false;
            }

            System.out.println("Stock actualizado: " + producto.getNombre_producto() +
                    " - Nuevo stock: " + nuevoStock);
        }

        // PASO 3: Marcar el carrito como finalizado
        boolean carritoFinalizado = carritoDAO.actualizarEstado(carrito.getId_carrito(), "finalizado");

        if (!carritoFinalizado) {
            System.err.println("Error al finalizar el carrito");
            return false;
        }

        System.out.println("✅ Compra finalizada exitosamente para usuario: " + idUsuario);
        return true;
    }

    // Obtener total del carrito
    public double obtenerTotalCarrito(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            return 0.0;
        }

        return carritoProductoDAO.calcularTotalCarrito(carrito.getId_carrito());
    }

    // Contar productos en el carrito
    public int contarProductosEnCarrito(int idUsuario) {
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            return 0;
        }

        return carritoProductoDAO.contarProductos(carrito.getId_carrito());
    }

    // Listar historial de carritos
    public List<Carrito> listarHistorialCarritos(int idUsuario) {
        return carritoDAO.listarCarritosPorUsuario(idUsuario);
    }

    // Listar carritos por estado
    public List<Carrito> listarCarritosPorEstado(int idUsuario, String estado) {
        return carritoDAO.listarCarritosPorUsuarioYEstado(idUsuario, estado);
    }

    // Buscar producto por ID (helper)
    public Producto buscarProductoPorId(int idProducto) {
        return productoDAO.buscarPorId(idProducto);
    }

    public boolean actualizarCantidad(int idUsuario, int idProducto, int nuevaCantidad) {
        // Validar que la cantidad sea positiva
        if (nuevaCantidad < 1) {
            System.err.println("La cantidad debe ser al menos 1");
            return false;
        }

        // Obtener el carrito activo del usuario
        Carrito carrito = carritoDAO.buscarCarritoActivoPorUsuario(idUsuario);

        if (carrito == null) {
            System.err.println("No se encontró carrito activo para el usuario");
            return false;
        }

        // Verificar que el producto existe y tiene stock suficiente
        Producto producto = productoDAO.buscarPorId(idProducto);

        if (producto == null) {
            System.err.println("Producto no encontrado");
            return false;
        }

        if (producto.getStock_producto() < nuevaCantidad) {
            System.err.println("Stock insuficiente. Disponible: " +
                    producto.getStock_producto() +
                    ", Solicitado: " + nuevaCantidad);
            return false;
        }

        // Actualizar la cantidad en la base de datos
        boolean actualizado = carritoProductoDAO.actualizarCantidad(
                carrito.getId_carrito(),
                idProducto,
                nuevaCantidad
        );

        if (actualizado) {
            System.out.println("Cantidad actualizada correctamente para producto: " + idProducto);
        }

        return actualizado;
    }

}