package listeners;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import view.FormPanel;
import view.MainView;
import model.CustomTableModel;
import model.TreeElement;
import model.TreeElement.Column;
import model.states.SelectionState;

/**
 * {@code TableSelectionListener} je listener koji reaguje na promjenu selekcije reda u tabeli.
 * Kada korisnik izabere red, prikazuje se {@code FormPanel} sa detaljima tog reda.
 * Ako nema selekcije, listener ne dira form panel niti stanje – 
 * Cancel i Create sami kontrolišu te slučajeve.
 */
public class TableSelectionListener implements ListSelectionListener {

    private MainView appView;

    /**
     * Konstruktor koji inicijalizuje listener sa referencom na glavnu aplikacionu formu.
     *
     * @param appView instanca glavnog prikaza aplikacije
     */
    public TableSelectionListener(MainView appView) {
        this.appView = appView;
    }

    /**
     * Metoda koja se poziva kada se promijeni selekcija u tabeli.
     * Ako je red selektovan prikazuje se {@link FormPanel} sa podacima tog reda.
     * Ako nije selektovan nijedan red, ne radi ništa – 
     * Cancel i Create upravljaju tim slučajem.
     *
     * @param e događaj selekcije
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;

        int selectedRow = appView.getTablePanel().getTable().getSelectedRow();
        int totalRows = appView.getTablePanel().getTable().getRowCount();

        if (selectedRow != -1) {
            JDesktopPane desktopPane = appView.getDesktopPane();
            desktopPane.removeAll();
            desktopPane.setLayout(new BorderLayout());

            FormPanel formPanel = appView.getTablePanel().getFormPanel();

            JTable table = appView.getTablePanel().getTable();
            int columnCount = table.getColumnCount();

            List<Column> metaColumns = new ArrayList<>();
            for (TreeElement element : appView.getTablePanel().getTableMeta().getAllElements()) {
                if (element instanceof Column) {
                    metaColumns.add((Column) element);
                }
            }

            appView.setAppState(new SelectionState(appView));
            appView.getStatusbar().setCurrentRow(selectedRow + 1, totalRows);

            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.VERTICAL_SPLIT,
                    appView.getTablePanel(),
                    formPanel
            );
            splitPane.setDividerLocation(350);
            splitPane.setResizeWeight(0.7);
            splitPane.setOneTouchExpandable(true);

            desktopPane.add(splitPane, BorderLayout.CENTER);

            CustomTableModel model = (CustomTableModel) appView.getTablePanel().getTable().getModel();
            formPanel.displayRow(model, selectedRow);
            formPanel.setVisible(true);

            desktopPane.revalidate();
            desktopPane.repaint();
        }

    }
}
