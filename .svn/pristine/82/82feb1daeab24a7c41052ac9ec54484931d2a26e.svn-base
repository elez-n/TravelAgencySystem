package model.states;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import model.CustomTableModel;
import model.GeneralTableModel;
import view.FormPanel;
import view.MainView;

/**
 * Klasa koja predstavlja stanje aplikacije pri izmjeni selektovanog reda tabele.
 * 
 * U ovom stanju korisniku su omogućene akcije izmjene podataka, potvrde ili
 * otkazivanja izmjena. Određeni elementi menija i alatne trake su onemoguceni
 * kako bi se sprijecilo narusavanje konzistentnosti podataka.
 * 
 * @author G4
 */
public class EditState extends AbstractAppState {

	 /**
     * Konstruktor koji postavlja prozor aplikacije u stanje izmjene.
     *
     * @param window glavna forma aplikacije
     */
	public EditState(MainView window) {
		super(window);

		statusbar.setState("Modifying");

		this.menubar.acceptItem.setEnabled(true);
		this.menubar.cancelItem.setEnabled(true);
		this.menubar.deleteItem.setEnabled(false);
		this.menubar.firstItem.setEnabled(false);
		this.menubar.lastItem.setEnabled(false);
		this.menubar.previousItem.setEnabled(false);
		this.menubar.nextItem.setEnabled(false);
		this.menubar.editItem.setEnabled(false);
		this.menubar.newItem.setEnabled(false);

		toolbar.setButtonsInModifyState();
	}

	@Override
	public void handleNext() {

	}

	@Override
	public void handleFirst() {
	}

	@Override
	public void handlePrev() {
	}

	@Override
	public void handleLast() {
	}

	
	@Override
	protected void handleSpecificSelection() {
	}

	@Override
	public void handleCreate() {
	}


    /**
     * Specifična logika za potvrdu izmjene reda u tabeli.
     * Ažurira model podataka i obavještava korisnika o uspješnosti.
     */
	@Override
	protected void handleSpecificSubmit() {
	    boolean success = false;
	    CustomTableModel tblModel = (CustomTableModel) window.getTablePanel().getTable().getModel();
	    
	    int selectedRow = window.getTablePanel().getTable().getSelectedRow();
	    
	    success = tblModel.editRow(window.getTablePanel().getFormPanel().getValues());
	    if (success) {
	        JOptionPane.showMessageDialog(null, "Uspješna izmjena reda.");
	        tblModel.getAllData();
	        
	        int newRowCount = window.getTablePanel().getTable().getRowCount();
	        if (selectedRow >= 0 && selectedRow < newRowCount) {
	            window.getTablePanel().getTable().setRowSelectionInterval(selectedRow, selectedRow);
	        }
	        window.getTablePanel().getTable().revalidate();
	        window.getTablePanel().adjustColumnWidths();
	    } else {
	        JOptionPane.showMessageDialog(null, "Greška prilikom izmjene reda: "+tblModel.getLastErrorMesagge());
	    }
	}


	 /**
     * Preuzima podatke iz selektovanog reda i popunjava formu
     * za izmjenu. Primarni ključevi su onemogućeni za uređivanje.
     */
	@Override
	public void handleChange() {
		int row = window.getTablePanel().getTable().getSelectedRow();
		int cols = window.getTablePanel().getTable().getColumnCount();
		//System.out.println(row + " i " + cols);

		if (row >= 0) {
			List<Object> rowData = new ArrayList<>();
			for (int i = 0; i < cols; i++) {
				rowData.add(window.getTablePanel().getTable().getValueAt(row, i));
				//System.out.println(rowData);
			}
			window.getTablePanel().getFormPanel().clearAll();
			window.getTablePanel().getFormPanel().fillInputs(rowData);
			window.getTablePanel().getFormPanel().enableInputs();
			window.getTablePanel().getFormPanel().disablePrimaryInputs();
			window.getTablePanel().getFormPanel().setVisible(true);
			//System.out.println("rowData size = " + rowData.size());
			//System.out.println("form inputs size = " + window.getTablePanel().getFormPanel().getInputFields().size());
		}
	}

	 /**
     * Briše selektovani red na osnovu primarnog ključa.
     * Korisniku se prikazuje dijalog za potvrdu prije brisanja.
     */
	@Override
	public void handleDelete() {
	    int selectedRow = window.getTablePanel().getTable().getSelectedRow();
	    if (selectedRow < 0) {
	        JOptionPane.showMessageDialog(window, "Nijedan red nije selektovan.");
	        return;
	    }

	    GeneralTableModel tblModel = (GeneralTableModel) window.getTablePanel().getTable().getModel();
	    FormPanel formPanel = window.getTablePanel().getFormPanel();

	    HashMap<String, Object> pkMap = formPanel.getPrimaryKeys();



	    if (pkMap.isEmpty()) {
	        JOptionPane.showMessageDialog(window, "Primarni ključ nije pronađen, brisanje nije moguće.");
	        return;
	    }

	    int result = JOptionPane.showConfirmDialog(window,
	            "Da li želite da obrišete selektovani red?",
	            "Potvrda brisanja", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

	    if (result == JOptionPane.YES_OPTION) {
	        boolean success;
	        if (pkMap.size() == 1) {
	            Object pkValue = pkMap.values().iterator().next();
	            //System.out.println("DEBUG: Brisanje jednog PK: " + pkValue);
	            success = tblModel.deleteRowById(pkValue);
	        } else {
	            ArrayList<Object> pkValues = new ArrayList<>(pkMap.values());
	            //System.out.println("DEBUG: Brisanje višestrukih PK: " + pkValues);
	            success = tblModel.deleteRowById(pkValues);
	        }

	        if (success) {
	            JOptionPane.showMessageDialog(window, "Red uspješno obrisan.");
	            formPanel.clearAll();
	            window.getTablePanel().getFormPanel().setVisible(false);
	            tblModel.getAllData();
	            window.getTablePanel().getTable().revalidate();
	            window.getTablePanel().adjustColumnWidths();
	        } else {
	            JOptionPane.showMessageDialog(window, "Brisanje nije uspjelo: "+tblModel.getLastErrorMesagge());
	            //System.out.println("DEBUG: deleteRowById vratio false!");
	        }
	    }
	}

	@Override
	public void handleCancel() {
	    window.getTablePanel().getTable().clearSelection();
	    window.getTablePanel().getFormPanel().clearAll();

	    JDesktopPane desktopPane = window.getDesktopPane();
	    desktopPane.removeAll();
	    desktopPane.setLayout(new BorderLayout());
	    desktopPane.add(window.getTablePanel(), BorderLayout.CENTER);
	    desktopPane.revalidate();
	    desktopPane.repaint();

	    window.getStatusbar().setCurrentRow(0, window.getTablePanel().getTable().getRowCount());

	    window.setAppState(new ActiveState(window));
	}
	
	

	 /**
     * Potvrđuje unos u formi. Ako obavezna polja nisu popunjena,
     * prikazuje poruku o grešci. U suprotnom, poziva specifičnu
     * logiku za čuvanje izmjena.
     */
	@Override
	public void handleSubmit() {
		if (!window.getTablePanel().getFormPanel().validateInputs()) {
			JOptionPane.showMessageDialog(null, "Polja oznacena sa * su obavezna!");
		} else {
			handleSpecificSubmit();
		}
	}

}
