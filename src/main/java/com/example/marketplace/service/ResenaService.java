package com.example.marketplace.service;

import com.example.marketplace.dao.ResenaDAO;
import com.example.marketplace.model.Resena;

import java.util.List;

public class ResenaService {

    private ResenaDAO resenaDAO;

    public ResenaService() {
        this.resenaDAO = new ResenaDAO();
    }

    public Resena crearResena(int idUsuario, int idProducto, String comentario, int calificacion) {

        if (calificacion < 1 || calificacion > 5) {
            System.err.println("La calificación debe estar entre 1 y 5");
            return null;
        }

        if (resenaDAO.usuarioYaResenoProducto(idUsuario, idProducto)) {
            System.err.println("Ya has reseñado este producto anteriormente");
            return null;
        }

        Resena r = new Resena(idUsuario, idProducto, comentario, calificacion);
        return resenaDAO.crearResena(r);
    }

    public List<Resena> obtenerResenasPorProducto(int idProducto) {
        return resenaDAO.listarResenasPorProducto(idProducto);
    }

    public List<Resena> obtenerResenasPorUsuario(int idUsuario) {
        return resenaDAO.listarResenasPorUsuario(idUsuario);
    }

    public boolean puedeResenar(int idUsuario, int idProducto) {
        return !resenaDAO.usuarioYaResenoProducto(idUsuario, idProducto);
    }

    public Resena obtenerResenaPorId(int idResena) {
        return resenaDAO.buscarPorId(idResena);
    }

    public boolean eliminarResena(int idResena) {
        return resenaDAO.eliminarResena(idResena);
    }

    public EstadisticasResena obtenerEstadisticas(int idProducto) {
        return new EstadisticasResena(
                resenaDAO.obtenerPromedioCalificacion(idProducto),
                resenaDAO.contarResenas(idProducto)
        );
    }

    public static class EstadisticasResena {
        private double promedio;
        private int total;

        public EstadisticasResena(double promedio, int total) {
            this.promedio = promedio;
            this.total = total;
        }

        public double getPromedio() { return promedio; }
        public int getTotal() { return total; }
    }

    public double obtenerPromedioCalificacion(int idProducto) {
        return resenaDAO.obtenerPromedioCalificacion(idProducto);
    }
}
