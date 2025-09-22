package model.states;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import model.GeneralTableModel;
import view.FormPanel;
import view.MainView;
import view.TableFrame;

/**
 * Klasa koja opisuje stanje aplikacije kada je korisnik izabrao neki red iz tabele.
 * U ovom stanju su aktivirane opcije za uređivanje, brisanje, kretanje kroz redove
 * i generisanje izvještaja.
 * 
 * Ova klasa nasljedjuje {@link AbstractAppState} i implementira ponašanje koje je
 * specifično za rad sa selektovanim redovima u tabeli.
 * 
 * @author G4
 */

public class SelectionState extends AbstractAppState {

	public SelectionState(MainView window) {
		super(window);
		statusbar.setState("Selection");

		boolean fullAccess = window.getTablePanel().isFullAccess();
		this.menubar.acceptItem.setEnabled(fullAccess);
		this.menubar.cancelItem.setEnabled(true);
		this.menubar.deleteItem.setEnabled(fullAccess);
		this.menubar.firstItem.setEnabled(true);
		this.menubar.lastItem.setEnabled(true);
		this.menubar.previousItem.setEnabled(true);
		this.menubar.nextItem.setEnabled(true);
		this.menubar.editItem.setEnabled(fullAccess);
		this.menubar.newItem.setEnabled(fullAccess);
		
		this.toolbar.getBtnFirst().setEnabled(true);
		this.toolbar.getBtnNext().setEnabled(true);
		this.toolbar.getBtnPrevious().setEnabled(true);
		this.toolbar.getBtnLast().setEnabled(true);
		this.toolbar.getBtnEdit().setEnabled(fullAccess);
		this.toolbar.getBtnDelete().setEnabled(fullAccess);
		this.toolbar.getBtnAccept().setEnabled(fullAccess);
		this.toolbar.getBtnCancel().setEnabled(true);
		this.toolbar.getBtnNew().setEnabled(fullAccess);
		this.toolbar.getBtnReport().setEnabled(true);
	}

	@Override
	public void handleCreate() {
		 TableFrame tablePanel = window.getTablePanel();
		    if (tablePanel == null) return;
		    FormPanel formPanel = tablePanel.getFormPanel();
		    formPanel.clearAll();
		    formPanel.enableInputs();
		    formPanel.prepareNewRow(tablePanel.getTableModel().getColumns());

		    JDesktopPane desktopPane = window.getDesktopPane();
		    desktopPane.removeAll();
		    desktopPane.setLayout(new BorderLayout());

		    JSplitPane splitPane = new JSplitPane(
		            JSplitPane.VERTICAL_SPLIT,
		            tablePanel,   
		            formPanel    
		    );
		    splitPane.setDividerLocation(350);
		    splitPane.setResizeWeight(0.7);
		    splitPane.setOneTouchExpandable(true);

		    desktopPane.add(splitPane, BorderLayout.CENTER);

		    desktopPane.revalidate();
		    desktopPane.repaint();
		    
		    tablePanel.getTable().getSelectionModel().clearSelection();
		    
		    window.setAppState(new CreationState(window));
		    window.getStatusbar().setCurrentRow(0, window.getTablePanel().getTable().getRowCount());
		    
	}

	@Override
	public void handleChange() {

	}

	
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
	            System.out.println("DEBUG: Brisanje jednog PK: " + pkValue);
	            success = tblModel.deleteRowById(pkValue);
	        } else {
	            ArrayList<Object> pkValues = new ArrayList<>(pkMap.values());
	            System.out.println("DEBUG: Brisanje višestrukih PK: " + pkValues);
	            success = tblModel.deleteRowById(pkValues);
	        }

	        if (success) {
	            JOptionPane.showMessageDialog(window, "Red uspješno obrisan.");
	            formPanel.clearAll();
	            window.getTablePanel().getFormPanel().setVisible(false);;
	            tblModel.getAllData();
	            window.getTablePanel().getTable().revalidate();
	            window.getTablePanel().adjustColumnWidths();
	    	    window.getStatusbar().setCurrentRow(0, window.getTablePanel().getTable().getRowCount());

	    	    window.setAppState(new ActiveState(window));
	        } else {
	            JOptionPane.showMessageDialog(window, "Brisanje nije uspjelo: " + tblModel.getLastErrorMesagge());
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


	@Override
	public void handleSubmit() {
	}

	@Override
	protected void handleSpecificSelection() {

	}

	@Override
	protected void handleSpecificSubmit() {

	}

	@Override
	public void handleNext() {
		int red = window.getTablePanel().getTable().getSelectedRow();
		red++;
		if (red >= window.getTablePanel().getTable().getRowCount())
			red = 0;
		window.getTablePanel().getTable().setRowSelectionInterval(red, red);

	}

	@Override
	public void handleFirst() {
		window.getTablePanel().getTable().setRowSelectionInterval(0, 0);

	}

	@Override
	public void handlePrev() {
		int red = window.getTablePanel().getTable().getSelectedRow();
		red--;
		if (red < 0)
			red = window.getTablePanel().getTable().getRowCount() - 1;
		window.getTablePanel().getTable().setRowSelectionInterval(red, red);

	}

	@Override
	public void handleLast() {
		window.getTablePanel().getTable().setRowSelectionInterval(window.getTablePanel().getTable().getRowCount() - 1, window.getTablePanel().getTable().getRowCount() - 1);

	}

}
