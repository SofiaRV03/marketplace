package com.example.marketplace;

public class Main {
    public static void main(String[] args) {
        // Opción 1: Modo consola (para pruebas)
        if (args.length > 0 && args[0].equals("--console")) {
            runConsoleMode();
        } else {
            // Opción 2: Modo GUI (por defecto)
            MarketplaceApp.main(args);
        }
    }

    private static void runConsoleMode() {
        System.out.println("=== Marketplace - Modo Consola ===");
        System.out.println("Para iniciar el modo gráfico, ejecuta sin argumentos");
        // Aquí tu código de consola existente
    }
}
//package com.example.marketplace;
//
//import com.example.marketplace.model.*;
//import com.example.marketplace.service.*;
//import com.example.marketplace.utils.ValidarProducto;
//import com.example.marketplace.utils.ValidarUsuario;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//
//public class Main {
//    private static Usuario usuarioLogueado = null;
//    public static void main(String[] args) {
//        UsuarioService usuarioService = new UsuarioService();
//        ProductoService productoService = new ProductoService();
//        CategoriaService categoriaService = new CategoriaService();
//        Scanner sc = new Scanner(System.in);
//        int opcion;
//
//        do {
//            System.out.println("\n=== Menú de Usuarios ===");
//            System.out.println("1. Registrar usuario");
//            System.out.println("2. Listar usuarios");
//            System.out.println("3. Login");
//            System.out.println("4. Editar usuario");
//            System.out.println("5. Eliminar usuario");
//            System.out.println("6. Registrar producto");
//            System.out.println("7. Listar productos");
//            System.out.println("8. Buscar producto por nombre");
//            System.out.println("9. Buscar producto por categoria");
//            System.out.println("10. Editar producto");
//            System.out.println("11. Eliminar producto");
//            System.out.println("12. Listar Categorias");
//            System.out.println("13. ver carrito");
//            System.out.println("14. Agregar producto al carrito");
//            System.out.println("15. Eliminar producto del carrito");
//            System.out.println("16. vaciar carrito");
//            System.out.println("17. Finalizar compra");
//            System.out.println("18. Ver perfil");
//            System.out.println("0. Salir");
//            System.out.print("Elige una opción: ");
//            opcion = sc.nextInt();
//            sc.nextLine();
//
//            switch (opcion) {
//                case 1 -> {
//                    String nombre,correo,contrasena,error;
//                    do{
//                        System.out.print("Nombre: ");
//                        nombre = sc.nextLine().trim();
//                        error= ValidarUsuario.validarNombre(nombre);
//                        if(error!=null) System.out.println(error);
//                    }while(error!=null);
//
//
//                    do{
//                        System.out.print("Correo: ");
//                        correo = sc.nextLine().trim();
//                        error= ValidarUsuario.validarCorreo(correo);
//                        if(error!=null) System.out.println(error);
//                    }while(error!=null);
//
//                    do{
//                        System.out.print("Contrasena: ");
//                        contrasena = sc.nextLine().trim();
//                        error= ValidarUsuario.validarContrasena(contrasena);
//                        if(error!=null) System.out.println(error);
//                    } while(error!=null);
//
//
//                    Usuario creado=usuarioService.registrarUsuario(nombre, correo, contrasena);
//
//                    if(creado!=null){
//                        System.out.println("Usuario registrado correctamente");
//                    }else{
//                        System.out.println("Usuario no registrado");
//                    }
//                }
//
//                case 2 -> {
//                    List<Usuario> usuarios = usuarioService.obtenerUsuarios();
//                    if (usuarios.isEmpty()) {
//                        System.out.println("No hay usuarios registrados");
//                    } else {
//                        System.out.println("\n=== Lista de usuarios ===");
//                        for (Usuario u : usuarios) {
//                            System.out.println(u.getId() + " | " + u.getNombre() + " - " + u.getCorreo() + " - " + u.getTipo() + " - " + u.getDireccion());
//                        }
//                    }
//                }
//                case 3 -> {
//                    System.out.print("Correo: ");
//                    String correo = sc.nextLine();
//                    System.out.print("Contraseña: ");
//                    String contrasena = sc.nextLine();
//
//                    Usuario u = usuarioService.login(correo, contrasena);
//                    if (u != null) {
//                        usuarioLogueado=u;
//                        System.out.println("Login exitoso: " + u.getNombre() + " (" + u.getTipo() + ")");
//                    } else {
//                        System.out.println("Credenciales incorrectas");
//                    }
//                }
//                case 4 -> {
//                    Usuario usuarioExistente = null;
//                    int id;
//                    do {
//                        System.out.print("ID del usuario a editar: ");
//                        id = sc.nextInt();
//                        sc.nextLine();
//
//                        usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
//                        if (usuarioExistente == null) {
//                            System.out.println("Usuario con el id " + id + " no encontrado");
//                        }
//                    } while (usuarioExistente == null);
//
//                    System.out.println("\nDatos actuales del Usuario:");
//                    System.out.println("Nombre: "+usuarioExistente.getNombre());
//                    System.out.println("Correo: "+usuarioExistente.getCorreo());
//                    System.out.println("contraseña: "+usuarioExistente.getContrasena());
//                    System.out.println("Tipo: "+usuarioExistente.getTipo());
//                    System.out.println("Direccion: "+usuarioExistente.getDireccion());
//
//
//                    System.out.println("\nDejar vacios los campos que no deseas cambiar");
//
//                    // --- Validar Nombre ---
//                    String nombre;
//                    do {
//                        System.out.print("Nuevo nombre: ");
//                        nombre = sc.nextLine();
//                        if (nombre.isEmpty()) break; // mantiene el anterior
//                        String error = ValidarUsuario.validarNombre(nombre);
//                        if (error == null) {
//                            usuarioExistente.setNombre(nombre);
//                            break;
//                        } else {
//                            System.out.println(error + " Inténtalo de nuevo.");
//                        }
//                    } while (true);
//
//                    // --- Validar Correo ---
//                    String correo;
//                    do {
//                        System.out.print("Nuevo correo: ");
//                        correo = sc.nextLine();
//                        if (correo.isEmpty()) break; // mantiene el anterior
//                        String error = ValidarUsuario.validarCorreo(correo);
//                        if (error == null) {
//                            usuarioExistente.setCorreo(correo);
//                            break;
//                        } else {
//                            System.out.println(error + " Inténtalo de nuevo.");
//                        }
//                    } while (true);
//
//                    // --- Validar Contraseña ---
//                    String contrasena;
//                    do {
//                        System.out.print("Nueva contraseña: ");
//                        contrasena = sc.nextLine();
//                        if (contrasena.isEmpty()) break; // mantiene la anterior
//                        String error = ValidarUsuario.validarContrasena(contrasena);
//                        if (error == null) {
//                            usuarioExistente.setContrasena(contrasena);
//                            break;
//                        } else {
//                            System.out.println(error + " Inténtalo de nuevo.");
//                        }
//                    } while (true);
//
//                    // --- Tipo (sin validación especial, solo opcional) ---
//                    System.out.print("Nuevo tipo: ");
//                    String tipo = sc.nextLine();
//                    if (!tipo.isEmpty()) {
//                        usuarioExistente.setTipo(tipo);
//                    }
//
//
//                    System.out.print("Nueva Direccion: ");
//                    String direccion = sc.nextLine();
//                    if (!direccion.isEmpty()) {
//                        usuarioExistente.setDireccion(direccion);
//                    }
//
//
//                    boolean actualizado = usuarioService.actualizarUsuario(usuarioExistente);
//
//                    if (actualizado) {
//                        System.out.println("Usuario actualizado con éxito");
//                    } else {
//                        System.out.println("No se pudo actualizar el usuario con id=" + id);
//                    }
//                }
//                case 5 -> {
//                    System.out.print("ID del usuario a eliminar: ");
//                    int id = sc.nextInt();
//                    sc.nextLine();
//
//                    boolean eliminado = usuarioService.eliminarUsuario(id);
//                    if (eliminado) {
//                        System.out.println("Usuario eliminado");
//                    } else {
//                        System.out.println("No se encontró el usuario con id=" + id);
//                    }
//
//                }
//
//                case 6 -> {
//                    String nombre,descripcion,imagen,error;
//                    Double precio;
//                    List<Integer> categorias = new ArrayList<>();
//                    int stock;
//                    do{
//                        System.out.print("Nombre: ");
//                        nombre = sc.nextLine().trim();
//                        error= ValidarProducto.validarNombre(nombre);
//                        if(error!=null) System.out.println(error);
//                    }while(error!=null);
//
//
//                    do{
//                        System.out.print("Descripcion: ");
//                        descripcion = sc.nextLine().trim();
//                        error= ValidarProducto.validarDescripcion(descripcion);
//                        if(error!=null) System.out.println(error);
//                    }while(error!=null);
//
//                    do {
//                        System.out.print("Precio: ");
//                        while(!sc.hasNextDouble()) {
//                            System.out.println("Por favor ingrese un número válido");
//                            sc.next();
//                        }
//                        precio = sc.nextDouble();
//                        sc.nextLine(); // Limpiar buffer
//                        error = ValidarProducto.validarPrecio(precio);
//                        if(error != null) System.out.println(error);
//                    } while(error != null);
//
//                    do {
//                        System.out.print("Stock: ");
//                        while(!sc.hasNextInt()) {
//                            System.out.println("Por favor ingrese un número entero válido");
//                            sc.next();
//                        }
//                        stock = sc.nextInt();
//                        sc.nextLine(); // Limpiar buffer
//                        error = ValidarProducto.validarStock(stock);
//                        if(error != null) System.out.println(error);
//                    } while(error != null);
//
//                    do {
//                        System.out.println("\n=== CATEGORÍAS DISPONIBLES ===");
//                        List<Categoria> listaCategoriasDisponibles = categoriaService.obtenerCategorias();
//
//                        if(listaCategoriasDisponibles != null && !listaCategoriasDisponibles.isEmpty()) {
//                            for(Categoria cat : listaCategoriasDisponibles) {
//                                System.out.println("ID: " + cat.getId_categoria() + " - " + cat.getNombre_categoria());
//                            }
//                        } else {
//                            System.out.println("No hay categorías disponibles");
//                            break;
//                        }
//
//                        System.out.print("\nIngrese IDs de categorías separados por comas (ej: 1,2,3): ");
//                        String inputCategorias = sc.nextLine().trim();
//
//                        try {
//                            categorias.clear();
//                            String[] categoriasArray = inputCategorias.split(",");
//                            for(String cat : categoriasArray) {
//                                categorias.add(Integer.parseInt(cat.trim()));
//                            }
//                            error = ValidarProducto.validarCategoria(categorias);
//                            if(error != null) System.out.println(error);
//                        } catch(NumberFormatException e) {
//                            error = "Formato inválido. Use números separados por comas";
//                            System.out.println(error);
//                        }
//                    } while(error != null);
//
//
//                    System.out.print("Imagen: ");
//                    imagen = sc.nextLine().trim();
//
//                    Producto creado= productoService.registrarProducto(nombre,descripcion,precio,stock,imagen,categorias);
//
//
//                    if(creado!=null){
//                        System.out.println("Producto registrado correctamente");
//                    }else{
//                        System.out.println("Producto no registrado");
//                    }
//                }
//
//                case 7 -> {
//                    List<Producto> productos = productoService.obtenerProductos();
//                    if (productos.isEmpty()) {
//                        System.out.println("No hay productos registrados");
//                    } else {
//                        System.out.println("\n=== Lista de productos ===");
//                        for (Producto p : productos) {
//                            String categorias = productoService.obtenerCategoriasProducto(p.getId_producto());
//
//                            System.out.println(p.getNombre_producto() + " | " +
//                                    p.getDescripcion_producto() + " - $" +
//                                    p.getPrecio_producto() + " - " +
//                                    categorias + " - Stock: " +
//                                    p.getStock_producto());
//                        }
//                    }
//                }
//                case 8-> {
//                    System.out.print("Ingrese el nombre del producto a buscar: ");
//                    String nombreBuscar = sc.nextLine().trim();
//
//                    if (nombreBuscar.isEmpty()) {
//                        System.out.println("El nombre no puede estar vacío");
//                        break;
//                    }
//
//                    List<Producto> productosEncontrados = productoService.buscarPorNombre(nombreBuscar);
//
//                    if (productosEncontrados != null && !productosEncontrados.isEmpty()) {
//                        System.out.println("\n=== PRODUCTOS ENCONTRADOS ===");
//                        System.out.println("Total: " + productosEncontrados.size() + " producto(s)\n");
//
//                        for (Producto p : productosEncontrados) {
//                            System.out.println("─────────────────────────────────");
//                            System.out.println("ID: " + p.getId_producto());
//                            System.out.println("Nombre: " + p.getNombre_producto());
//                            System.out.println("Descripción: " + p.getDescripcion_producto());
//                            System.out.println("Precio: $" + String.format("%.2f", p.getPrecio_producto()));
//                            System.out.println("Stock: " + p.getStock_producto());
//                            System.out.println("Imagen: " + (p.getImagen_producto() != null ? p.getImagen_producto() : "Sin imagen"));
//
//                            // Obtener y mostrar categorías
//                            List<Categoria> categorias = categoriaService.obtenerCategoriasPorProducto(p.getId_producto());
//                            if (categorias != null && !categorias.isEmpty()) {
//                                System.out.print("Categorías: ");
//                                for (int i = 0; i < categorias.size(); i++) {
//                                    System.out.print(categorias.get(i).getNombre_categoria());
//                                    if (i < categorias.size() - 1) System.out.print(", ");
//                                }
//                                System.out.println();
//                            } else {
//                                System.out.println("Categorías: Sin categorías asignadas");
//                            }
//                        }
//                        System.out.println("─────────────────────────────────");
//                    } else {
//                        System.out.println("\n✗ No se encontraron productos con el nombre: '" + nombreBuscar + "'");
//                    }
//
//                }
//
//                case 9 -> {
//                    // Mostrar categorías disponibles
//                    System.out.println("\n=== CATEGORÍAS DISPONIBLES ===");
//                    List<Categoria> listaCategoriasDisponibles = categoriaService.obtenerCategorias();
//
//                    if (listaCategoriasDisponibles == null || listaCategoriasDisponibles.isEmpty()) {
//                        System.out.println("No hay categorías disponibles");
//                        break;
//                    }
//
//                    for (Categoria cat : listaCategoriasDisponibles) {
//                        System.out.println("ID: " + cat.getId_categoria() + " - " + cat.getNombre_categoria());
//                    }
//
//                    // Solicitar IDs de categorías
//                    System.out.print("\nIngrese los IDs de las categorías separados por comas (ej: 1,2,3): ");
//                    String inputCategorias = sc.nextLine().trim();
//
//                    if (inputCategorias.isEmpty()) {
//                        System.out.println("Error: Debe ingresar al menos una categoría");
//                        break;
//                    }
//
//                    List<Integer> idsCategoria = new ArrayList<>();
//                    List<String> nombresCategoriasSeleccionadas = new ArrayList<>();
//
//                    try {
//                        String[] categoriasArray = inputCategorias.split(",");
//
//                        for (String cat : categoriasArray) {
//                            int idCategoria = Integer.parseInt(cat.trim());
//
//                            // Validar que la categoría existe
//                            Categoria categoriaSeleccionada = categoriaService.obtenerCategoriaPorId(idCategoria);
//                            if (categoriaSeleccionada == null) {
//                                System.out.println("Advertencia: La categoría con ID " + idCategoria + " no existe (ignorada)");
//                            } else {
//                                idsCategoria.add(idCategoria);
//                                nombresCategoriasSeleccionadas.add(categoriaSeleccionada.getNombre_categoria());
//                            }
//                        }
//
//                        if (idsCategoria.isEmpty()) {
//                            System.out.println("\n✗ No se encontraron categorías válidas");
//                            break;
//                        }
//
//                    } catch (NumberFormatException e) {
//                        System.out.println("Error: Formato inválido. Use números separados por comas");
//                        break;
//                    }
//
//                    // Buscar productos
//                    List<Producto> productosEncontrados = productoService.listarPorCategoria(idsCategoria);
//
//                    // Mostrar resultados
//                    if (productosEncontrados != null && !productosEncontrados.isEmpty()) {
//                        System.out.println("\n=== PRODUCTOS ENCONTRADOS ===");
//                        System.out.println("Categorías buscadas: " + String.join(", ", nombresCategoriasSeleccionadas));
//                        System.out.println("Total: " + productosEncontrados.size() + " producto(s)\n");
//
//                        for (Producto p : productosEncontrados) {
//                            System.out.println("─────────────────────────────────");
//                            System.out.println("ID: " + p.getId_producto());
//                            System.out.println("Nombre: " + p.getNombre_producto());
//                            System.out.println("Descripción: " + p.getDescripcion_producto());
//                            System.out.println("Precio: $" + String.format("%.2f", p.getPrecio_producto()));
//                            System.out.println("Stock: " + p.getStock_producto());
//                            System.out.println("Imagen: " + (p.getImagen_producto() != null ? p.getImagen_producto() : "Sin imagen"));
//
//                            // Mostrar todas las categorías del producto
//                            List<Categoria> categorias = categoriaService.obtenerCategoriasPorProducto(p.getId_producto());
//                            if (categorias != null && !categorias.isEmpty()) {
//                                System.out.print("Categorías: ");
//                                for (int i = 0; i < categorias.size(); i++) {
//                                    System.out.print(categorias.get(i).getNombre_categoria());
//                                    if (i < categorias.size() - 1) System.out.print(", ");
//                                }
//                                System.out.println();
//                            }
//                        }
//                        System.out.println("─────────────────────────────────");
//                    } else {
//                        System.out.println("\n✗ No se encontraron productos con las categorías seleccionadas");
//                    }
//                }
//
//                case 10 -> {
//                    System.out.println("\n=== PRODUCTOS DISPONIBLES ===");
//                    List<Producto> listaProductos = productoService.obtenerProductos();
//
//                    if (listaProductos == null || listaProductos.isEmpty()) {
//                        System.out.println("No hay productos disponibles para editar");
//                        break;
//                    }
//
//                    for (Producto prod : listaProductos) {
//                        System.out.println("ID: " + prod.getId_producto() + " - " + prod.getNombre_producto() +
//                                " | Precio: $" + String.format("%.2f", prod.getPrecio_producto()) +
//                                " | Stock: " + prod.getStock_producto());
//                    }
//                    System.out.print("Ingrese el ID del producto a editar: ");
//
//                    if (!sc.hasNextInt()) {
//                        System.out.println("Error: Debe ingresar un número válido");
//                        sc.next();
//                        break;
//                    }
//
//                    int idProducto = sc.nextInt();
//                    sc.nextLine(); // Limpiar buffer
//
//                    // Buscar el producto
//                    Producto productoExistente = productoService.obtenerProductoPorId(idProducto);
//
//                    if (productoExistente == null) {
//                        System.out.println("\n✗ Producto con ID " + idProducto + " no encontrado");
//                        break;
//                    }
//
//                    // Mostrar datos actuales
//                    System.out.println("\n=== DATOS ACTUALES DEL PRODUCTO ===");
//                    System.out.println("Nombre: " + productoExistente.getNombre_producto());
//                    System.out.println("Descripción: " + productoExistente.getDescripcion_producto());
//                    System.out.println("Precio: $" + String.format("%.2f", productoExistente.getPrecio_producto()));
//                    System.out.println("Stock: " + productoExistente.getStock_producto());
//                    System.out.println("Imagen: " + (productoExistente.getImagen_producto() != null ? productoExistente.getImagen_producto() : "Sin imagen"));
//
//                    // Mostrar categorías actuales
//                    List<Categoria> categoriasActuales = categoriaService.obtenerCategoriasPorProducto(idProducto);
//                    if (categoriasActuales != null && !categoriasActuales.isEmpty()) {
//                        System.out.print("Categorías: ");
//                        for (int i = 0; i < categoriasActuales.size(); i++) {
//                            System.out.print(categoriasActuales.get(i).getNombre_categoria());
//                            if (i < categoriasActuales.size() - 1) System.out.print(", ");
//                        }
//                        System.out.println();
//                    }
//
//                    System.out.println("\n=== INGRESE LOS NUEVOS DATOS ===");
//                    System.out.println("(Presione Enter para mantener el valor actual)");
//
//                    String nombre, descripcion, imagen, error;
//                    Double precio = null;
//                    Integer stock = null;
//                    List<Integer> categorias = null;
//
//                    // Editar nombre
//                    do {
//                        System.out.print("Nombre [" + productoExistente.getNombre_producto() + "]: ");
//                        nombre = sc.nextLine().trim();
//
//                        if (nombre.isEmpty()) {
//                            nombre = productoExistente.getNombre_producto();
//                            error = null;
//                        } else {
//                            error = ValidarProducto.validarNombre(nombre);
//                            if (error != null) System.out.println(error);
//                        }
//                    } while (error != null);
//
//                    // Editar descripción
//                    do {
//                        System.out.print("Descripción [" + productoExistente.getDescripcion_producto() + "]: ");
//                        descripcion = sc.nextLine().trim();
//
//                        if (descripcion.isEmpty()) {
//                            descripcion = productoExistente.getDescripcion_producto();
//                            error = null;
//                        } else {
//                            error = ValidarProducto.validarDescripcion(descripcion);
//                            if (error != null) System.out.println(error);
//                        }
//                    } while (error != null);
//
//                    // Editar precio
//                    do {
//                        System.out.print("Precio [" + productoExistente.getPrecio_producto() + "]: ");
//                        String precioInput = sc.nextLine().trim();
//
//                        if (precioInput.isEmpty()) {
//                            precio = productoExistente.getPrecio_producto();
//                            error = null;
//                        } else {
//                            try {
//                                precio = Double.parseDouble(precioInput);
//                                error = ValidarProducto.validarPrecio(precio);
//                                if (error != null) System.out.println(error);
//                            } catch (NumberFormatException e) {
//                                error = "Formato inválido. Ingrese un número válido";
//                                System.out.println(error);
//                            }
//                        }
//                    } while (error != null);
//
//                    // Editar stock
//                    do {
//                        System.out.print("Stock [" + productoExistente.getStock_producto() + "]: ");
//                        String stockInput = sc.nextLine().trim();
//
//                        if (stockInput.isEmpty()) {
//                            stock = productoExistente.getStock_producto();
//                            error = null;
//                        } else {
//                            try {
//                                stock = Integer.parseInt(stockInput);
//                                error = ValidarProducto.validarStock(stock);
//                                if (error != null) System.out.println(error);
//                            } catch (NumberFormatException e) {
//                                error = "Formato inválido. Ingrese un número entero válido";
//                                System.out.println(error);
//                            }
//                        }
//                    } while (error != null);
//
//                    // Editar categorías
//                    System.out.println("\n¿Desea editar las categorías? (s/n): ");
//                    String editarCategorias = sc.nextLine().trim().toLowerCase();
//
//                    if (editarCategorias.equals("s")) {
//                        do {
//                            System.out.println("\n=== CATEGORÍAS DISPONIBLES ===");
//                            List<Categoria> listaCategoriasDisponibles = categoriaService.obtenerCategorias();
//
//                            if (listaCategoriasDisponibles != null && !listaCategoriasDisponibles.isEmpty()) {
//                                for (Categoria cat : listaCategoriasDisponibles) {
//                                    System.out.println("ID: " + cat.getId_categoria() + " - " + cat.getNombre_categoria());
//                                }
//                            }
//
//                            System.out.print("\nIngrese IDs de categorías separados por comas (ej: 1,2,3): ");
//                            String inputCategorias = sc.nextLine().trim();
//
//                            try {
//                                categorias = new ArrayList<>();
//                                String[] categoriasArray = inputCategorias.split(",");
//                                for (String cat : categoriasArray) {
//                                    categorias.add(Integer.parseInt(cat.trim()));
//                                }
//                                error = ValidarProducto.validarCategoria(categorias);
//                                if (error != null) System.out.println(error);
//                            } catch (NumberFormatException e) {
//                                error = "Formato inválido. Use números separados por comas";
//                                System.out.println(error);
//                            }
//                        } while (error != null);
//                    }
//
//                    // Editar imagen
//                    System.out.print("Imagen [" + (productoExistente.getImagen_producto() != null ? productoExistente.getImagen_producto() : "Sin imagen") + "]: ");
//                    imagen = sc.nextLine().trim();
//                    if (imagen.isEmpty()) {
//                        imagen = productoExistente.getImagen_producto();
//                    }
//
//                    // Actualizar producto
//                    Producto productoActualizado = new Producto(
//                            idProducto,
//                            nombre,
//                            descripcion,
//                            precio,
//                            stock,
//                            imagen
//                    );
//
//                    boolean editado = productoService.actualizarProducto(productoActualizado);
//
//                    // Actualizar categorías si se modificaron
//                    if (editado && categorias != null) {
//                        ProductoCategoriaService productoCategoriaService = new ProductoCategoriaService();
//                        productoCategoriaService.quitarTodasCategoriasDeProducto(idProducto);
//                        productoCategoriaService.asignarCategoriasAProducto(idProducto, categorias);
//                    }
//
//                    if (editado) {
//                        System.out.println("\n✓ Producto editado correctamente");
//                    } else {
//                        System.out.println("\n✗ Error al editar el producto");
//                    }
//                }
//
//                case 11 -> {
//                    List<Producto> productos = productoService.obtenerProductos();
//                    if (productos.isEmpty()) {
//                        System.out.println("No hay productos registrados");
//                    } else {
//                        System.out.println("\n=== Lista de productos ===");
//                        for (Producto p : productos) {
//                            String categorias = productoService.obtenerCategoriasProducto(p.getId_producto());
//
//                            System.out.println(p.getId_producto() + " | " +
//                                    p.getNombre_producto() + " - " +
//                                    p.getDescripcion_producto() + " - $" +
//                                    p.getPrecio_producto() + " - " +
//                                    categorias + " - Stock: " +
//                                    p.getStock_producto());
//                        }
//                    }
//
//                    System.out.print("ID del producto a eliminar: ");
//                    int id = sc.nextInt();
//                    sc.nextLine();
//
//                    boolean eliminado = productoService.eliminarProducto(id);
//                    if (eliminado) {
//                        System.out.println("Producto eliminado");
//                    } else {
//                        System.out.println("No se encontró el producto con id=" + id);
//                    }
//
//                }
//
//                case 12 -> {
//                    List<Categoria> categorias = categoriaService.obtenerCategorias();
//                    if (categorias.isEmpty()) {
//                        System.out.println("No hay categorias registradas");
//                    } else {
//                        System.out.println("\n=== Lista de categorias ===");
//                        for (Categoria c : categorias) {
//                            System.out.println(c.getId_categoria() + " | " + c.getNombre_categoria());
//                        }
//                    }
//                }
//
//                case 13 -> { // Ver carrito
//                    if (usuarioLogueado == null) {
//                        System.out.println("\n⚠️  Debe iniciar sesión para ver el carrito");
//                        break;
//                    }
//
//                    CarritoService carritoService = new CarritoService();
//                    Carrito carrito = carritoService.obtenerCarritoActivoDeUsuario(usuarioLogueado.getId());
//
//                    if (carrito == null) {
//                        System.out.println("\n⚠️  No tienes un carrito activo");
//                        break;
//                    }
//
//                    List<CarritoProducto> detalles = carritoService.obtenerDetallesCarrito(usuarioLogueado.getId());
//
//                    if (detalles == null || detalles.isEmpty()) {
//                        System.out.println("\n⚠️  Tu carrito está vacío");
//                        break;
//                    }
//
//                    System.out.println("\n=== TU CARRITO ===");
//                    System.out.println("Carrito ID: " + carrito.getId_carrito());
//                    System.out.println("Fecha: " + carrito.getfecha_creacion());
//                    System.out.println("\nProductos:");
//
//                    for (CarritoProducto cp : detalles) {
//                        Producto producto = carritoService.buscarProductoPorId(cp.getid_producto());
//                        if (producto != null) {
//                            System.out.println("─────────────────────────────────");
//                            System.out.println("Producto: " + producto.getNombre_producto());
//                            System.out.println("Precio unitario: $" + String.format("%.2f", producto.getPrecio_producto()));
//                            System.out.println("Cantidad: " + cp.getCantidad());
//                            System.out.println("Subtotal: $" + String.format("%.2f", cp.getSubtotal()));
//                        }
//                    }
//
//                    double total = carritoService.obtenerTotalCarrito(usuarioLogueado.getId());
//                    System.out.println("─────────────────────────────────");
//                    System.out.println("TOTAL: $" + String.format("%.2f", total));
//                    System.out.println("═════════════════════════════════\n");
//                }
//
//                case 14 -> { // Agregar producto al carrito
//                    if (usuarioLogueado == null) {
//                        System.out.println("\n⚠️  Debe iniciar sesión para usar el carrito");
//                        break;
//                    }
//
//                    System.out.println("\n=== PRODUCTOS DISPONIBLES ===");
//                    List<Producto> listaProductos = productoService.obtenerProductos();
//
//                    if (listaProductos == null || listaProductos.isEmpty()) {
//                        System.out.println("No hay productos disponibles");
//                        break;
//                    }
//
//                    for (Producto prod : listaProductos) {
//                        if (prod.getStock_producto() > 0) {
//                            System.out.println("ID: " + prod.getId_producto() + " - " + prod.getNombre_producto() +
//                                    " | Precio: $" + String.format("%.2f", prod.getPrecio_producto()) +
//                                    " | Stock: " + prod.getStock_producto());
//                        }
//                    }
//
//                    System.out.print("\nIngrese el ID del producto: ");
//                    if (!sc.hasNextInt()) {
//                        System.out.println("Error: ID inválido");
//                        sc.next();
//                        break;
//                    }
//                    int idProducto = sc.nextInt();
//
//                    // Validar que el producto existe
//                    Producto producto = productoService.obtenerProductoPorId(idProducto);
//                    if (producto == null) {
//                        System.out.println("\n✗ Producto no encontrado");
//                        sc.nextLine();
//                        break;
//                    }
//
//                    System.out.print("Ingrese la cantidad: ");
//                    if (!sc.hasNextInt()) {
//                        System.out.println("Error: Cantidad inválida");
//                        sc.next();
//                        sc.nextLine();
//                        break;
//                    }
//                    int cantidad = sc.nextInt();
//                    sc.nextLine(); // Limpiar buffer
//
//                    if (cantidad <= 0) {
//                        System.out.println("\n✗ La cantidad debe ser mayor a 0");
//                        break;
//                    }
//
//                    if (producto.getStock_producto() < cantidad) {
//                        System.out.println("\n✗ Stock insuficiente. Disponible: " + producto.getStock_producto());
//                        break;
//                    }
//
//                    CarritoService carritoService = new CarritoService();
//                    boolean agregado = carritoService.agregarProductoAlCarrito(usuarioLogueado.getId(), idProducto, cantidad);
//
//                    if (agregado) {
//                        System.out.println("\n✓ Producto agregado al carrito correctamente");
//                    } else {
//                        System.out.println("\n✗ Error al agregar producto al carrito");
//                    }
//                }
//
//                case 15 -> { // Eliminar producto del carrito
//                    if (usuarioLogueado == null) {
//                        System.out.println("\n⚠️  Debe iniciar sesión para modificar el carrito");
//                        break;
//                    }
//
//                    CarritoService carritoService = new CarritoService();
//
//                    // Verificar que tiene carrito activo
//                    Carrito carrito = carritoService.obtenerCarritoActivoDeUsuario(usuarioLogueado.getId());
//                    if (carrito == null) {
//                        System.out.println("\n⚠️  No tienes un carrito activo");
//                        break;
//                    }
//
//                    // Mostrar productos del carrito
//                    List<CarritoProducto> detalles = carritoService.obtenerDetallesCarrito(usuarioLogueado.getId());
//                    if (detalles == null || detalles.isEmpty()) {
//                        System.out.println("\n⚠️  Tu carrito está vacío");
//                        break;
//                    }
//
//                    System.out.println("\n=== PRODUCTOS EN TU CARRITO ===");
//                    for (CarritoProducto cp : detalles) {
//                        Producto producto = carritoService.buscarProductoPorId(cp.getid_producto());
//                        if (producto != null) {
//                            System.out.println("ID: " + producto.getId_producto() + " - " + producto.getNombre_producto() +
//                                    " | Cantidad: " + cp.getCantidad() +
//                                    " | Subtotal: $" + String.format("%.2f", cp.getSubtotal()));
//                        }
//                    }
//
//                    System.out.print("\nIngrese el ID del producto a eliminar: ");
//                    if (!sc.hasNextInt()) {
//                        System.out.println("Error: ID inválido");
//                        sc.next();
//                        break;
//                    }
//                    int idProducto = sc.nextInt();
//                    sc.nextLine(); // Limpiar buffer
//
//                    boolean eliminado = carritoService.eliminarProductoDelCarrito(usuarioLogueado.getId(), idProducto);
//
//                    if (eliminado) {
//                        System.out.println("\n✓ Producto eliminado del carrito");
//                    } else {
//                        System.out.println("\n✗ Error al eliminar producto");
//                    }
//                }
//
//                case 18 -> { // Ver perfil
//                    if (usuarioLogueado == null) {
//                        System.out.println("Debe iniciar sesión primero");
//                        break;
//                    }
//
//                    System.out.println("\n=== MI PERFIL ===");
//                    System.out.println("ID: " + usuarioLogueado.getId());
//                    System.out.println("Nombre: " + usuarioLogueado.getNombre());
//                    System.out.println("Correo: " + usuarioLogueado.getCorreo());
//                    System.out.println("Direccion" + usuarioLogueado.getDireccion());
//                    System.out.println("Tipo: " + usuarioLogueado.getTipo());
//                    System.out.println("Fecha de registro: " + usuarioLogueado.getFecha_registro());
//                }
//
//                case 16 -> { // Vaciar carrito
//                    if (usuarioLogueado == null) {
//                        System.out.println("\n⚠️  Debe iniciar sesión para vaciar el carrito");
//                        break;
//                    }
//
//                    CarritoService carritoService = new CarritoService();
//                    int cantidadProductos = carritoService.contarProductosEnCarrito(usuarioLogueado.getId());
//
//                    if (cantidadProductos == 0) {
//                        System.out.println("\n⚠️  Tu carrito ya está vacío");
//                        break;
//                    }
//
//                    System.out.print("¿Está seguro de vaciar el carrito? (" + cantidadProductos + " productos) (s/n): ");
//                    String confirmacion = sc.nextLine().trim().toLowerCase();
//
//                    if (confirmacion.equals("s")) {
//                        boolean vaciado = carritoService.vaciarCarrito(usuarioLogueado.getId());
//
//                        if (vaciado) {
//                            System.out.println("\n✓ Carrito vaciado correctamente");
//                        } else {
//                            System.out.println("\n✗ Error al vaciar el carrito");
//                        }
//                    } else {
//                        System.out.println("\nOperación cancelada");
//                    }
//                }
//
//
//
//
//
//
//
//
//                case 0 -> System.out.println("Saliendo...");
//                default -> System.out.println("Opción no válida");
//            }
//
//        } while (opcion != 0);
//
//        sc.close();
//    }
//}
