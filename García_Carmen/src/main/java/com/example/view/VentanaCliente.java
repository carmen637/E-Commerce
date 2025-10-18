package com.example.view;

import com.example.model.Clases.Cliente;
import com.example.model.Clases.ClienteDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaCliente extends JFrame {
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton btnCargar, btnAgregar, btnModificar, btnEliminar;

    public VentanaCliente() {
        setTitle("Gestión de Clientes");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Nombre", "Email", "Teléfono", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaClientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);

        btnCargar = new JButton("Cargar clientes");
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCargar);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Listeners de los botones
        btnCargar.addActionListener(e -> cargarClientes());

        btnAgregar.addActionListener(e -> agregarCliente());

        btnModificar.addActionListener(e -> modificarCliente());

        btnEliminar.addActionListener(e -> eliminarCliente());
    }

    private void cargarClientes() {
        try {
            ClienteDB clienteDB = new ClienteDB();
            List<Cliente> clientes = clienteDB.listarClientes();
            modeloTabla.setRowCount(0);
            for (Cliente c : clientes) {
                Object[] fila = {
                        c.getIdCliente(),
                        c.getNombre(),
                        c.getEmail(),
                        c.getTelefono(),
                        c.getDireccion()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarCliente() {
        try {
            String nombre = JOptionPane.showInputDialog(this, "Nombre:");
            if (nombre == null || nombre.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null) email = "";

            String telefono = JOptionPane.showInputDialog(this, "Teléfono:");
            if (telefono == null) telefono = "";

            String direccion = JOptionPane.showInputDialog(this, "Dirección:");
            if (direccion == null) direccion = "";

            Cliente c = new Cliente(0, nombre, email, telefono, direccion);
            ClienteDB clienteDB = new ClienteDB();
            clienteDB.anadir(c);

            cargarClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para modificar");
            return;
        }
        try {
            int idCliente = (int) modeloTabla.getValueAt(fila, 0);

            String nombre = JOptionPane.showInputDialog(this, "Nombre:", modeloTabla.getValueAt(fila, 1));
            if (nombre == null || nombre.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog(this, "Email:", modeloTabla.getValueAt(fila, 2));
            if (email == null) email = "";

            String telefono = JOptionPane.showInputDialog(this, "Teléfono:", modeloTabla.getValueAt(fila, 3));
            if (telefono == null) telefono = "";

            String direccion = JOptionPane.showInputDialog(this, "Dirección:", modeloTabla.getValueAt(fila, 4));
            if (direccion == null) direccion = "";

            Cliente c = new Cliente(idCliente, nombre, email, telefono, direccion);
            ClienteDB clienteDB = new ClienteDB();
            clienteDB.actualizar(c);

            cargarClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar cliente:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar");
            return;
        }
        try {
            int idCliente = (int) modeloTabla.getValueAt(fila, 0);
            ClienteDB clienteDB = new ClienteDB();
            clienteDB.eliminar(idCliente);
            cargarClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

