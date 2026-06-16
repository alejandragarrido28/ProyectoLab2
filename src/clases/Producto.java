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

    public Producto(String codigo, String nombre, String proveedor, CategoriaProducto categoria, double precioUnitario, int stockActual, int stockMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.precioUnitario = precioUnitario;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

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
    public boolean tieneStockCritico() {
        return stockActual < stockMinimo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProveedor() {
        return proveedor;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

}
