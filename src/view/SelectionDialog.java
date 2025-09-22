package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

/**
 * Selektorski dijalog koji omogućava korisniku da odabere red iz tabele.
 * 
 * Dijalogu se prosleđuje referenca na {@link LinkedField}, tabelarni prikaz
 * i meta-informacije (ime tabele i kolone). Nakon izbora reda i potvrde,
 * rezultat se vraća nazad u povezani {@code LinkedField}.
 * 
 * Dijalogu se automatski dodaje navigaciona traka, kao i dugmad za
 * potvrdu ("Select") i odustanak ("Cancel").  
 *
 * @author G4
 */
public class SelectionDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private Table table;
	private int idCol = -1;
	private int dispCol = -1;

	private Object selectedValue;

	/**
	 * Konstruktor za inicijalizaciju dijaloga kada nisu poznati
	 * indeksi ID i prikazne kolone.
	 *
	 * @param linkedField referenca na {@link LinkedField} koji koristi dijalog
	 * @param tableView tabelarni prikaz koji se prikazuje u dijalogu
	 * @param tableName naziv povezane tabele
	 * @param refColumn naziv referencirane kolone
	 * @param currentRow red koji treba unapred selektovati
	 */
	public SelectionDialog(LinkedField linkedField, Table tableView, String tableName, String refColumn, int currentRow) {
		this(linkedField, tableView, tableName, refColumn, currentRow, -1, -1);
	}

	/**
	 * Glavni konstruktor koji kreira modalni dijalog za izbor reda iz tabele.
	 *
	 * @param linkedField referenca na {@link LinkedField} koji koristi dijalog
	 * @param tableView tabelarni prikaz koji se prikazuje u dijalogu
	 * @param tableName naziv povezane tabele
	 * @param refColumn naziv kolone koja predstavlja vrijednost za prikaz
	 * @param currentRow red koji treba unapred selektovati
	 * @param idCol indeks kolone koja predstavlja primarni ključ (ID) ili -1 ako nije poznat
	 * @param dispCol indeks kolone koja predstavlja vrijednost za prikaz ili -1 ako nije poznat
	 */
	public SelectionDialog(LinkedField linkedField, Table tableView, String tableName, String refColumn, int currentRow, int idCol, int dispCol) {
		setTitle(tableName);
		this.table = tableView;
		this.idCol = idCol;
		this.dispCol = dispCol;
		tableView.setTableModel();

		if (currentRow > -1) {
			int colIdx = -1;
			if (tableView.getModel() instanceof model.GeneralTableModel) {
				colIdx = ((model.GeneralTableModel) tableView.getModel()).getIndexOfColumn(refColumn);
			} else {
				for (int i = 0; i < tableView.getModel().getColumnCount(); i++) {
					if (refColumn.equals(tableView.getModel().getColumnName(i))) { colIdx = i; break; }
				}
			}
			if (colIdx >= 0 && currentRow < tableView.getRowCount()) {
				this.selectedValue = tableView.getValueAt(currentRow, (this.idCol!=-1? this.idCol : colIdx));
			}
		}

		setIconImage(new ImageIcon("resources/logo.png").getImage());
		setLayout(new BorderLayout());

		NavigationToolbar navToolbar = new NavigationToolbar(this);
		add(navToolbar, BorderLayout.NORTH);

		JButton btnOk = new JButton("Select");
		btnOk.setPreferredSize(new Dimension(200, 20));
		btnOk.setBackground(new Color(51, 153, 255));
		btnOk.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int idc = (this.idCol != -1) ? this.idCol : 0;
				int dc = this.dispCol;
				if (dc == -1) {
					dc = -1;
					for (int i = 0; i < table.getModel().getColumnCount(); i++) {
						if (refColumn.equals(table.getModel().getColumnName(i))) { dc = i; break; }
					}
				}
				Object id = table.getModel().getValueAt(row, idc);
				Object disp = (dc>=0? table.getModel().getValueAt(row, dc) : null);
				linkedField.setLinkedSelection(id, disp);
			}
			dispose();
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setPreferredSize(new Dimension(200, 20));
		btnCancel.setBackground(new Color(51, 153, 255));
		btnCancel.addActionListener(e -> dispose());

		tableView.setSelectionListener(e -> {
			if (e.getValueIsAdjusting()) return;
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (!lsm.isSelectionEmpty()) {
				int selectedRow = lsm.getLeadSelectionIndex();
				int selectedCol = -1;
				if (tableView.getModel() instanceof model.GeneralTableModel) {
					selectedCol = ((model.GeneralTableModel) tableView.getModel()).getIndexOfColumn(refColumn);
				} else {
					for (int i = 0; i < tableView.getModel().getColumnCount(); i++) {
						if (refColumn.equals(tableView.getModel().getColumnName(i))) { selectedCol = i; break; }
					}
				}
				if (selectedCol >= 0) {
					selectedValue = tableView.getModel().getValueAt(selectedRow, (this.idCol!=-1? this.idCol : selectedCol));
				}
			}
		});

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(tableView);
		setPreferredSize(new Dimension(900, 300));
		scrollPane.getViewport().setBackground(new Color(204, 228, 252));
		tableView.setOpaque(false);
		Border border = BorderFactory.createLineBorder(new Color(51, 153, 255), 2);
		scrollPane.setBorder(border);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setPreferredSize(new Dimension(800, 50));
		pnlButtons.setBackground(new Color(204, 228, 252));
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
		pnlButtons.add(Box.createHorizontalGlue());
		pnlButtons.add(btnOk);
		pnlButtons.add(Box.createHorizontalStrut(10));
		pnlButtons.add(btnCancel);
		pnlButtons.add(Box.createHorizontalGlue());
		getRootPane().setDefaultButton(btnOk);

		if (currentRow >= 0 && currentRow < tableView.getRowCount()) {
			tableView.setRowSelectionInterval(currentRow, currentRow);
		}

		add(scrollPane, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);

		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void handleNext() {
		int row = table.getSelectedRow();
		row++;
		if (row >= table.getRowCount()) row = 0;
		table.setRowSelectionInterval(row, row);
	}

	public void handleFirst() {
		table.setRowSelectionInterval(0, 0);
	}

	public void handlePrev() {
		int row = table.getSelectedRow();
		row--;
		if (row < 0) row = table.getRowCount() - 1;
		table.setRowSelectionInterval(row, row);
	}

	public void handleLast() {
		table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
	}
}
