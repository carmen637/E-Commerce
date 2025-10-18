package com.example.view;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import com.example.model.Clases.Producto;
import com.example.model.Clases.ProductoDB;

public class VentanaProducto extends JFrame {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnCargar, Agregar, Modificar, Eliminar;
    private TableRowSorter<DefaultTableModel> sorter; //Para ordenar los datos una tabla automáticamente y que funciona con DefaultTableModel, ya que es donde estan los datos
    private JTextField buscarTxt;

    public VentanaProducto() {
        setTitle("Gestión de Productos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //Para centrar la ventana en la pantalla
        getContentPane().setLayout(new BorderLayout());

        // Columnas para la tabla
        String[] columnas = { "ID", "Nombre", "Descripción", "Precio", "ID Categoría", "Stock" };

        //Inicializamos los componentes junto con la llamada de los listeners con funciones
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tablaProductos.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        Agregar = new JButton("Agregar");
        Agregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anadir();
            }
        });

        Modificar = new JButton("Modificar");
        Modificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizar();
            }
        });

        Eliminar = new JButton("Eliminar");
        Eliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        btnCargar = new JButton("Cargar productos");
        btnCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarProductos();
            }
        });

        //Buscador, cuando se escribe o se borra en el TextField se le aplica el método filtrar
        buscarTxt = new JTextField(20);
        buscarTxt.getDocument().addDocumentListener(new DocumentListener() { //Usamos Document Listener para trabajar sobre los cambios de texto del JTextField. getDocument lo usamos para dar acceso al contenido del JTextField
            public void insertUpdate(DocumentEvent e) { 
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(DocumentEvent e) { //Document te obliga a poner los tres metodos, aunque este no lo use
                
            }
        });
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Buscar Producto: "));
        panelBusqueda.add(buscarTxt);

        getContentPane().add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCargar);
        panelBoton.add(Agregar);
        panelBoton.add(Modificar);
        panelBoton.add(Eliminar);

       
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBoton, BorderLayout.SOUTH);
    }

    private void filtrar() {
    String texto = buscarTxt.getText();
    if (texto.trim().length() == 0) {
        sorter.setRowFilter(null);
    } else {
        // Filtra por nombre o descripción que contengan el texto, ignorando mayúsculas y minúsculas. ?i es el modificador que hace que la búsqueda sea insensible a mayúsculas y minúsculas
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1, 2));
    }
}

    private void anadir() {
        try {
            //Pedimos los datos de los textField que saldrán de los JOptionPane para después guardarlos en el objeto de tipo Producto y ProductoDB
            String nombre = JOptionPane.showInputDialog(this, "Nombre");
            if (nombre == null || nombre.isEmpty()) {
                return;
            }
            String descripcion = JOptionPane.showInputDialog(this, "Descripción");
            if (descripcion == null) {
                descripcion = "";
            }
            String precioStr = JOptionPane.showInputDialog(this, "Precio:");
            double precio = Double.parseDouble(precioStr);

            String idCatStr = JOptionPane.showInputDialog(this, "ID Categoría:");
            int idCategoria = Integer.parseInt(idCatStr);

            String stockStr = JOptionPane.showInputDialog(this, "Stock:");
            int stock = Integer.parseInt(stockStr);

            Producto p = new Producto(0, nombre, descripcion, precio, idCategoria, stock);
            ProductoDB pdb = new ProductoDB();
            pdb.anadir(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = tablaProductos.getSelectedRow(); //La fila que elija el usuario con selectedRow será la que la elimine
        if (fila == -1) { //Si no hay ninguna fila seleccionada, saltará un mensaje para elegir una fila
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminarlo");
            return;
        }

        try { //Cogemos el id del producto seleccionado para eliminarlo de la base de datos y lueo cargamos los productos con la lista actualizados
            int idProducto = (int) modeloTabla.getValueAt(fila, 0);
            ProductoDB pdb = new ProductoDB();
            pdb.eliminar(idProducto);

            cargarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el producto" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminarlo");
            return;
        }

        try {
            int idProducto = (int) modeloTabla.getValueAt(fila, 0);

            String nombre = JOptionPane.showInputDialog(this, "Nombre:", modeloTabla.getValueAt(fila, 1)); //Para actualizar un producto también preguntará por JOptionPane el cambio
            if (nombre.isEmpty())
                return;

            String descripcion = JOptionPane.showInputDialog(this, "Descripción:", modeloTabla.getValueAt(fila, 2));
            if (descripcion == null)
                descripcion = "";

            String precioStr = JOptionPane.showInputDialog(this, "Precio:", modeloTabla.getValueAt(fila, 3));
            double precio = Double.parseDouble(precioStr);

            String idCatStr = JOptionPane.showInputDialog(this, "ID Categoría:", modeloTabla.getValueAt(fila, 4));
            int idCategoria = Integer.parseInt(idCatStr);

            String stockStr = JOptionPane.showInputDialog(this, "Stock:", modeloTabla.getValueAt(fila, 5));
            int stock = Integer.parseInt(stockStr);

            Producto p = new Producto(idProducto, nombre, descripcion, precio, idCategoria, stock);
            ProductoDB pdb = new ProductoDB();
            pdb.actualizar(p);

            cargarProductos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar producto:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void cargarProductos() {
        //Método para cargar los productos de la base de datos que llama al método que los lista que está en ProductoDB
        try {
            ProductoDB pdb = new ProductoDB();
            List<Producto> productos = pdb.ListarProductos();

            // Limpiar tabla antes de cargar
            modeloTabla.setRowCount(0);

            for (Producto p : productos) {
                Object[] fila = {
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getIdCategoria(),
                        p.getStock()
                };
                modeloTabla.addRow(fila);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
