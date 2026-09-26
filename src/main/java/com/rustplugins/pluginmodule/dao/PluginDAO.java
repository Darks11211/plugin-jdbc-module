package com.rustplugins.pluginmodule.dao;

import com.rustplugins.pluginmodule.modelo.Plugin;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrato para las operaciones de acceso a datos del modulo de plugins.
 * Define las cuatro funcionalidades exigidas por la evidencia:
 * insercion, consulta, actualizacion y eliminacion (CRUD).
 */
public interface PluginDAO {

    int insertarPlugin(Plugin plugin) throws SQLException;

    Plugin consultarPorId(int idPlugin) throws SQLException;

    List<Plugin> consultarTodos() throws SQLException;

    boolean actualizarPlugin(Plugin plugin) throws SQLException;

    boolean eliminarPlugin(int idPlugin) throws SQLException;
}
