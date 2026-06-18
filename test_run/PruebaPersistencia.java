import clases.MateriaPrima;
import clases.Producto;
import clases.ProductoElectrionico;
import clases.ProductoPerecible;
import Enums.CategoriaProducto;
import persistencia.GestorPersistencia;
import modelo.MovimientoInventario;
import java.util.List;
import java.util.ArrayList;

public class PruebaPersistencia {
    public static void main(String[] args) throws Exception {
        List<Producto> productos = new ArrayList<>();
        productos.add(new ProductoPerecible("2026-06-20", 4.0, 3, "P001", "Leche", "Lacteos SA", CategoriaProducto.PERECEDERO, 25.0, 5, 10));
        productos.add(new ProductoElectrionico("SN-123", 12, 220, "E001", "Monitor", "TechCorp", CategoriaProducto.ELECTRONICO, 3500.0, 2, 5));
        productos.add(new MateriaPrima("kg", 0.95, "M001", "Harina", "Molinos", CategoriaProducto.MATERIA_PRIMA, 15.0, 0, 20));
        GestorPersistencia gestor = new GestorPersistencia();
        gestor.registrarMovimiento(new MovimientoInventario("ENTRADA", "P001", 5, "Recepcion inicial"));
        gestor.guardarInventarioCompleto(productos);
        System.out.println("Alertas: " + gestor.recolectarAlertas(productos).size());
        System.out.println("Backup: " + gestor.restaurarDesdeBackup().size());
        System.out.println("Valor: " + gestor.cargarEstadisticas().getValorTotalInventario());
        System.out.println("Log: " + gestor.leerLogMovimientos().size());
    }
}