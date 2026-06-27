package persistencia;

import excepciones.ArchivoNoEncontradoException;
import excepciones.ErrorEscrituraException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import modelo.EstadisticasAlmacen;

/**
 *
 * @author garrido
 */
public class EstadisticasAlmacenIO {

    public static final String ARCHIVO = "estadisticas_almacen.bin";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void guardar(EstadisticasAlmacen estadisticas) throws ErrorEscrituraException {
        try (DataOutputStream salida = new DataOutputStream(new FileOutputStream(ARCHIVO, true))) {
            salida.writeUTF(estadisticas.getFechaGeneracion().format(FORMATO));
            salida.writeInt(estadisticas.getTotalProductos());
            salida.writeInt(estadisticas.getMovimientosDia());
            salida.writeDouble(estadisticas.getValorTotalInventario());
            salida.writeInt(estadisticas.getProductosConAlerta());
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    public EstadisticasAlmacen cargar() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            throw new ArchivoNoEncontradoException(ARCHIVO);
        }

        EstadisticasAlmacen ultima = null;
        try (DataInputStream entrada = new DataInputStream(new FileInputStream(archivo))) {
            while (true) {
                LocalDateTime fecha = LocalDateTime.parse(entrada.readUTF(), FORMATO);
                int totalProductos = entrada.readInt();
                int movimientosDia = entrada.readInt();
                double valorTotal = entrada.readDouble();
                int alertasActivas = entrada.readInt();
                ultima = new EstadisticasAlmacenConFecha(
                        fecha, totalProductos, movimientosDia, valorTotal, alertasActivas);
            }
        } catch (EOFException e) {
            if (ultima != null) {
                return ultima;
            }
            throw new ErrorEscrituraException(ARCHIVO, e);
        } catch (FileNotFoundException e) {
            throw new ArchivoNoEncontradoException(ARCHIVO, e);
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    private static class EstadisticasAlmacenConFecha extends EstadisticasAlmacen {

        private final LocalDateTime fecha;

        EstadisticasAlmacenConFecha(LocalDateTime fecha, int totalProductos, int movimientosDia,
                double valorTotalInventario, int productosConAlerta) {
            super(totalProductos, 0, movimientosDia, valorTotalInventario, productosConAlerta, new ArrayList<>());
            this.fecha = fecha;
        }

        @Override
        public LocalDateTime getFechaGeneracion() {
            return fecha;
        }
    }
}
