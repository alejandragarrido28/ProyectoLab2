package clases;

import Enums.CategoriaProducto;

/**
 *
 * @author euced
 */
public class ProductoElectronico extends ProductoElectrionico {

    public ProductoElectronico(String numeroDeSerie, int garantiaMeses, int voltaje, String codigo,
            String nombre, String proveedor, CategoriaProducto categoria, double precioUnitario,
            int stockActual, int stockMinimo) {
        super(numeroDeSerie, garantiaMeses, voltaje, codigo, nombre, proveedor, categoria,
                precioUnitario, stockActual, stockMinimo);
    }
}
