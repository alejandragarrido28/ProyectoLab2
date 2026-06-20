package excepciones;

/**
 * Se lanza cuando una salida dejaria el inventario con stock negativo.
 */
public class StockInsuficienteException extends InventarioException {

    public StockInsuficienteException(String codigo) {
        super("Salida invalida: stock insuficiente para el producto " + codigo);
    }
}
