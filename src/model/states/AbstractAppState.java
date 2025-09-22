package model.states;
import javax.swing.event.ListSelectionEvent;

import view.MainView;
import view.FormPanel;
import view.Menubar;
import view.Statusbar;
import view.Table;
import view.Toolbar;

/**
 * Apstraktna klasa koju nasljedjuju konkretna stanja aplikacije i koja definise
 * zajednicko ponasanje za sva stanja aplikacije.
 * 
 * @author G4
 */

public abstract class AbstractAppState implements IAppState {
	protected MainView window;
	protected Table table;
	protected FormPanel form;
	protected Toolbar toolbar;
	protected Statusbar statusbar;
	protected Menubar menubar;

	/**
	 * Konstruktor koji inicijalizuje stanje aplikacije sa referencama na UI komponente.
	 * 
	 * @param window glavna forma aplikacije
	 */
	public AbstractAppState(MainView window) {
		this.window = window;
		table = window.getTable();
		menubar = window.getMenubar();
		toolbar = window.getToolbar();
		statusbar = window.getStatusbar();
	}

	/**
	 * Metoda za obradu dogadjaja selekcije reda u tabeli od strane korisnika u
	 * zavisnosti od stanja aplikacije
	 */
	protected abstract void handleSpecificSelection();

	/**
	 * Metoda za definisanje ponasanja Submit dugmeta u razlicitim stanjima
	 * aplikacije
	 */
	protected abstract void handleSpecificSubmit();

	@Override
	public void handleSelectionChanged(ListSelectionEvent e) {


	}

	@Override
	public void handleCreate() {

	}

	@Override
	public void handleChange() {

	}

	@Override
	public void handleDelete() {

	}

	@Override
	public void handleCancel() {

	}

}