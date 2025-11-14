package com.example.marketplace.utils;

import com.example.marketplace.dao.CategoriaDAO;
import com.example.marketplace.model.Categoria;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidarProducto {
    // Regex: solo letras, tildes, ñ y espacios
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");
    private static CategoriaDAO categoriaDAO = new CategoriaDAO();




    // --- Métodos de validación ---

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        if (!PATRON_NOMBRE.matcher(nombre.trim()).matches()) {
            return "El nombre solo puede contener letras y espacios.";
        }
        return null; // válido
    }

    public static String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripcion no puede estar vacía.";
        }
        if (descripcion.trim().length() < 6) {
            return "La descripcion debe tener al menos 6 caracteres.";
        }
        return null; // válido
    }

    public static String validarPrecio(Double precio) {
        if (precio == null) {
            return "El precio no puede estar vacío.";
        }
        if (precio <= 0) {
            return "El precio debe ser mayor que 0.";
        }
        return null; // válido
    }

    public static String validarStock(int stock) {
        if (stock < 0) {
            return "Stock invalido.";
        }
        return null; // válido
    }

    public static String validarCategoria(List<Integer> idsCategoria) {
        if (idsCategoria == null || idsCategoria.isEmpty()) {
            return "Debe seleccionar al menos una categoría.";
        }

        // Validar que cada ID exista en la base de datos
        for (Integer id : idsCategoria) {
            if (id == null || id <= 0) {
                return "ID de categoría inválido: " + id;
            }

            Categoria categoria = categoriaDAO.buscarPorId(id);
            if (categoria == null) {
                return "La categoría con ID " + id + " no existe en el sistema.";
            }
        }

        return null; // válido
    }

    public static String validarCategoriaId(int idCategoria) {
        if (idCategoria <= 0) {
            return "ID de categoría inválido.";
        }

        Categoria categoria = categoriaDAO.buscarPorId(idCategoria);

        if (categoria == null) {
            return "No existe una categoría con el ID " + idCategoria + ".";
        }

        return null; // válido
    }
}
