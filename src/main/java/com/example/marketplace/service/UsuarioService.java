package com.example.marketplace.service;

import com.example.marketplace.dao.UsuarioDAO;
import com.example.marketplace.model.Usuario;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario registrarUsuario(String nombre, String correo, String contrasena) {
        Usuario nuevo = new Usuario(nombre, correo, contrasena);
        return usuarioDAO.crearUsuario(nuevo);
        // Si devuelve null significa que ya existía el correo o falló
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    public Usuario login(String correo, String contrasena) {
        Usuario u = usuarioDAO.buscarPorCorreo(correo);
        if (u != null && u.getContrasena().equals(contrasena)) {
            return u; // login exitoso
        }
        return null; // credenciales incorrectas
    }

    public boolean actualizarUsuario(Usuario usuario) {
        return usuarioDAO.editarUsuario(usuario);
    }

    public boolean eliminarUsuario(int id) {
        return usuarioDAO.eliminarUsuario(id);
        // true si se eliminó, false si no
    }

    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }


}
