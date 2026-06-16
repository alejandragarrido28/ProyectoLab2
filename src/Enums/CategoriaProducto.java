/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Enums;

/**
 *
 * @author euced
 */
public enum CategoriaProducto {
    PERECEDERO("El producto se puede echar a perder", true), 
    ELECTRONICO("El producto contiene componentes electronicos",false), 
    MATERIA_PRIMA("Materia lista para ser transformada",true);
    private String descripcion;
    private boolean requiereRefrigeracion;

    private CategoriaProducto(String descripcion, boolean requiereRefrigeracion) {
        this.descripcion = descripcion;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }
    
}
