package excepciones;

/**
 * Se lanza cuando el movimiento no es ENTRADA ni SALIDA.
 */
public class TipoMovimientoInvalidoException extends InventarioException {

    public TipoMovimientoInvalidoException(String tipo) {
        super("Tipo de movimiento no soportado: " + tipo);
    }
}
