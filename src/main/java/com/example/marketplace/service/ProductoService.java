package com.example.marketplace.service;

import com.example.marketplace.dao.ProductoDAO;
import com.example.marketplace.model.Categoria;
import com.example.marketplace.model.Producto;

import java.util.List;

public class ProductoService {

    private ProductoDAO productoDAO = new ProductoDAO();


    public Producto registrarProducto(String nombre, String descripcion, Double precio, int stock, String imagen, List<Integer> categorias) {
        Producto producto = new Producto(nombre, descripcion, precio, stock, imagen);
        Producto productoCreado = productoDAO.crearProducto(producto);

        if (productoCreado != null && categorias != null && !categorias.isEmpty()) {
            ProductoCategoriaService productoCategoriaService = new ProductoCategoriaService();
            productoCategoriaService.asignarCategoriasAProducto(productoCreado.getId_producto(),categorias);
        }
        return productoCreado;
    }

    public List<Producto> obtenerProductos() {
        return productoDAO.listarProductos();
    }

    public Producto obtenerProductoPorId(int id) {
        return productoDAO.buscarPorId(id);
    }

    public List<Producto> listarPorCategoria(List<Integer> idsCategoria) {
        return productoDAO.listarPorCategoria(idsCategoria);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }

    public boolean actualizarProducto(Producto producto) {
        return productoDAO.editarProducto(producto);
    }

    public boolean eliminarProducto(int id) {
        return productoDAO.eliminarProducto(id);
    }

    public boolean actualizarStock(int idProducto) {
        return productoDAO.actualizarStock(idProducto);
    }
    public String obtenerCategoriasProducto(int idProducto) {
        ProductoCategoriaService pcService = new ProductoCategoriaService();
        CategoriaService categoriaService = new CategoriaService();

        List<Integer> idsCategoria = pcService.obtenerCategoriasDeProducto(idProducto);

        StringBuilder categorias = new StringBuilder();
        for (int idCat : idsCategoria) {
            Categoria cat = categoriaService.obtenerCategoriaPorId(idCat);
            if (cat != null) {
                if (categorias.length() > 0) {
                    categorias.append(", ");
                }
                categorias.append(cat.getNombre_categoria());
            }
        }

        return categorias.length() > 0 ? categorias.toString() : "Sin categoría";
    }

    // Agregar estos métodos a la clase ProductoService

    /**
     * Obtiene productos filtrados por nombre de categoría
     */
    public List<Producto> obtenerProductosPorCategoria(String nombreCategoria) {
        return productoDAO.listarPorNombreCategoria(nombreCategoria);
    }

    /**
     * Obtiene productos filtrados por ID de categoría
     */
    public List<Producto> obtenerProductosPorIdCategoria(int idCategoria) {
        return productoDAO.listarPorIdCategoria(idCategoria);
    }



}
