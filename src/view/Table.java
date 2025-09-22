/***********************************************************************
 * Module:  Table.java
 * Author:  Korisnik
 * Purpose: Defines the Class Table
 ***********************************************************************/

package view;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/** JTable omotač sa pomoćnim metodama koje koriste ostale klase. */
public class Table extends JTable {
	private static final long serialVersionUID = 1L;

	public Table() {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public Table(TableModel model) {
		super(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void setSelectionListener(ListSelectionListener listener) {
		getSelectionModel().addListSelectionListener(listener);
	}

	/**
	 * Kompatibilnost: u nekim primerima se poziva setTableModel() pre rada sa tabelom.
	 * Ovdje nije potrebna posebna logika pa metoda ostaje prazna.
	 */
	public void setTableModel() {
	}

	public model.GeneralTableModel getTableModel() {
		TableModel m = getModel();
		if (m instanceof model.GeneralTableModel) {
			return (model.GeneralTableModel) m;
		}
		return null;
	}
}