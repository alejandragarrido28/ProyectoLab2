package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author garrido
 */
public class MovimientoInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime fecha;
    private final String tipo;
    private final String codigoProducto;
    private final int cantidad;
    private final String detalle;

    public MovimientoInventario(String tipo, String codigoProducto, int cantidad, String detalle) {
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.detalle = detalle;
    }

    public MovimientoInventario(LocalDateTime fecha, String tipo, String codigoProducto, int cantidad, String detalle) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.detalle = detalle;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDetalle() {
        return detalle;
    }

    public String toLineaLog() {
        return String.format("%s|%s|%s|%d|%s",
                fecha.format(FORMATO), tipo, codigoProducto, cantidad, detalle);
    }

    public static MovimientoInventario desdeLineaLog(String linea) {
        String[] partes = linea.split("\\|", 5);
        if (partes.length < 5) {
            throw new IllegalArgumentException("Formato de movimiento invalido: " + linea);
        }
        LocalDateTime fecha = LocalDateTime.parse(partes[0], FORMATO);
        return new MovimientoInventario(fecha, partes[1], partes[2], Integer.parseInt(partes[3]), partes[4]);
    }
}
