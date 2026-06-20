package excepciones;

/**
 * Se lanza al intentar registrar dos productos con el mismo codigo.
 */
public class ProductoDuplicadoException extends InventarioException {

    public ProductoDuplicadoException(String codigo) {
        super("Ya existe un producto con codigo: " + codigo);
    }
}
