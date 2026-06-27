package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author garrido
 */
public class EstadisticasAlmacen implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDateTime fechaGeneracion;
    private final int totalProductos;
    private final int stockTotal;
    private final int movimientosDia;
    private final double valorTotalInventario;
    private final int productosConAlerta;
    private final List<String> alertas;

    public EstadisticasAlmacen(int totalProductos, int stockTotal, double valorTotalInventario,
            int productosConAlerta, List<String> alertas) {
        this(totalProductos, stockTotal, 0, valorTotalInventario, productosConAlerta, alertas);
    }

    public EstadisticasAlmacen(int totalProductos, int stockTotal, int movimientosDia, double valorTotalInventario,
            int productosConAlerta, List<String> alertas) {
        this.fechaGeneracion = LocalDateTime.now();
        this.totalProductos = totalProductos;
        this.stockTotal = stockTotal;
        this.movimientosDia = movimientosDia;
        this.valorTotalInventario = valorTotalInventario;
        this.productosConAlerta = productosConAlerta;
        this.alertas = new ArrayList<>(alertas);
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public int getMovimientosDia() {
        return movimientosDia;
    }

    public double getValorTotalInventario() {
        return valorTotalInventario;
    }

    public int getProductosConAlerta() {
        return productosConAlerta;
    }

    public List<String> getAlertas() {
        return new ArrayList<>(alertas);
    }
}
