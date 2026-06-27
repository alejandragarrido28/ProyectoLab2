package persistencia;

import clases.Producto;
import excepciones.ArchivoNoEncontradoException;
import excepciones.ErrorEscrituraException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author garrido
 */
public class BackupIO {

    public static final String ARCHIVO = "inventario_backup.bin";
    private static final String ARCHIVO_ANTERIOR = "backup.bin";

    public void guardar(List<Producto> productos) throws ErrorEscrituraException {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            salida.writeObject(new ArrayList<>(productos));
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Producto> cargar() throws ArchivoNoEncontradoException, ErrorEscrituraException {
        File archivo = obtenerArchivoLectura();
        if (!archivo.exists()) {
            throw new ArchivoNoEncontradoException(ARCHIVO);
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Producto>) entrada.readObject();
        } catch (FileNotFoundException e) {
            throw new ArchivoNoEncontradoException(ARCHIVO, e);
        } catch (IOException | ClassNotFoundException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    private File obtenerArchivoLectura() {
        File archivo = new File(ARCHIVO);
        if (archivo.exists()) {
            return archivo;
        }
        return new File(ARCHIVO_ANTERIOR);
    }
}
