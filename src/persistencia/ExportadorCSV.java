package persistencia;

import clases.MateriaPrima;
import clases.Producto;
import clases.ProductoElectrionico;
import clases.ProductoPerecible;
import excepciones.ErrorEscrituraException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author garrido
 */
public class ExportadorCSV {

    public static final String ARCHIVO = "inventario_completo.csv";

    private static final String ENCABEZADO = "codigo,nombre,proveedor,categoria,precioUnitario,stockActual,stockMinimo,"
            + "valorStock,alerta,atributosExtra";

    public void exportar(List<Producto> productos) throws ErrorEscrituraException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO))) {
            escritor.write(ENCABEZADO);
            escritor.newLine();
            for (Producto producto : productos) {
                escritor.write(construirLinea(producto));
                escritor.newLine();
            }
        } catch (IOException e) {
            throw new ErrorEscrituraException(ARCHIVO, e);
        }
    }

    private String construirLinea(Producto producto) {
        String alerta = producto.generarAlerta().replace(",", ";");
        return String.format("%s,%s,%s,%s,%.2f,%d,%d,%.2f,%s,%s",
                escapar(producto.getCodigo()),
                escapar(producto.getNombre()),
                escapar(producto.getProveedor()),
                escapar(producto.getCategoria().name()),
                producto.getPrecioUnitario(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                producto.calcularValorStock(),
                escapar(alerta),
                escapar(obtenerAtributosExtra(producto)));
    }

    private String obtenerAtributosExtra(Producto producto) {
        if (producto instanceof ProductoPerecible perecible) {
            return String.format("vencimiento=%s; tempMax=%.1f; diasVigencia=%d",
                    perecible.getFechaVencimiento(),
                    perecible.getTemperaturaMaxC(),
                    perecible.getDiasVigencia());
        }
        if (producto instanceof ProductoElectrionico electronico) {
            return String.format("serie=%s; garantia=%d; voltaje=%d",
                    electronico.getNumeroDeSerie(),
                    electronico.getGarantiaMeses(),
                    electronico.getVoltaje());
        }
        if (producto instanceof MateriaPrima materia) {
            return String.format("unidad=%s; concentracion=%.2f",
                    materia.getUnidadMedida(),
                    materia.getConcentracion());
        }
        return "";
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.contains(",") || valor.contains("\"")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
