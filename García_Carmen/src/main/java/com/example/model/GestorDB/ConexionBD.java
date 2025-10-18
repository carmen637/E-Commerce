package com.example.model.GestorDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static String url = "jdbc:mysql://localhost:3306/ecommerce";
    private static String user = "root";
    private static String password = "digiCar"; //Esta es mi contraseña para sql, en mi ordenador me funciona

public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
}


}
