package com.example.marketplace.service;

import com.example.marketplace.dao.ProductoCategoriaDAO;
import java.util.List;

public class ProductoCategoriaService {

    private ProductoCategoriaDAO productoCategoriaDAO = new ProductoCategoriaDAO();

    // Asignar una categoría a un producto
    public boolean asignarCategoriaAProducto(int idProducto, int idCategoria) {
        return productoCategoriaDAO.agregarCategoriaAProducto(idProducto, idCategoria);
    }

    // Asignar múltiples categorías a un producto
    public boolean asignarCategoriasAProducto(int idProducto, List<Integer> idsCategoria) {
        if (idsCategoria == null || idsCategoria.isEmpty()) {
            System.out.println("Debe proporcionar al menos una categoría.");
            return false;
        }
        return productoCategoriaDAO.agregarCategoriasAProducto(idProducto, idsCategoria);
    }

    // Eliminar una categoría de un producto
    public boolean quitarCategoriaDeProducto(int idProducto, int idCategoria) {
        return productoCategoriaDAO.eliminarCategoriaDeProducto(idProducto, idCategoria);
    }

    // Eliminar todas las categorías de un producto
    public boolean quitarTodasCategoriasDeProducto(int idProducto) {
        return productoCategoriaDAO.eliminarTodasCategoriasDeProducto(idProducto);
    }

    // Obtener las categorías de un producto
    public List<Integer> obtenerCategoriasDeProducto(int idProducto) {
        return productoCategoriaDAO.obtenerCategoriasDeProducto(idProducto);
    }

    // Obtener los productos de una categoría
    public List<Integer> obtenerProductosDeCategoria(int idCategoria) {
        return productoCategoriaDAO.obtenerProductosDeCategoria(idCategoria);
    }

    // Verificar si un producto tiene una categoría
    public boolean verificarProductoTieneCategoria(int idProducto, int idCategoria) {
        return productoCategoriaDAO.productoTieneCategoria(idProducto, idCategoria);
    }

    // Actualizar las categorías de un producto
    public boolean actualizarCategoriasDeProducto(int idProducto, List<Integer> nuevasCategoria) {
        if (nuevasCategoria == null || nuevasCategoria.isEmpty()) {
            System.out.println("Debe proporcionar al menos una categoría.");
            return false;
        }
        return productoCategoriaDAO.actualizarCategoriasDeProducto(idProducto, nuevasCategoria);
    }
}