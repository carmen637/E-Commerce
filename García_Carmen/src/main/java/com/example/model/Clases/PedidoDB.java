package com.example.model.Clases;

import com.example.model.GestorDB.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDB {

    public int insertarPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (id_cliente, Fecha_Pedido, Estado) VALUES (?, ?, ?)";
        int idGenerado = -1; //Todavía no existe el pedido, por eso se inicializa a -1

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pedido.getIdCliente());
            ps.setTimestamp(2, Timestamp.valueOf(pedido.getFechaPedido()));
            ps.setString(3, pedido.getEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) { //Obtiene el ID autoincremental que le dio la base de datos
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        }

        return idGenerado;
    }

    public void insertarDetalle(DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (ID_Pedido, ID_Producto, Cantidad, PrecioUnitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioUnitario());
            ps.executeUpdate();
        }
    }

    public List<Pedido> listarPedidos() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("ID_Pedido"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setFechaPedido(rs.getTimestamp("Fecha_Pedido").toLocalDateTime());
                p.setEstado(rs.getString("Estado"));

                lista.add(p);
            }
        }

        return lista;
    }

    public List<DetallePedido> listarDetalles(int idPedido) throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE ID_Pedido = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setIdDetalle(rs.getInt("ID_Detalle"));
                    d.setIdPedido(rs.getInt("ID_Pedido"));
                    d.setIdProducto(rs.getInt("ID_Producto"));
                    d.setCantidad(rs.getInt("Cantidad"));
                    d.setPrecioUnitario(rs.getDouble("PrecioUnitario"));
                    lista.add(d);
                }
            }
        }

        return lista;
    }
}

