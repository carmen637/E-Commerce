package com.example.model.Clases;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private LocalDateTime fechaPedido;
    private String estado;
    private List<DetallePedido> detalles; // Lista de productos del pedido

    public Pedido() {
        
    }

    public Pedido(int idPedido, int idCliente, LocalDateTime fechaPedido, String estado) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}

