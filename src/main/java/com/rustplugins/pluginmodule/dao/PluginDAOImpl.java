package com.rustplugins.pluginmodule.dao;

import com.rustplugins.pluginmodule.conexion.ConexionBD;
import com.rustplugins.pluginmodule.modelo.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC (java.sql.*) del acceso a datos para la tabla plugin_db.plugin.
 * Cada metodo abre su propia conexion (try-with-resources) y la cierra automaticamente,
 * evitando fugas de conexiones abiertas.
 */
public class PluginDAOImpl implements PluginDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO plugin (nombre, descripcion, precio, id_usuario_autor) VALUES (?, ?, ?, ?)";
    private static final String SQL_CONSULTAR_POR_ID =
            "SELECT id_plugin, nombre, descripcion, precio, id_usuario_autor, fecha_publicacion " +
            "FROM plugin WHERE id_plugin = ?";
    private static final String SQL_CONSULTAR_TODOS =
            "SELECT id_plugin, nombre, descripcion, precio, id_usuario_autor, fecha_publicacion " +
            "FROM plugin ORDER BY id_plugin";
    private static final String SQL_ACTUALIZAR =
            "UPDATE plugin SET nombre = ?, descripcion = ?, precio = ? WHERE id_plugin = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM plugin WHERE id_plugin = ?";

    private final ConexionBD conexionBD;

    public PluginDAOImpl() {
        this.conexionBD = new ConexionBD();
    }

    /** Permite inyectar una fabrica de conexion distinta (por ejemplo, en pruebas). */
    public PluginDAOImpl(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public int insertarPlugin(Plugin plugin) throws SQLException {
        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, plugin.getNombre());
            sentencia.setString(2, plugin.getDescripcion());
            sentencia.setDouble(3, plugin.getPrecio());
            sentencia.setInt(4, plugin.getIdUsuarioAutor());

            sentencia.executeUpdate();

            try (ResultSet llavesGeneradas = sentencia.getGeneratedKeys()) {
                if (llavesGeneradas.next()) {
                    int idGenerado = llavesGeneradas.getInt(1);
                    plugin.setIdPlugin(idGenerado);
                    return idGenerado;
                }
            }
        }
        return -1;
    }

    @Override
    public Plugin consultarPorId(int idPlugin) throws SQLException {
        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ID)) {

            sentencia.setInt(1, idPlugin);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearPlugin(resultado);
                }
            }
        }
        return null;
    }

    @Override
    public List<Plugin> consultarTodos() throws SQLException {
        List<Plugin> plugins = new ArrayList<>();

        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_TODOS);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                plugins.add(mapearPlugin(resultado));
            }
        }
        return plugins;
    }

    @Override
    public boolean actualizarPlugin(Plugin plugin) throws SQLException {
        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {

            sentencia.setString(1, plugin.getNombre());
            sentencia.setString(2, plugin.getDescripcion());
            sentencia.setDouble(3, plugin.getPrecio());
            sentencia.setInt(4, plugin.getIdPlugin());

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    @Override
    public boolean eliminarPlugin(int idPlugin) throws SQLException {
        try (Connection conexion = conexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {

            sentencia.setInt(1, idPlugin);

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    private Plugin mapearPlugin(ResultSet resultado) throws SQLException {
        return new Plugin(
                resultado.getInt("id_plugin"),
                resultado.getString("nombre"),
                resultado.getString("descripcion"),
                resultado.getDouble("precio"),
                resultado.getInt("id_usuario_autor"),
                resultado.getTimestamp("fecha_publicacion")
        );
    }
}
