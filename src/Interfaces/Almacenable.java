/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

/**
 *
 * @author euced
 */
public interface Almacenable {
    double calcularValorStock();
    String generarAlerta();
    boolean tieneStockCritico();
    String toRegistro();
}
