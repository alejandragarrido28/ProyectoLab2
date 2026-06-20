package excepciones;

/**
 * Excepcion base para reglas de negocio del inventario.
 */
public class InventarioException extends RuntimeException {

    public InventarioException(String mensaje) {
        super(mensaje);
    }

    public InventarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
