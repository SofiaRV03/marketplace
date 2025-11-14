package com.example.marketplace.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

//public class conexionDB {
//
//    private static final String URL = "jdbc:mysql://localhost:3306/marketplace";
//    private static final String USUARIO = "root";
//    private static final String CONTRASENA = "";
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
//    }
//}


import java.sql.*;




import java.sql.*;
import java.io.File;

public class conexionDB {

    private static final String DB_DIR = "data";
    private static final String DB_FILE = "marketplace.db";
    private static final String URL = "jdbc:sqlite:" + DB_DIR + "/" + DB_FILE;

    static {
        try {
            // Crear carpeta data si no existe
            File dataDir = new File(DB_DIR);
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdir();
                if (created) {
                    System.out.println("✓ Carpeta 'data' creada");
                }
            }

            // Cargar driver SQLite
            Class.forName("org.sqlite.JDBC");
            System.out.println("✓ Driver SQLite cargado correctamente");

        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error: No se encontró el driver SQLite");
            System.err.println("  Asegúrate de tener la dependencia sqlite-jdbc en tu pom.xml");
            throw new RuntimeException("No se pudo cargar el driver SQLite", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // Habilitar foreign keys en SQLite (importante!)
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON");
        stmt.close();
        return conn;
    }

    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean existeBaseDatos() {
        File dbFile = new File(DB_DIR + "/" + DB_FILE);
        return dbFile.exists();
    }
}
//    public static void main(String[] args) {
//
//        try{
//            Connection connection = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/marketplace",
//                    "root",
//                    "");
//
//            Statement statement = connection.createStatement();
//            ResultSet resultSet= statement.executeQuery("SELECT * FROM usuario");
//
//            while (resultSet.next()){
//
//                System.out.println(resultSet.getString("nombre"));
//                System.out.println(resultSet.getString("correo"));
//                System.out.println(resultSet.getString("contrasena"));
//            }
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//
//
//
//    }

