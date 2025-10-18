package com.example.view;

import com.example.model.Clases.*;
import com.example.model.Clases.Carrito.ItemCarrito;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class VentanaPedidos extends JFrame {
    private JComboBox<Cliente> listaClientes;
    private JTable tablaProductos, tablaCarrito;
    private DefaultTableModel modelPr, modelCr; //En esta ventana tendremos dos partes diferenciadas, una tabla con los productos y otra con el carrito
    private JButton agregar, vaciar, quitar, realizarPedido;
    private JLabel total;
    private List<Producto> productos;
    private List<Cliente> clientes;
    private Carrito carrito;

    public VentanaPedidos() {
        setTitle("Gestion de Pedidos y Carrito");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        carrito = new Carrito();

        //Inicializamos los componentes de la parte del producto
        modelPr = new DefaultTableModel(new String[] { "ID", "Nombre", "Descripción", "Precio", "Stock" }, 0);
        tablaProductos = new JTable(modelPr);
        JScrollPane jspr = new JScrollPane(tablaProductos);

        //Inicializamos loc componentes de la parte del carrito
        modelCr = new DefaultTableModel(new String[] { "ID Producto", "Nombre", "Cantidad", "Precio Unitario" }, 0);
        tablaCarrito = new JTable(modelCr);
        JScrollPane jscr = new JScrollPane(tablaCarrito);


        agregar = new JButton("Agregar producto");
        vaciar = new JButton("Vaciar carrito");
        quitar = new JButton("Quitar del carrito");
        realizarPedido = new JButton("Realizar pedido");

        //Estructura panel de los productos
        JPanel panelPr = new JPanel(new BorderLayout());
        panelPr.add(new JLabel("Productos"), BorderLayout.NORTH);
        panelPr.add(jspr, BorderLayout.CENTER);
        panelPr.add(agregar, BorderLayout.SOUTH);

        //Estructura panel del carrito
        JPanel panelCr = new JPanel(new BorderLayout());
        panelCr.add(new JLabel("Tu carrito"), BorderLayout.NORTH);
        panelCr.add(jscr, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(quitar);
        panelBotones.add(vaciar);
        panelBotones.add(realizarPedido);
        panelCr.add(panelBotones, BorderLayout.SOUTH);

        //EStructura del panel de búsqueda del cliente situada arriba
        JPanel panelCliente = new JPanel();
        panelCliente.add(new JLabel("Cliente"));
        listaClientes = new JComboBox<>();
        panelCliente.add(listaClientes);

        total = new JLabel();
        JPanel panelTotal = new JPanel();
        panelTotal.add(total);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelCliente, BorderLayout.NORTH);

        // Usamos JSplitPane para que productos y carrito compartan bien el espacio
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelPr, panelCr);
        splitPane.setDividerLocation(450); // Ajusta el ancho inicial
        splitPane.setResizeWeight(0.5); // Ambos paneles se redimensionan equitativamente

        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(panelTotal, BorderLayout.SOUTH);

        cargarClientes();
        cargarProductos();

        //Asignamos los Listeners de los botones cada uno con su respectiva función
        agregar.addActionListener(e -> agregarAlCarrito());
        quitar.addActionListener(e -> quitarDelCarrito());
        vaciar.addActionListener(e -> vaciarCarrito());
        realizarPedido.addActionListener(e -> hacerPedido());

    }

    public void agregarAlCarrito() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto");
            return;
        }

        int id = (int) modelPr.getValueAt(fila, 0);
        String nombre = (String) modelPr.getValueAt(fila, 1);
        double precio = (double) modelPr.getValueAt(fila, 3);
        int stock = (int) modelPr.getValueAt(fila, 4);
        String cantitdadStr = JOptionPane.showInputDialog(this, "Cantidad");
        if (cantitdadStr == null) {
            return; //Si la cantidad es nula, da un error
        }

        int cantidad = Integer.parseInt(cantitdadStr);
        if (cantidad <= 0 || cantidad > stock) {
            JOptionPane.showMessageDialog(this, "Cantidad no válida");
            return;
        }

        Producto p = productos.get(fila);
        carrito.agregarProducto(p, cantidad);
        actualizarCarrito();
    }

    public void quitarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto");
            return;
        }

        int id = (int) modelCr.getValueAt(fila, 0);
        carrito.quitar(id); //quitar es un método de la clase itemCarrito
        actualizarCarrito();
    }

    public void hacerPedido() {
        Cliente cliente = (Cliente) listaClientes.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente");
            return;
        }
        if (carrito.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrito vacío");
            return;
        }

        try {
            Pedido pedido = new Pedido();
            pedido.setIdCliente(cliente.getIdCliente());
            pedido.setFechaPedido(LocalDateTime.now());
            pedido.setEstado("Pendiente");

            PedidoDB db = new PedidoDB();
            int idPedido = db.insertarPedido(pedido);

            for (ItemCarrito item : carrito.getItems()) {
                DetallePedido dp = new DetallePedido();
                dp.setIdPedido(idPedido);
                dp.setIdProducto(item.getProducto().getIdProducto());
                dp.setCantidad(item.getCantidad());
                dp.setPrecioUnitario(item.getProducto().getPrecio());

                db.insertarDetalle(dp);
            }

            JOptionPane.showMessageDialog(this, "Pedido realizado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los productos");
        }

    }

    public void vaciarCarrito() { 
        carrito.vaciar();
        actualizarCarrito();
    }

    public void actualizarCarrito() {
        modelCr.setRowCount(0);
        double suma = 0;

        for (ItemCarrito item : carrito.getItems()) {
            modelCr.addRow(new Object[] {
                    item.getProducto().getIdProducto(),
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    item.getProducto().getPrecio()
            });
            suma = suma + item.getSubtotal();

        }
        total.setText("Total: " + suma + " €");
    }

    private void cargarClientes() {
        try {
            clientes = new ClienteDB().listarClientes();
            listaClientes.removeAllItems(); //Vacia el combobox para que cuando carguemos los datos no se dupliquen
            for (Cliente c : clientes) {
                listaClientes.addItem(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            productos = new ProductoDB().ListarProductos();
            modelPr.setRowCount(0);
            for (Producto p : productos) {
                modelPr.addRow(new Object[] {
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getStock()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}