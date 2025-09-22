package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import db.DbManipulation;
import model.TreeElement.Column;

/**
 * Komponenta {LinkedField} predstavlja input polje koje je povezano 
 * sa drugom (referentnom) tabelom u bazi podataka.
 * Sastoji se od osnovnog polja (read-only) i dugmeta koje otvara dijalog
 * za izbor reda iz povezane tabele. Pri izboru reda automatski se postavlja
 * odgovarajući PK iz reference tabele (uključujući složene ključeve).
 */
public class LinkedField extends JPanel implements IInputField {
    private static final long serialVersionUID = 1L;

    private final IInputField baseField; 
    private final String refColumn;      
    private final String refTable;       
    private final Column owningColumnMeta;

    private TableViewFactory tableViewFactory = new TableViewFactory();
    private view.Table tableView;
    private JButton button;

    private Object selectedId = null;     
    private Object selectedDisplay = null;
    private String pkColumnName = null;   
    
    /**
     * Konstruktor za kreiranje {LinkedField} instance.
     *
     * @param baseField        osnovno input polje (readonly)
     * @param refTable         naziv referentne tabele
     * @param refColumn        naziv kolone koja se prikazuje korisniku
     * @param owningColumnMeta metapodaci o koloni koja koristi ovo polje
     */

    public LinkedField(IInputField baseField, String refTable, String refColumn, Column owningColumnMeta) {
        this.baseField = baseField;
        this.refColumn = refColumn;
        this.refTable = refTable;
        this.owningColumnMeta = owningColumnMeta;

        setName(baseField.getName());

        button = new JButton("...");
        button.setPreferredSize(new Dimension(20, 20));
        button.setBackground(new Color(51, 153, 255));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> selectMatchingRow());

        baseField.setEnabled(false); 
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = java.awt.GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        add((Component) baseField, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = java.awt.GridBagConstraints.NONE; gbc.weightx = 0;
        add(button, gbc);

        prepareReferenceTable();
    }

    /**
     * Inicijalno učitava podatke iz referentne tabele
     * i kreira {@link view.Table} za izbor reda.
     */
    private void prepareReferenceTable() {
        try {
            Connection conn = DbManipulation.createConnection().getConnection();
            resolvePkColumnName(conn);

            String sql = "SELECT * FROM [" + refTable + "]";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            List<String> colNames = new ArrayList<>();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) colNames.add(meta.getColumnLabel(i));

            List<List<Object>> rows = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= colCount; i++) row.add(rs.getObject(i));
                rows.add(row);
            }

            SimpleTableModel tblModel = new SimpleTableModel(colNames, rows);
            this.tableView = tableViewFactory.create(tblModel);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Pokušava da pronađe naziv primarnog ključa u datoj tabeli.
     * 
     * Ako je PK ujedno i FK, preskače ga i traži sljedeći.
     *
     * @param conn konekcija ka bazi
     */

    private void resolvePkColumnName(Connection conn) {
        try {
            DatabaseMetaData md = conn.getMetaData();
            try (ResultSet pk = md.getPrimaryKeys(conn.getCatalog(), null, refTable)) {
                ResultSet fkRs = md.getImportedKeys(conn.getCatalog(), null, refTable);
                List<String> fkeys = new ArrayList<>();
                while (fkRs.next()) fkeys.add(fkRs.getString("FKCOLUMN_NAME"));

                while (pk.next()) {
                    String col = pk.getString("COLUMN_NAME");
                    if (!fkeys.contains(col)) { // ignoriraj PK koji je FK
                        pkColumnName = col;
                        break;
                    }
                }
            }
        } catch (SQLException ignore) { }
    }

    private int findColumnIndex(javax.swing.table.TableModel model, String columnName) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (columnName != null && columnName.equals(model.getColumnName(i))) return i;
        }
        return -1;
    }

    private int findIdColumnIndex(javax.swing.table.TableModel model) {
        if (pkColumnName != null) {
            int idx = findColumnIndex(model, pkColumnName);
            if (idx != -1) return idx;
        }
        int idxCommon = findColumnIndex(model, "Identifikator");
        if (idxCommon != -1) return idxCommon;
        return model.getColumnCount() > 0 ? 0 : -1;
    }

    /**
     * Pokušava da pronađe red koji odgovara trenutno selektovanoj vrijednosti
     * i otvara dijalog za izbor reda iz povezane tabele.
     */
    private void selectMatchingRow() {
        javax.swing.table.TableModel model = (tableView != null) ? tableView.getModel() : null;
        if (model == null) return;

        int idCol = findIdColumnIndex(model);
        int dispCol = findColumnIndex(model, refColumn);
        Object current = (selectedId != null) ? selectedId : baseField.getValue();

        int targetRow = 0;
        if (current != null) {
            boolean matched = false;
            if (idCol >= 0) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    Object v = model.getValueAt(i, idCol);
                    if (current.equals(v)) { targetRow = i; matched = true; break; }
                }
            }
            if (!matched && dispCol >= 0) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    Object v = model.getValueAt(i, dispCol);
                    if (current.equals(v)) { targetRow = i; break; }
                }
            }
        }

        new SelectionDialog(this, tableView, refTable, refColumn, targetRow, idCol, dispCol);
        tableView.setRowSelectionInterval(targetRow, targetRow);
    }

    /**
     * Postavlja selektovani red u {LinkedField}.
     *
     * @param id      vrijednost primarnog ključa
     * @param display vrijednost koja se prikazuje korisniku
     */
    void setLinkedSelection(Object id, Object display) {
        this.selectedId = id;
        this.selectedDisplay = display;
        baseField.setValue(display);
    }

    @Override
    public Object getValue() {
        Object value = baseField.getValue();
        if (value == null) return null;

        try {
            DbManipulation instance = DbManipulation.dbManipulations.get(0);
            Connection conn = instance.getConnection();

            ResultSet pks = conn.getMetaData().getPrimaryKeys(instance.getDbName(), "pisg4", refTable);
            ResultSet fks = conn.getMetaData().getImportedKeys(instance.getDbName(), "pisg4", refTable);
            List<String> fkeys = new ArrayList<>();
            while (fks.next()) fkeys.add(fks.getString("FKCOLUMN_NAME"));

            String colName = "";
            boolean isFk = true;
            while (pks.next() && isFk) {
                colName = pks.getString("COLUMN_NAME");
                isFk = false;
                for (String fk : fkeys) if (colName.equals(fk)) isFk = true;
            }

            if ("Drzava".equals(colName)) colName = "Oznaka";

            String query = "SELECT [" + colName + "] FROM [" + refTable + "] WHERE [" + refColumn + "]=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setObject(1, value);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) return rs.getObject(colName); // provjeri da li postoji red
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


@Override
    public void setValue(Object object) {
        //System.out.println("LinkedField.setValue() called for " + getName() + " with value: " + object);

        baseField.setValue(object);
        selectedDisplay = object;

        if (object != null) {
            try {
                DbManipulation instance = DbManipulation.dbManipulations.get(0);
                Connection conn = instance.getConnection();

                ResultSet pks = conn.getMetaData().getPrimaryKeys(instance.getDbName(), "pisg4", refTable);
                ResultSet fks = conn.getMetaData().getImportedKeys(instance.getDbName(), "pisg4", refTable);
                List<String> fkeys = new ArrayList<>();
                while (fks.next()) fkeys.add(fks.getString("FKCOLUMN_NAME"));

                String colName = "";
                boolean isFk = true;
                while (pks.next() && isFk) {
                    colName = pks.getString("COLUMN_NAME");
                    isFk = false;
                    for (String fk : fkeys) if (colName.equals(fk)) isFk = true;
                }

                if ("Drzava".equals(colName)) colName = "Oznaka";

                String query = "SELECT [" + colName + "] FROM [" + refTable + "] WHERE [" + refColumn + "]=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setObject(1, object);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) selectedId = rs.getObject(colName);
                else selectedId = null;

            } catch (Exception e) {
                e.printStackTrace();
                selectedId = null;
            }
        } else {
            selectedId = null;
        }

        //System.out.println("LinkedField.setValue() completed with selectedId=" + selectedId);
    }


    @Override
    public void setName(String name) { ((JComponent) baseField).setName(name); }
    @Override
    public void setReferenceBtn(Component btn) { }
    @Override
    public void setEnabled(boolean enabled) {
        if (baseField == null) return;

        if (owningColumnMeta != null && Boolean.TRUE.equals(owningColumnMeta.isAutoIncrement())) {
            baseField.setEnabled(false);
            if (button != null) button.setEnabled(false);
            return;
        }

        if (button == null) {
            baseField.setEnabled(enabled);
        } else {
            baseField.setEnabled(false);
            button.setEnabled(enabled);
            button.setVisible(true);
        }
    }

    @Override
    public boolean isPrimary() { return baseField.isPrimary(); }
    @Override
    public boolean isNullable() { return baseField.isNullable(); }
    @Override
    public String getName() { return baseField.getName(); }
    @Override
    public void setColumnCode(String code) { baseField.setColumnCode(code); }
    @Override
    public String getColumnCode() { return baseField.getColumnCode(); }

    private static class TableViewFactory { view.Table create(javax.swing.table.TableModel model) { return new view.Table(model); } }

    private static class SimpleTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private final List<String> columns;
        private final List<List<Object>> data;
        SimpleTableModel(List<String> columns, List<List<Object>> data) { this.columns = columns; this.data = data; }
        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return columns.size(); }
        @Override public String getColumnName(int column) { return columns.get(column); }
        @Override public Object getValueAt(int rowIndex, int columnIndex) { return data.get(rowIndex).get(columnIndex); }
    }

	@Override
	public boolean isAutoIncrement() {
		return false;
	}
	
	public Object getLinkedId() {
	    return selectedId;
	}
}
