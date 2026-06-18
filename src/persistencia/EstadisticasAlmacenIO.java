package persistencia;

import excepciones.ArchivoNoEncontradoException;
import excepciones.ErrorEscrituraException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import modelo.EstadisticasAlmacen;

/**
 *
 * @author garrido
 */
public class EstadisticasAlmacenIO {

    public static final String ARCHIVO = "estadisticas_almacen.bin";

    public void guardar(EstadisticasAlmacen estadisticas) throws ErrorEscrituraException {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            salida.writeObject(estadisticas);
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    public EstadisticasAlmacen cargar() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            throw new ArchivoNoEncontradoException(ARCHIVO);
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            return (EstadisticasAlmacen) entrada.readObject();
        } catch (FileNotFoundException e) {
            throw new ArchivoNoEncontradoException(ARCHIVO, e);
        } catch (IOException | ClassNotFoundException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }
}
