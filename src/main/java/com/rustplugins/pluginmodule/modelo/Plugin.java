package com.rustplugins.pluginmodule.modelo;

import java.sql.Timestamp;

/**
 * Clase modelo que representa un plugin publicado en la tienda
 * (tabla plugin_db.plugin, base de datos propia del microservicio plugin-service).
 *
 * Convencion de nombramiento utilizada:
 *  - Clases: PascalCase (Plugin)
 *  - Atributos y metodos: camelCase (idPlugin, obtenerNombre)
 *  - Paquetes: minusculas sin guiones bajos (com.rustplugins.pluginmodule.modelo)
 */
public class Plugin {

    private int idPlugin;
    private String nombre;
    private String descripcion;
    private double precio;
    private int idUsuarioAutor;
    private Timestamp fechaPublicacion;

    public Plugin() {
    }

    public Plugin(String nombre, String descripcion, double precio, int idUsuarioAutor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.idUsuarioAutor = idUsuarioAutor;
    }

    public Plugin(int idPlugin, String nombre, String descripcion, double precio,
                  int idUsuarioAutor, Timestamp fechaPublicacion) {
        this.idPlugin = idPlugin;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.idUsuarioAutor = idUsuarioAutor;
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getIdPlugin() {
        return idPlugin;
    }

    public void setIdPlugin(int idPlugin) {
        this.idPlugin = idPlugin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdUsuarioAutor() {
        return idUsuarioAutor;
    }

    public void setIdUsuarioAutor(int idUsuarioAutor) {
        this.idUsuarioAutor = idUsuarioAutor;
    }

    public Timestamp getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Timestamp fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "idPlugin=" + idPlugin +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", idUsuarioAutor=" + idUsuarioAutor +
                ", fechaPublicacion=" + fechaPublicacion +
                '}';
    }
}
