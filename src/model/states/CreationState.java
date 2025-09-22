package model.states;

import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import model.CustomTableModel;
import view.FormPanel;
import view.MainView;

/**
 * Klasa koja opisuje stanje kreiranja i ažurira korisnički interfejs
 * pri dodavanju novog reda u tabelu.
 * 
 * @author G4
 */

public class CreationState extends AbstractAppState {

    public CreationState(MainView window) {
        super(window);
        
        FormPanel form = window.getTablePanel().getFormPanel();
        form.clearAll();
        form.enableInputs();
        //form.disablePrimaryInputs(); 

        statusbar.setState("Creating");
        toolbar.setVisible(true);
        toolbar.setButtonsInCreationState();
        this.menubar.acceptItem.setEnabled(true);
        this.menubar.cancelItem.setEnabled(true);
        this.menubar.deleteItem.setEnabled(false);
        this.menubar.firstItem.setEnabled(false);
        this.menubar.lastItem.setEnabled(false);
        this.menubar.previousItem.setEnabled(false);
        this.menubar.nextItem.setEnabled(false);
        this.menubar.editItem.setEnabled(false);
        this.menubar.newItem.setEnabled(false);
    }

    @Override
    public void handleCreate() {
        form.clearAll();
        form.setVisible(true);
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

        window.setAppState(new ActiveState(window));
    }


    @Override
    protected void handleSpecificSelection() {
 
    }

    @Override
    protected void handleSpecificSubmit() {
        boolean success = false;
        CustomTableModel tblModel = (CustomTableModel) window.getTablePanel().getTable().getModel();

        success = tblModel.addRow(window.getTablePanel().getFormPanel().getValues());
        if (success) {
            JOptionPane.showMessageDialog(null, "Uspješno dodat novi red.");
            
            tblModel.getAllData();

            int newRowCount = window.getTablePanel().getTable().getRowCount();
            if (newRowCount > 0) {
                window.getTablePanel().getTable().setRowSelectionInterval(newRowCount - 1, newRowCount - 1);
            }

            window.getTablePanel().getTable().revalidate();
            window.getTablePanel().adjustColumnWidths();
        } else {
            JOptionPane.showMessageDialog(null, "Greška prilikom dodavanja reda: " + tblModel.getLastErrorMesagge());
        }
    }


    @Override
    public void handleNext() {}

    @Override
    public void handleFirst() {}

    @Override
    public void handlePrev() {}

    @Override
    public void handleLast() {}

    @Override
    public void handleChange() {}

	@Override
	public void handleSubmit() {
		if (!window.getTablePanel().getFormPanel().validateInputs()) {
			JOptionPane.showMessageDialog(null, "Polja oznacena sa * su obavezna!");
		} else {
			handleSpecificSubmit();
		}
	}
}