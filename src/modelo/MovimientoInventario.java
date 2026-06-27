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
    private final String usuarioResponsable;

    public MovimientoInventario(String tipo, String codigoProducto, int cantidad, String detalle) {
        this(tipo, codigoProducto, cantidad, detalle, "Usuario");
    }

    public MovimientoInventario(String tipo, String codigoProducto, int cantidad, String detalle, String usuarioResponsable) {
        this(LocalDateTime.now(), tipo, codigoProducto, cantidad, detalle, usuarioResponsable);
    }

    public MovimientoInventario(LocalDateTime fecha, String tipo, String codigoProducto, int cantidad, String detalle) {
        this(fecha, tipo, codigoProducto, cantidad, detalle, "Usuario");
    }

    public MovimientoInventario(LocalDateTime fecha, String tipo, String codigoProducto, int cantidad, String detalle, String usuarioResponsable) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.detalle = detalle;
        this.usuarioResponsable = usuarioResponsable;
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

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public String toLineaLog() {
        return String.format("%s|%s|%s|%d|%s|%s",
                fecha.format(FORMATO), tipo, codigoProducto, cantidad, detalle, usuarioResponsable);
    }

    public static MovimientoInventario desdeLineaLog(String linea) {
        String[] partes = linea.split("\\|", 6);
        if (partes.length < 5) {
            throw new IllegalArgumentException("Formato de movimiento invalido: " + linea);
        }
        LocalDateTime fecha = LocalDateTime.parse(partes[0], FORMATO);
        String usuario = partes.length >= 6 ? partes[5] : "Usuario";
        return new MovimientoInventario(fecha, partes[1], partes[2], Integer.parseInt(partes[3]), partes[4], usuario);
    }
}
