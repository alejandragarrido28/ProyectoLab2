/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import Enums.CategoriaProducto;

/**
 *
 * @author euced
 */
public class MateriaPrima extends Producto {

    private String unidadMedida;
    private double concentracion;

    public MateriaPrima(String unidadMedida, double concentracion, String codigo, String nombre, String proveedor, CategoriaProducto categoria, double precioUnitario, int stockActual, int stockMinimo) {
        super(codigo, nombre, proveedor, categoria, precioUnitario, stockActual, stockMinimo);
        this.unidadMedida = unidadMedida;
        this.concentracion = concentracion;
    }

    
    @Override
    public double calcularValorStock() {
        return super.getPrecioUnitario() * super.getStockActual();
    }

    @Override
    public String generarAlerta() {
        if (super.getStockActual() == 0) {
            return String.format("CRITICO: Sin existencias - %s [%s] Contactar: %s", super.getNombre(), super.getCodigo(), super.getProveedor());
        } else if (tieneStockCritico()) {
            return String.format("ALERTA: Stock bajo (%d %s, min %d) - %s. Proveedor: %s",
                    super.getStockActual(), unidadMedida, super.getStockMinimo(), super.getNombre(), super.getProveedor());
        }
        return "";

    }

    @Override
    public String toRegistro() {
        return super.toRegistro() + String.format("|%s|%.2f",
                unidadMedida,
                concentracion);
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public double getConcentracion() {
        return concentracion;
    }
}
