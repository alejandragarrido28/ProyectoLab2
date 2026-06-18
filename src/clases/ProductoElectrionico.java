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
public class ProductoElectrionico extends Producto {

    private String numeroDeSerie;
    private int garantiaMeses;
    private int voltaje;

    public ProductoElectrionico(String numeroDeSerie, int garantiaMeses, int voltaje, String codigo, String nombre, String proveedor, CategoriaProducto categoria, double precioUnitario, int stockActual, int stockMinimo) {
        super(codigo, nombre, proveedor, categoria, precioUnitario, stockActual, stockMinimo);
        this.numeroDeSerie = numeroDeSerie;
        this.garantiaMeses = garantiaMeses;
        this.voltaje = voltaje;
    }

    @Override
    public double calcularValorStock() {
        return super.getPrecioUnitario() * super.getStockActual() * (1 + garantiaMeses * 0.01);
    }

    @Override
    public String generarAlerta() {
        if (super.getStockActual() == 0) {
            return String.format("CRITICO: Sin existencias - %s [%s] Serie: %s", super.getNombre(), super.getCodigo(), numeroDeSerie);
        } else if (tieneStockCritico()) {
            return String.format("ALERTA: Stock bajo (%d/%d min) - %s [%s]", super.getStockActual(), super.getStockMinimo(), super.getNombre(), super.getCodigo());
        }
        return "";
    }

    @Override
    public String toRegistro() {
        return super.toRegistro() + String.format("|%s|%d|%d",
                numeroDeSerie, garantiaMeses, voltaje);
    }

    public String getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public int getVoltaje() {
        return voltaje;
    }

}
