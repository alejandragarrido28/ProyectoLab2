package modelo;

import clases.Producto;
import excepciones.DatosInvalidosInventarioException;
import excepciones.ErrorEscrituraException;
import excepciones.ProductoDuplicadoException;
import excepciones.ProductoNoEncontradoException;
import excepciones.StockInsuficienteException;
import excepciones.TipoMovimientoInvalidoException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter FORMATO_ALERTA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            throw new ProductoDuplicadoException(producto.getCodigo());
        }
        productos.add(producto);
    }

    public void registrarMovimiento(MovimientoInventario movimiento) throws ErrorEscrituraException {
        if (movimiento == null) {
            throw new DatosInvalidosInventarioException("El movimiento no puede ser nulo");
        }

        String tipo = normalizarTipoMovimiento(movimiento.getTipo());
        Producto producto = buscarPorCodigo(movimiento.getCodigoProducto());
        if (producto == null) {
            throw new ProductoNoEncontradoException(movimiento.getCodigoProducto());
        }
        if (movimiento.getCantidad() <= 0) {
            throw new DatosInvalidosInventarioException("La cantidad del movimiento debe ser mayor que cero");
        }

        if ("SALIDA".equals(tipo)) {
            if (!validarSalida(producto, movimiento.getCantidad())) {
                throw new StockInsuficienteException(producto.getCodigo());
            }
            producto.actualizarStock(-movimiento.getCantidad());
        } else if ("ENTRADA".equals(tipo)) {
            producto.actualizarStock(movimiento.getCantidad());
        } else {
            throw new TipoMovimientoInvalidoException(movimiento.getTipo());
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
                alertas.add(formatearAlertaConFecha(alerta));
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
            throw new DatosInvalidosInventarioException("Indice de producto invalido: " + indice);
        }
        if (cambio == 0) {
            throw new DatosInvalidosInventarioException("El cambio de stock no puede ser cero");
        }
        if (cambio < 0 && !validarSalida(indice, Math.abs(cambio))) {
            throw new StockInsuficienteException(productos.get(indice).getCodigo());
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
            throw new DatosInvalidosInventarioException("El producto no puede ser nulo");
        }
        if (producto.getCodigo() == null || producto.getCodigo().isBlank()) {
            throw new DatosInvalidosInventarioException("El producto debe tener codigo");
        }
        if (producto.getStockActual() < 0) {
            throw new DatosInvalidosInventarioException("El stock inicial no puede ser negativo");
        }
        if (producto.getStockMinimo() < 0) {
            throw new DatosInvalidosInventarioException("El stock minimo no puede ser negativo");
        }
        if (producto.getPrecioUnitario() < 0) {
            throw new DatosInvalidosInventarioException("El precio unitario no puede ser negativo");
        }
    }

    private String formatearAlertaConFecha(String motivo) {
        return String.format("[%s] %s", LocalDateTime.now().format(FORMATO_ALERTA), motivo);
    }

    private String normalizarTipoMovimiento(String tipo) {
        if (tipo == null) {
            return "";
        }
        return tipo.trim().toUpperCase();
    }
}
