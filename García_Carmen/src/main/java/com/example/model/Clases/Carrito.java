package com.example.model.Clases;

import java.util.*;

public class Carrito {
    private List<ItemCarrito> items;

    public Carrito() {
        items = new ArrayList<>(); //Cuando se crea un carrito, la lista de items esta vacía
    }
    //Agrega productos al carrito
    public void agregarProducto(Producto producto, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) { //Si el producto ya esta en el carrito, solo suma la cantidad que pregunta en el JOptionPane
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(producto, cantidad)); //Sino, crea un nuevo objeto de tipo ItemCarrito
    }

    public void vaciar() {
        items.clear(); //Limpia el carrito
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public List<ItemCarrito> getItems() {
        return items;
    }
    public void quitar(int idProducto){
        items.removeIf(item -> item.getProducto().getIdProducto() == idProducto); //removeIf elimina el objeto del carrito seleccionado
    }

    // Clase interna ItemCarrito, osea, los objetos que están dentro del carrito
    public static class ItemCarrito {
        private Producto producto;
        private int cantidad;

        public ItemCarrito(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public double getSubtotal() {
            return producto.getPrecio() * cantidad;
        }

        // Getters y setters
        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }
}

