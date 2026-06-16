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
public class ProductoPerecible extends Producto {

    private String fechaVencimiento;
    private double temperaturaMaxC;
    private int diasVigencia;

    public ProductoPerecible(String fechaVencimiento, double temperaturaMaxC, int diasVigencia, String codigo, String nombre, String proveedor, CategoriaProducto categoria, double precioUnitario, int stockActual, int stockMinimo) {
        super(codigo, nombre, proveedor, categoria, precioUnitario, stockActual, stockMinimo);
        this.fechaVencimiento = fechaVencimiento;
        this.temperaturaMaxC = temperaturaMaxC;
        this.diasVigencia = diasVigencia;
    }

    @Override
    public double calcularValorStock() {
        if (diasVigencia < 7) {
            return super.getPrecioUnitario() * 0.5;
        } else {
            return super.getPrecioUnitario();
        }

    }

    @Override
    public String generarAlerta() {
        if (diasVigencia <= 0) {
            return String.format("CRITICO: Producto VENCIDO - %s [%s]", super.getNombre(), super.getCodigo());
        } else if (diasVigencia < 3) {
            return String.format("URGENTE: Vence en %d dia(s) - %s [%s]", diasVigencia, super.getNombre(), super.getCodigo());
        } else if (diasVigencia < 7) {
            return String.format("ALERTA: Vence en %d dias - %s [%s]", diasVigencia, super.getNombre(), super.getCodigo());
        } else if (tieneStockCritico()) {
            return String.format("AVISO: Stock bajo (%d uds) - %s [%s]", super.getStockActual(), super.getNombre(), super.getCodigo());
        }
        return "";
    }


}
