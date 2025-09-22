package view;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;


/**
 * Renderer za prikaz boolean vrijednosti u {@link JTable} komponenti.
 * 
 * Umesto standardnih vrijednosti {@code true} i {@code false}, 
 * ovaj renderer prikazuje tekstualne vrednosti:
 * 
 * "Da" – kada je vrednost {@code true}
 * "Ne" – kada je vrednost {@code false}
 *  Prazno polje – kada vrednost nije boolean tipa
 * 
 * Ova klasa nasljeđuje {@link DefaultTableCellRenderer} 
 * i koristi se za prilagođeno renderovanje ćelija u tabeli.
 * 
 * @author G4
 */
public class BooleanRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Boolean) {
            setText((Boolean) value ? "Da" : "Ne");
        } else {
            setText("");
        }

        return this;
    }
}
