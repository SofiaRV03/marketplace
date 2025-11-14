package com.example.marketplace.dao;

import java.sql.*;
import java.io.File;

public class InicializadorDB {

    public static void inicializar() {
        try {
            // Verificar si las tablas existen, no solo el archivo
            if (!existenTablas()) {
                System.out.println("=== CREANDO BASE DE DATOS ===");
                crearBaseDatos();
            } else {
                System.out.println("✓ Base de datos ya inicializada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar/crear base de datos:");
            e.printStackTrace();
        }
    }

    private static boolean existenTablas() throws SQLException {
        File dbFile = new File("data/marketplace.db");
        if (!dbFile.exists()) {
            return false;
        }

        try (Connection conn = conexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='usuario'")) {
            return rs.next(); // Si hay resultado, la tabla existe
        } catch (SQLException e) {
            // Si falla, probablemente el archivo está corrupto
            return false;
        }
    }

    private static void crearBaseDatos() {
        try (Connection conn = conexionDB.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("→ Creando tablas...");

            // 1. Tabla usuario
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS usuario (" +
                            "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nombre VARCHAR(150) NOT NULL," +
                            "correo VARCHAR(150) UNIQUE," +
                            "contrasena VARCHAR(255) NOT NULL," +
                            "tipo VARCHAR(50) NOT NULL DEFAULT 'cliente'," +
                            "fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                            "direccion VARCHAR(255) NOT NULL DEFAULT 'Sin dirección'" +
                            ")"
            );

            // 2. Tabla categorias
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS categorias (" +
                            "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nombre VARCHAR(100)" +
                            ")"
            );

            // 3. Tabla producto
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS producto (" +
                            "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nombre VARCHAR(150) NOT NULL," +
                            "descripcion TEXT," +
                            "precio DECIMAL(10,2) NOT NULL," +
                            "stock INTEGER NOT NULL DEFAULT 0," +
                            "imagen VARCHAR(200) NOT NULL DEFAULT 'Sin imágen'" +
                            ")"
            );

            // 4. Tabla carrito
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS carrito (" +
                            "id_carrito INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "id_usuario INTEGER," +
                            "fecha_creacion DATE," +
                            "estado VARCHAR(50) NOT NULL DEFAULT 'activo'," +
                            "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)" +
                            ")"
            );

            // 5. Tabla carrito_producto
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS carrito_producto (" +
                            "id_carrito INTEGER NOT NULL," +
                            "id_producto INTEGER NOT NULL," +
                            "cantidad INTEGER," +
                            "subtotal DECIMAL(10,2)," +
                            "PRIMARY KEY (id_carrito, id_producto)," +
                            "FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito)," +
                            "FOREIGN KEY (id_producto) REFERENCES producto(id_producto)" +
                            ")"
            );

            // 6. Tabla producto_categoria
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS producto_categoria (" +
                            "id_producto INTEGER NOT NULL," +
                            "id_categoria INTEGER NOT NULL," +
                            "PRIMARY KEY (id_producto, id_categoria)," +
                            "FOREIGN KEY (id_producto) REFERENCES producto(id_producto) ON DELETE CASCADE," +
                            "FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE CASCADE" +
                            ")"
            );

            // 7. Tabla pedido
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS pedido (" +
                            "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "id_usuario INTEGER," +
                            "fecha_pedido DATE," +
                            "total DECIMAL(10,2)," +
                            "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)" +
                            ")"
            );

            // 8. Tabla producto_pedido
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS producto_pedido (" +
                            "id_pedido INTEGER NOT NULL," +
                            "id_producto INTEGER NOT NULL," +
                            "cantidad INTEGER," +
                            "precio_unitario DECIMAL(10,2)," +
                            "subtotal DECIMAL(10,2)," +
                            "PRIMARY KEY (id_pedido, id_producto)," +
                            "FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)," +
                            "FOREIGN KEY (id_producto) REFERENCES producto(id_producto)" +
                            ")"
            );

            // 9. Tabla pago
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS pago (" +
                            "id_pago INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "id_pedido INTEGER," +
                            "monto DECIMAL(10,2)," +
                            "metodo VARCHAR(100)," +
                            "FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)" +
                            ")"
            );

            // 10. Tabla promocion
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS promocion (" +
                            "id_promocion INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "codigo VARCHAR(50)," +
                            "descripcion TEXT," +
                            "valor_descuento DECIMAL(10,2)" +
                            ")"
            );

            // 11. Tabla usuario_promocion
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS usuario_promocion (" +
                            "id_usuario INTEGER NOT NULL," +
                            "id_promocion INTEGER NOT NULL," +
                            "usado INTEGER," +
                            "PRIMARY KEY (id_usuario, id_promocion)," +
                            "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)," +
                            "FOREIGN KEY (id_promocion) REFERENCES promocion(id_promocion)" +
                            ")"
            );

            // 12. Tabla resena
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS resena (" +
                            "id_resena INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "id_usuario INTEGER," +
                            "id_producto INTEGER," +
                            "comentario TEXT," +
                            "calificacion INTEGER," +
                            "FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)," +
                            "FOREIGN KEY (id_producto) REFERENCES producto(id_producto)" +
                            ")"
            );

            System.out.println("  ✓ Tablas creadas correctamente");

            // Insertar datos iniciales
            System.out.println("→ Insertando datos iniciales...");
            insertarDatosIniciales(stmt);

            System.out.println("✅ BASE DE DATOS CREADA EXITOSAMENTE");

        } catch (SQLException e) {
            System.err.println("❌ Error al crear la base de datos:");
            e.printStackTrace();
        }
    }

    private static void insertarDatosIniciales(Statement stmt) throws SQLException {

        // Usuarios
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(11, 'Taylor Swift', 'taylor@example.com', '1313', 'cliente', '2025-09-15 02:30:26', 'Av. Principal 123, New York')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(16, 'Gracie Abrams', 'gracie@example.com', '123456', 'cliente', '2025-09-15 03:05:17', 'Calle Luna 45, Los Ángeles')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(18, 'Charli xcx', 'brat@gmail.com', 'apple360', 'cliente', '2025-09-15 03:24:11', '742 Evergreen Terrace, Springfield')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(19, 'Sofia Restrepo', 'sofiarestrepo072005@gmail.com', '132456', 'admin', '2025-09-18 04:48:23', 'Calle 10 #25-50, Medellín')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(21, 'BlackPink', 'blackpink@gmail.com', 'negrorosa', 'cliente', '2025-09-18 05:14:25', 'Av. Siempre Viva 742, Seúl')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(22, 'Alba Lucia Villegas', 'albalucia@hotmail.com', 'teamosofi', 'cliente', '2025-09-19 03:15:18', 'Calle 8 #12-115')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(23, 'Allison Restrepo', 'allisonmariana@gmail.com', 'allison123', 'cliente', '2025-10-16 03:54:44', 'calle 3 #23 45')");
        stmt.execute("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, tipo, fecha_registro, direccion) VALUES " +
                "(24, 'Alba Lucia Villegas', 'alba@hotmail.com', '123456', 'cliente', '2025-10-18 03:42:35', 'calle 8 #12-115')");

        // Categorias
        String[] categorias = {"Ropa", "Calzado", "Electrónica", "Hogar", "Cocina", "Deporte", "Joyería", "Taylor Swift", "Infantil", "Animales", "Mascotas", "Musica"};
        for (int i = 0; i < categorias.length; i++) {
            stmt.execute("INSERT INTO categorias (id_categoria, nombre) VALUES (" + (i+1) + ", '" + categorias[i] + "')");
        }

        // Productos
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(1, 'Lover', 'Album de Taylor Swift', 250000.00, 19, 'lover.png')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(2, 'Folklore', 'Album de Taylor Swift', 150000.00, 8, 'folklore.png')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(4, 'Peluche', 'Peluche de Enter', 50000.00, 18, 'enter.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(5, 'Taza Rosada', 'Disfruta tus bebidas favoritas con estilo', 75000.00, 10, 'taza.png')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(8, 'Estuche Celular', 'Estuche transparente con gatito 3D', 25000.00, 30, 'estuche_gato_1.png')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(10, 'Audífonos Bluetooth Inalámbricos', 'Audífonos TWS con cancelación de ruido', 45900.00, 150, '514vwxueu7L._UF1000,1000_QL80_.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(11, 'Cargador Rápido 3 Puertos USB', 'Cargador de pared 102W', 32500.00, 200, 'cargador.jpeg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(12, 'Mouse Inalámbrico RGB rosado', 'Mouse gamer recargable', 38700.00, 80, 'mouse.jpeg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(13, 'Lámpara LED Escritorio Regulable', 'Lámpara LED touch con 3 niveles', 42300.00, 120, 'lampara.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(14, 'Organizador Giratorio Especias', 'Torre giratoria para 20 frascos', 54900.00, 110, 'especias.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(15, 'Juego de Sartenes Antiadherentes 3 Piezas', 'Set de sartenes de aluminio', 89500.00, 65, 'sartenes.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(18, 'Set 12 Brochas Maquillaje Profesional', 'Brochas suaves con estuche', 34900.00, 160, 'brochas.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(19, 'Rodillo Jade Facial Masajeador', 'Rodillo facial de piedra natural', 27800.00, 190, 'rodillo.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(20, 'Espejo LED Maquillaje 10X', 'Espejo táctil con luz LED regulable', 52300.00, 95, 'espejo led.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(21, 'Plancha Alisadora Cerámica Profesional', 'Plancha para cabello', 78900.00, 70, 'plancha.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(25, 'Muñeca Interactiva con Accesorios', 'Muñeca de 30cm con 10 accesorios', 54600.00, 110, 'muñeca}.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(26, 'Banda Elástica Resistencia Set 5 Niveles', 'Set de bandas de ejercicio', 34900.00, 150, 'bandas.jpg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(27, 'Botella Agua Deportiva de 1 litro', 'Botella motivacional', 25600.00, 200, 'termo.jpeg')");
        stmt.execute("INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, imagen) VALUES " +
                "(28, 'Tapete Yoga Antideslizante 6mm', 'Colchoneta para yoga', 54900.00, 110, 'tapete.jpeg')");

        // Carritos
        stmt.execute("INSERT INTO carrito (id_carrito, id_usuario, fecha_creacion, estado) VALUES (1, 11, '2025-10-07', 'finalizado')");
        stmt.execute("INSERT INTO carrito (id_carrito, id_usuario, fecha_creacion, estado) VALUES (2, 11, '2025-10-15', 'finalizado')");
        stmt.execute("INSERT INTO carrito (id_carrito, id_usuario, fecha_creacion, estado) VALUES (8, 11, '2025-10-17', 'activo')");

        // Carrito_producto
        stmt.execute("INSERT INTO carrito_producto (id_carrito, id_producto, cantidad, subtotal) VALUES (1, 4, 1, 50000.00)");
        stmt.execute("INSERT INTO carrito_producto (id_carrito, id_producto, cantidad, subtotal) VALUES (2, 4, 1, 50000.00)");

        // Producto_categoria
        int[][] relaciones = {{1,8},{1,12},{2,8},{2,12},{4,3},{4,4},{5,4},{5,5},{8,3},{10,3},{10,12},{11,3},{12,3},{13,3},{13,4},{14,4},{14,5},{15,5},{20,4},{25,9},{26,6},{27,6},{28,6}};
        for (int[] rel : relaciones) {
            stmt.execute("INSERT INTO producto_categoria (id_producto, id_categoria) VALUES (" + rel[0] + ", " + rel[1] + ")");
        }

        System.out.println("  ✓ Datos iniciales insertados");
    }
}