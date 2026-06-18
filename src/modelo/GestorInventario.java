package modelo;

import clases.Producto;
import excepciones.ErrorEscrituraException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import persistencia.GestorPersistencia;

/**
 * Gestiona la logica central del inventario: productos, movimientos, stock,
 * busquedas, valor total y alertas.
 *
 * @author Aguilar
 */
public class GestorInventario {

    private final List<Producto> productos;
    private final List<MovimientoInventario> movimientos;
    private final GestorPersistencia persistencia;

    public GestorInventario() {
        this(new ArrayList<>(), null);
    }

    public GestorInventario(List<Producto> productosIniciales) {
        this(productosIniciales, null);
    }

    public GestorInventario(GestorPersistencia persistencia) {
        this(new ArrayList<>(), persistencia);
    }

    public GestorInventario(List<Producto> productosIniciales, GestorPersistencia persistencia) {
        this.productos = new ArrayList<>();
        this.movimientos = new ArrayList<>();
        this.persistencia = persistencia;

        if (productosIniciales != null) {
            for (Producto producto : productosIniciales) {
                registrarProducto(producto);
            }
        }
    }

    public void registrarProducto(Producto producto) {
        validarProducto(producto);
        if (buscarPorCodigo(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con codigo: " + producto.getCodigo());
        }
        productos.add(producto);
    }

    public void registrarMovimiento(MovimientoInventario movimiento) throws ErrorEscrituraException {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento no puede ser nulo");
        }

        String tipo = normalizarTipoMovimiento(movimiento.getTipo());
        Producto producto = buscarPorCodigo(movimiento.getCodigoProducto());
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + movimiento.getCodigoProducto());
        }
        if (movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor que cero");
        }

        if ("SALIDA".equals(tipo)) {
            if (!validarSalida(producto, movimiento.getCantidad())) {
                throw new IllegalArgumentException("Salida invalida: stock insuficiente");
            }
            producto.actualizarStock(-movimiento.getCantidad());
        } else if ("ENTRADA".equals(tipo)) {
            producto.actualizarStock(movimiento.getCantidad());
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no soportado: " + movimiento.getTipo());
        }

        movimientos.add(movimiento);
        if (persistencia != null) {
            persistencia.registrarMovimiento(movimiento);
        }
    }

    public MovimientoInventario registrarEntrada(String codigoProducto, int cantidad, String detalle)
            throws ErrorEscrituraException {
        MovimientoInventario movimiento = new MovimientoInventario("ENTRADA", codigoProducto, cantidad, detalle);
        registrarMovimiento(movimiento);
        return movimiento;
    }

    public MovimientoInventario registrarSalida(String codigoProducto, int cantidad, String detalle)
            throws ErrorEscrituraException {
        MovimientoInventario movimiento = new MovimientoInventario("SALIDA", codigoProducto, cantidad, detalle);
        registrarMovimiento(movimiento);
        return movimiento;
    }

    public double calcularValorTotalAlmacen() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.calcularValorStock();
        }
        return total;
    }

    public List<String> generarReporteAlertas() {
        List<String> alertas = new ArrayList<>();
        for (Producto producto : productos) {
            String alerta = producto.generarAlerta();
            if (alerta != null && !alerta.isBlank()) {
                alertas.add(alerta);
            }
        }
        return alertas;
    }

    public Producto buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return producto;
            }
        }
        return null;
    }

    public boolean validarSalida(int indice, int cantidad) {
        if (indice < 0 || indice >= productos.size()) {
            return false;
        }
        Producto producto = productos.get(indice);
        return cantidad > 0 && producto.getStockActual() >= cantidad;
    }

    public boolean validarSalida(int cantidad) {
        return cantidad > 0;
    }

    public boolean validarSalida(Producto producto, int cantidad) {
        return producto != null && cantidad > 0 && producto.getStockActual() >= cantidad;
    }

    public boolean validarSalida(String codigoProducto, int cantidad) {
        return validarSalida(buscarPorCodigo(codigoProducto), cantidad);
    }

    public void actualizarStock(int indice, int cambio) {
        if (indice < 0 || indice >= productos.size()) {
            throw new IndexOutOfBoundsException("Indice de producto invalido: " + indice);
        }
        if (cambio == 0) {
            throw new IllegalArgumentException("El cambio de stock no puede ser cero");
        }
        if (cambio < 0 && !validarSalida(indice, Math.abs(cambio))) {
            throw new IllegalArgumentException("Salida invalida: stock insuficiente");
        }
        productos.get(indice).actualizarStock(cambio);
    }

    public List<Producto> obtenerProductos() {
        return Collections.unmodifiableList(productos);
    }

    public List<MovimientoInventario> obtenerMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    public int obtenerIndiceProducto(String codigoProducto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equalsIgnoreCase(codigoProducto)) {
                return i;
            }
        }
        return -1;
    }

    public boolean guardarInventarioCompleto() throws ErrorEscrituraException {
        if (persistencia == null) {
            return false;
        }
        persistencia.guardarInventarioCompleto(productos);
        return true;
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (producto.getCodigo() == null || producto.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El producto debe tener codigo");
        }
        if (producto.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock inicial no puede ser negativo");
        }
        if (producto.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock minimo no puede ser negativo");
        }
        if (producto.getPrecioUnitario() < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
    }

    private String normalizarTipoMovimiento(String tipo) {
        if (tipo == null) {
            return "";
        }
        return tipo.trim().toUpperCase();
    }
}
