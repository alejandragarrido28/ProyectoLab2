package excepciones;

/**
 * Se lanza cuando no existe un producto con el codigo solicitado.
 */
public class ProductoNoEncontradoException extends InventarioException {

    public ProductoNoEncontradoException(String codigo) {
        super("Producto no encontrado: " + codigo);
    }
}
