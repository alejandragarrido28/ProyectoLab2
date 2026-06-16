/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import Enums.CategoriaProducto;
import Interfaces.Almacenable;

/**
 *
 * @author euced
 */
public abstract class Producto implements Almacenable {

    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private String proveedor;
    private CategoriaProducto categoria;
    private double precioUnitario;
    private int stockActual;
    private int stockMinimo;

    @Override
    public String toRegistro() {
        return String.format("%s|%s|%s|%s|%.2f|%d|%d",
                codigo,
                nombre,
                proveedor,
                categoria.getDescripcion(),
                precioUnitario,
                stockActual,
                stockMinimo);
    }

    @Override
    public boolean tieneStockCritico(){
        return stockActual < stockMinimo;
    };
    
}
