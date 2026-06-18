package excepciones;

/**
 *
 * @author garrido
 */
public class ErrorEscrituraException extends Exception {

    public ErrorEscrituraException(String nombreArchivo) {
        super("Error de escritura en archivo: " + nombreArchivo);
    }

    public ErrorEscrituraException(String nombreArchivo, Throwable causa) {
        super("Error de escritura en archivo: " + nombreArchivo, causa);
    }
}
