package Menus;

import clases.Producto;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author valer
 */
public final class UiUtils {

    private UiUtils() {
    }

    public static void instalarSoloEnteros(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                if (!Character.isDigit(evt.getKeyChar()) && !Character.isISOControl(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
    }

    public static Producto seleccionarProducto(Component padre) {
        List<Producto> productos = AppContext.getGestorInventario().obtenerProductos();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "No hay productos registrados.", "Sin productos", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Código", "Producto", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Producto producto : productos) {
            modelo.addRow(new Object[]{producto.getCodigo(), producto.getNombre(), producto.getStockActual()});
        }

        JTable tabla = new JTable(modelo);
        int opcion = JOptionPane.showConfirmDialog(padre, new JScrollPane(tabla), "Seleccionar producto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION || tabla.getSelectedRow() < 0) {
            return null;
        }
        return productos.get(tabla.convertRowIndexToModel(tabla.getSelectedRow()));
    }

    public static String obtenerDetalleMovimiento(JComboBox<String> combo, JTextField campoDetalle, String detallePorDefecto) {
        String tipo = combo.getSelectedItem().toString();
        if (!tipo.equals("Otros")) {
            return tipo;
        }

        String detalle = campoDetalle.getText().trim();
        if (!detalle.isEmpty()) {
            return detalle;
        }
        return detallePorDefecto;
    }

    public static void configurarCampoDetalle(JComboBox<String> combo, JTextField campoDetalle) {
        boolean usarDetalleManual = "Otros".equals(combo.getSelectedItem().toString());
        campoDetalle.setEditable(usarDetalleManual);
        campoDetalle.setBackground(usarDetalleManual ? java.awt.Color.WHITE : new java.awt.Color(237, 237, 255));
    }

    public static void ajustarAnchoColumnas(JTable tabla, JScrollPane scrollPane, int[] anchosMinimos, int anchoVisibleReserva) {
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int columna = 0; columna < tabla.getColumnCount(); columna++) {
            int ancho = anchosMinimos[columna];
            for (int fila = 0; fila < tabla.getRowCount(); fila++) {
                Component componente = tabla.prepareRenderer(tabla.getCellRenderer(fila, columna), fila, columna);
                ancho = Math.max(ancho, componente.getPreferredSize().width + 16);
            }
            TableColumn tableColumn = tabla.getColumnModel().getColumn(columna);
            tableColumn.setPreferredWidth(ancho);
        }
        cubrirAnchoVisible(tabla, scrollPane, anchoVisibleReserva);
    }

    private static void cubrirAnchoVisible(JTable tabla, JScrollPane scrollPane, int anchoVisibleReserva) {
        int anchoColumnas = 0;
        for (int columna = 0; columna < tabla.getColumnCount(); columna++) {
            anchoColumnas += tabla.getColumnModel().getColumn(columna).getPreferredWidth();
        }

        int anchoVisible = scrollPane.getViewport().getExtentSize().width;
        if (anchoVisible <= 0) {
            anchoVisible = scrollPane.getWidth();
        }
        if (anchoVisible <= 0) {
            anchoVisible = anchoVisibleReserva;
        }
        if (anchoColumnas < anchoVisible && tabla.getColumnCount() > 0) {
            TableColumn ultima = tabla.getColumnModel().getColumn(tabla.getColumnCount() - 1);
            ultima.setPreferredWidth(ultima.getPreferredWidth() + (anchoVisible - anchoColumnas));
        }
    }
}
