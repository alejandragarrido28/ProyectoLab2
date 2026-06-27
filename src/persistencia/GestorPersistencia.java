package persistencia;

import clases.Producto;
import excepciones.ArchivoNoEncontradoException;
import excepciones.ErrorEscrituraException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.EstadisticasAlmacen;
import modelo.MovimientoInventario;

/**
 * Coordina la persistencia del inventario: log de movimientos, estadisticas
 * binarias, backup y exportacion CSV, con alertas automaticas al guardar.
 *
 * @author garrido
 */
public class GestorPersistencia {

    private static final DateTimeFormatter FORMATO_ALERTA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LogMovimientosIO logMovimientos;
    private final EstadisticasAlmacenIO estadisticasIO;
    private final BackupIO backupIO;
    private final ExportadorCSV exportadorCSV;

    public GestorPersistencia() {
        this.logMovimientos = new LogMovimientosIO();
        this.estadisticasIO = new EstadisticasAlmacenIO();
        this.backupIO = new BackupIO();
        this.exportadorCSV = new ExportadorCSV();
    }

    public void guardarInventarioCompleto(List<Producto> productos) throws ErrorEscrituraException {
        List<String> alertas = recolectarAlertas(productos);

        EstadisticasAlmacen estadisticas = calcularEstadisticas(productos, alertas);
        estadisticasIO.guardar(estadisticas);
        backupIO.guardar(productos);
        exportadorCSV.exportar(productos);
    }

    public List<Producto> restaurarDesdeBackup() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        return backupIO.cargar();
    }

    public EstadisticasAlmacen cargarEstadisticas() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        return estadisticasIO.cargar();
    }

    public List<MovimientoInventario> leerLogMovimientos() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        return logMovimientos.leerMovimientos();
    }

    public void registrarMovimiento(MovimientoInventario movimiento) throws ErrorEscrituraException {
        logMovimientos.registrarMovimiento(movimiento);
    }

    public void registrarAlerta(String alerta) throws ErrorEscrituraException {
        logMovimientos.registrarAlerta(alerta);
    }

    public void exportarInventarioCSV(List<Producto> productos) throws ErrorEscrituraException {
        exportadorCSV.exportar(productos);
    }

    public List<String> recolectarAlertas(List<Producto> productos) {
        List<String> alertas = new ArrayList<>();
        for (Producto producto : productos) {
            String alerta = producto.generarAlerta();
            if (alerta != null && !alerta.isBlank()) {
                alertas.add(formatearAlertaConFecha(alerta));
            }
        }
        return alertas;
    }

    private EstadisticasAlmacen calcularEstadisticas(List<Producto> productos, List<String> alertas) {
        int stockTotal = 0;
        double valorTotal = 0;
        for (Producto producto : productos) {
            stockTotal += producto.getStockActual();
            valorTotal += producto.calcularValorStock();
        }
        return new EstadisticasAlmacen(
                productos.size(),
                stockTotal,
                contarMovimientosDia(),
                valorTotal,
                alertas.size(),
                alertas);
    }

    private int contarMovimientosDia() {
        List<MovimientoInventario> movimientos;
        try {
            movimientos = logMovimientos.leerMovimientos();
        } catch (Exception e) {
            movimientos = Collections.emptyList();
        }
        int total = 0;
        LocalDateTime ahora = LocalDateTime.now();
        for (MovimientoInventario movimiento : movimientos) {
            String tipo = movimiento.getTipo();
            if (movimiento.getFecha().toLocalDate().equals(ahora.toLocalDate())
                    && ("ENTRADA".equalsIgnoreCase(tipo) || "SALIDA".equalsIgnoreCase(tipo))) {
                total++;
            }
        }
        return total;
    }

    private String formatearAlertaConFecha(String motivo) {
        return String.format("[%s] %s", LocalDateTime.now().format(FORMATO_ALERTA), motivo);
    }
}
