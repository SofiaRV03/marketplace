package com.example.marketplace.utils;

import java.util.regex.Pattern;

public class ValidarUsuario {

    // Regex: solo letras, tildes, ñ y espacios
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");

    // Regex: solo correos de gmail, hotmail, outlook o yahoo
    private static final Pattern PATRON_CORREO = Pattern.compile(
            "^[\\w._%+-]+@(gmail\\.com|hotmail\\.com|outlook\\.com|yahoo\\.com)$"
    );

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

    public static String validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return "El correo no puede estar vacío.";
        }
        if (!PATRON_CORREO.matcher(correo.trim()).matches()) {
            return "El correo debe ser válido y pertenecer a Gmail, Hotmail, Outlook o Yahoo.";
        }
        return null; // válido
    }

    public static String validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return "La contraseña no puede estar vacía.";
        }
        if (contrasena.trim().length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        return null; // válido
    }
}
