package com.rustplugins.pluginmodule.app;

import com.rustplugins.pluginmodule.dao.PluginDAO;
import com.rustplugins.pluginmodule.dao.PluginDAOImpl;
import com.rustplugins.pluginmodule.modelo.Plugin;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicacion de consola que expone las cuatro funcionalidades exigidas
 * por la evidencia GA7-220501096-AA2-EV01 (insercion, consulta,
 * actualizacion y eliminacion) para el modulo de plugins del proyecto,
 * usando conexion JDBC pura contra la base de datos plugin_db.
 */
public class PluginModuleApp {

    private final PluginDAO pluginDAO = new PluginDAOImpl();
    private final Scanner entrada = new Scanner(System.in);

    public static void main(String[] argumentos) {
        new PluginModuleApp().iniciar();
    }

    private void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> insertarPlugin();
                    case 2 -> consultarTodos();
                    case 3 -> consultarPorId();
                    case 4 -> actualizarPlugin();
                    case 5 -> eliminarPlugin();
                    case 0 -> System.out.println("Saliendo del modulo de plugins...");
                    default -> System.out.println("Opcion invalida. Intente de nuevo.");
                }
            } catch (SQLException excepcion) {
                System.out.println("Ocurrio un error accediendo a la base de datos: "
                        + excepcion.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== MODULO DE PLUGINS (JDBC) =====");
        System.out.println("1. Insertar plugin");
        System.out.println("2. Consultar todos los plugins");
        System.out.println("3. Consultar plugin por id");
        System.out.println("4. Actualizar plugin");
        System.out.println("5. Eliminar plugin");
        System.out.println("0. Salir");
    }

    private void insertarPlugin() throws SQLException {
        System.out.println("--- Insertar nuevo plugin ---");
        String nombre = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripcion: ");
        double precio = leerDecimal("Precio: ");
        int idUsuarioAutor = leerEntero("Id del usuario autor: ");

        Plugin nuevoPlugin = new Plugin(nombre, descripcion, precio, idUsuarioAutor);
        int idGenerado = pluginDAO.insertarPlugin(nuevoPlugin);

        System.out.println("Plugin insertado correctamente con id = " + idGenerado);
    }

    private void consultarTodos() throws SQLException {
        System.out.println("--- Listado de plugins ---");
        List<Plugin> plugins = pluginDAO.consultarTodos();

        if (plugins.isEmpty()) {
            System.out.println("No hay plugins registrados todavia.");
            return;
        }
        for (Plugin plugin : plugins) {
            System.out.println(plugin);
        }
    }

    private void consultarPorId() throws SQLException {
        int idPlugin = leerEntero("Id del plugin a consultar: ");
        Plugin plugin = pluginDAO.consultarPorId(idPlugin);

        if (plugin == null) {
            System.out.println("No existe un plugin con id " + idPlugin);
        } else {
            System.out.println(plugin);
        }
    }

    private void actualizarPlugin() throws SQLException {
        int idPlugin = leerEntero("Id del plugin a actualizar: ");
        Plugin plugin = pluginDAO.consultarPorId(idPlugin);

        if (plugin == null) {
            System.out.println("No existe un plugin con id " + idPlugin);
            return;
        }

        String nuevoNombre = leerTexto("Nuevo nombre (" + plugin.getNombre() + "): ");
        String nuevaDescripcion = leerTexto("Nueva descripcion: ");
        double nuevoPrecio = leerDecimal("Nuevo precio: ");

        plugin.setNombre(nuevoNombre);
        plugin.setDescripcion(nuevaDescripcion);
        plugin.setPrecio(nuevoPrecio);

        boolean actualizado = pluginDAO.actualizarPlugin(plugin);
        System.out.println(actualizado ? "Plugin actualizado correctamente."
                : "No se pudo actualizar el plugin.");
    }

    private void eliminarPlugin() throws SQLException {
        int idPlugin = leerEntero("Id del plugin a eliminar: ");
        boolean eliminado = pluginDAO.eliminarPlugin(idPlugin);

        System.out.println(eliminado ? "Plugin eliminado correctamente."
                : "No existe un plugin con ese id.");
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine();
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!entrada.hasNextInt()) {
            System.out.print("Ingrese un numero entero valido: ");
            entrada.next();
        }
        int valor = entrada.nextInt();
        entrada.nextLine();
        return valor;
    }

    private double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        while (!entrada.hasNextDouble()) {
            System.out.print("Ingrese un numero valido: ");
            entrada.next();
        }
        double valor = entrada.nextDouble();
        entrada.nextLine();
        return valor;
    }
}
