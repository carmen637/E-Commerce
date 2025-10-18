package com.example;

import javax.swing.SwingUtilities;

import com.example.view.*;

public class Main {
    public static void main( String[] args ){
         SwingUtilities.invokeLater(() -> {
            new VentanaProducto().setVisible(true);
            new VentanaCliente().setVisible(true);
            new VentanaPedidos().setVisible(true);
        });
    }
}
