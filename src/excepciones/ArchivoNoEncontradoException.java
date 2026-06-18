package excepciones;

/**
 *
 * @author garrido
 */
public class ArchivoNoEncontradoException extends Exception {

    public ArchivoNoEncontradoException(String nombreArchivo) {
        super("Archivo no encontrado: " + nombreArchivo);
    }

    public ArchivoNoEncontradoException(String nombreArchivo, Throwable causa) {
        super("Archivo no encontrado: " + nombreArchivo, causa);
    }
}
