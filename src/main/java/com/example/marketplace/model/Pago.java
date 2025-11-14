package com.example.marketplace.model;

import java.sql.Timestamp;

public class Pago {
    private int id_pago;
    private int id_pedido;
    private String metodo_pago;
    private double monto;
    private String estado_pago;
    private Timestamp fecha_pago;

    // Campos adicionales para tarjeta
    private String numero_tarjeta;
    private String fecha_caducidad;
    private String cvv;
    private String nombre_titular;

    // Campos para dirección de envío
    private String nombre_completo;
    private String direccion;
    private String ciudad;
    private String codigo_postal;

    // Constructores
    public Pago() {
        this.estado_pago = "Completado";
    }

    public Pago(int id_pedido, String metodo_pago, double monto) {
        this.id_pedido = id_pedido;
        this.metodo_pago = metodo_pago;
        this.monto = monto;
        this.estado_pago = "Completado";
    }

    // Getters y Setters
    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }

    public Timestamp getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Timestamp fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public String getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(String fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNombre_titular() {
        return nombre_titular;
    }

    public void setNombre_titular(String nombre_titular) {
        this.nombre_titular = nombre_titular;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id_pago=" + id_pago +
                ", id_pedido=" + id_pedido +
                ", metodo_pago='" + metodo_pago + '\'' +
                ", monto=" + monto +
                ", estado_pago='" + estado_pago + '\'' +
                ", fecha_pago=" + fecha_pago +
                '}';
    }
}