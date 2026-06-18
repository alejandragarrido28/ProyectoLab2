package persistencia;

import clases.Producto;
import excepciones.ArchivoNoEncontradoException;
import excepciones.ErrorEscrituraException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.MovimientoInventario;

/**
 *
 * @author garrido
 */
public class LogMovimientosIO {

    public static final String ARCHIVO = "log_movimientos.txt";

    public void registrarMovimiento(MovimientoInventario movimiento) throws ErrorEscrituraException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            escritor.write(movimiento.toLineaLog());
            escritor.newLine();
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    public void registrarMovimientos(List<MovimientoInventario> movimientos) throws ErrorEscrituraException {
        for (MovimientoInventario movimiento : movimientos) {
            registrarMovimiento(movimiento);
        }
    }

    public void registrarAlerta(String alerta) throws ErrorEscrituraException {
        MovimientoInventario alertaMovimiento = new MovimientoInventario(
                "ALERTA", "SISTEMA", 0, alerta);
        registrarMovimiento(alertaMovimiento);
    }

    public List<MovimientoInventario> leerMovimientos() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            throw new ArchivoNoEncontradoException(ARCHIVO);
        }

        List<MovimientoInventario> movimientos = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.isBlank()) {
                    movimientos.add(MovimientoInventario.desdeLineaLog(linea.trim()));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ArchivoNoEncontradoException(ARCHIVO, e);
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
        return movimientos;
    }
}
