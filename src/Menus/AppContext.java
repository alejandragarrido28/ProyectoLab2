/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;
import modelo.GestorInventario;
import persistencia.GestorPersistencia;
/**
 *
 * @author valer
 */
public class AppContext {//para mantener el GestorInventario compartido para todas las clases
    private static GestorInventario gestorInventario;
    public static GestorInventario getGestorInventario()
    {
        if(gestorInventario==null)
        {
            GestorPersistencia gp= new GestorPersistencia();
            gestorInventario= new GestorInventario(gp);
        }
        return gestorInventario;
    }
}
