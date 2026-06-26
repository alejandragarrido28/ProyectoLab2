/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;
import clases.Producto;
import excepciones.ArchivoNoEncontradoException;
import excepciones.ErrorEscrituraException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
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
            List<Producto> productosIniciales = new ArrayList<>();
            try
            {
                productosIniciales = gp.restaurarDesdeBackup();
            }
            catch (ArchivoNoEncontradoException e)
            {
                // Primera ejecucion: no hay inventario guardado todavia.
            }
            catch (ErrorEscrituraException e)
            {
                JOptionPane.showMessageDialog(null, "No se pudo cargar el inventario guardado: " + e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
            gestorInventario= new GestorInventario(productosIniciales, gp);
        }
        return gestorInventario;
    }
}
