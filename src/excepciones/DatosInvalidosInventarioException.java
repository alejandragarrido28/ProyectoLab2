package excepciones;

/**
 * Se lanza cuando un dato de inventario no cumple las reglas minimas.
 */
public class DatosInvalidosInventarioException extends InventarioException {

    public DatosInvalidosInventarioException(String mensaje) {
        super(mensaje);
    }
}
