package com.example.model.Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.GestorDB.ConexionBD;

public class ProductoDB {
      public List<Producto> ListarProductos() throws SQLException { 
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("ID_Producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setIdCategoria(rs.getInt("ID_Categoria"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
                
            }
            
        }

        return lista;
    }
    public void anadir(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, ID_Categoria, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setDouble(3, p.getPrecio());
        ps.setInt(4, p.getIdCategoria());
        ps.setInt(5, p.getStock());

        ps.executeUpdate();
    }

    }
    public void actualizar(Producto p) throws SQLException {
    String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, ID_Categoria=?, stock=? WHERE ID_Producto=?";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setDouble(3, p.getPrecio());
        ps.setInt(4, p.getIdCategoria());
        ps.setInt(5, p.getStock());
        ps.setInt(6, p.getIdProducto());

        ps.executeUpdate();
        }
    }
    public void eliminar(int idProducto) throws SQLException {
    String sql = "DELETE FROM productos WHERE ID_Producto=?";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idProducto);

        ps.executeUpdate();
    }
}

}

