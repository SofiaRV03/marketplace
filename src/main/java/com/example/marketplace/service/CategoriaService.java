package com.example.marketplace.service;

import com.example.marketplace.dao.CategoriaDAO;
import com.example.marketplace.model.Categoria;

import java.util.List;

public class CategoriaService {
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    public Categoria registrarCategoria(String nombre) {
        Categoria nueva = new Categoria(nombre);
        return categoriaDAO.crearCategoria(nueva);
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaDAO.listarCategorias();
    }

    public List<String> obtenerNombreCategorias() {
        return categoriaDAO.obtenerNombreCategorias();
    }




    public boolean actualizarCategoria(Categoria categoria) {
        return categoriaDAO.editarCategoria(categoria);
    }

    public boolean eliminarUsuario(int id) {
        return categoriaDAO.eliminarCategoria(id);
        // true si se eliminó, false si no
    }

    public Categoria obtenerCategoriaPorId(int id) {
        return categoriaDAO.buscarPorId(id);
    }

    public Categoria obtenerCategoriaPorNombre(String nombre) {
        return categoriaDAO.buscarPorNombre(nombre);
    }

    public List<Categoria> obtenerCategoriasPorProducto(int idProducto) {
        return categoriaDAO.obtenerCategoriasPorProducto(idProducto);
    }


}
