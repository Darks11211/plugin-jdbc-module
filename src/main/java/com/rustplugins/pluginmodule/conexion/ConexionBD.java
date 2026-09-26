package com.rustplugins.pluginmodule.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase encargada de abrir la conexion JDBC contra la base de datos
 * plugin_db (base de datos propia del microservicio plugin-service,
 * segun el modelo logico definido en la evidencia GA4-220501095-AA1-EV01
 * y el script fisico de la evidencia GA6-220501096-AA2-EV03).
 *
 * Los datos de conexion NO se dejan escritos directamente en el codigo
 * (buena practica de seguridad / confidencialidad de los datos): se leen
 * desde el archivo src/main/resources/config.properties.
 */
public class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "config.properties";

    /**
     * Abre y retorna una nueva conexion JDBC a la base de datos plugin_db.
     *
     * @return conexion JDBC lista para usar
     * @throws SQLException si ocurre un error al conectarse a la base de datos
     */
    public Connection obtenerConexion() throws SQLException {
        Properties propiedades = cargarPropiedades();

        String urlConexion = propiedades.getProperty("db.url");
        String usuario = propiedades.getProperty("db.usuario");
        String clave = propiedades.getProperty("db.clave");

        return DriverManager.getConnection(urlConexion, usuario, clave);
    }

    private Properties cargarPropiedades() throws SQLException {
        Properties propiedades = new Properties();
        try (InputStream entrada = ConexionBD.class.getClassLoader()
                .getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new SQLException("No se encontro el archivo " + ARCHIVO_CONFIGURACION
                        + " en el classpath.");
            }
            propiedades.load(entrada);
        } catch (IOException excepcion) {
            throw new SQLException("Error leyendo la configuracion de conexion.", excepcion);
        }
        return propiedades;
    }
}
