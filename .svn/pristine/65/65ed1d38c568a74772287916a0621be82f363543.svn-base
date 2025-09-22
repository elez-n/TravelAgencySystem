

package model.states;

import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;

import view.FormPanel;
import view.MainView;
import view.TableFrame;

/** 
 * Klasa ActiveState predstavlja aktivno stanje aplikacije u okviru obrasca stanja (State pattern).
 * 
 * U ovom stanju toolbar je vidljiv, odredjene stavke menija su omogucene,
 * dok su druge onemogucene u skladu sa logikom rada aplikacije.
 * 
 * Nasljedjuje apstraktnu klasu {@link AbstractAppState} i implementira njene metode.
 * 
 * @author G4
 *  */
public class ActiveState extends AbstractAppState {

	/**
	 * Konstruktor klase {@link ActiveState}.
	 * 
	 * Inicijalizuje toolbar, statusnu traku i menubar tako da odrzavaju
	 * aktivno stanje aplikacije.
	 * 
	 * @param window referenca na glavni prozor aplikacije {@link MainView}
	 */
	public ActiveState(MainView window) {
		super(window);

		statusbar.setState("Active");

		this.toolbar.setVisible(true);
		
		
		boolean fullAccess = window.getTablePanel().isFullAccess();

		this.menubar.acceptItem.setEnabled(false);
		this.menubar.cancelItem.setEnabled(false);
		this.menubar.deleteItem.setEnabled(false);
		this.menubar.firstItem.setEnabled(false);
		this.menubar.lastItem.setEnabled(false);
		this.menubar.previousItem.setEnabled(false);
		this.menubar.nextItem.setEnabled(false);
		this.menubar.editItem.setEnabled(false);

		this.toolbar.getBtnFirst().setEnabled(false);
		this.toolbar.getBtnNext().setEnabled(false);
		this.toolbar.getBtnPrevious().setEnabled(false);
		this.toolbar.getBtnLast().setEnabled(false);
		this.toolbar.getBtnEdit().setEnabled(false);
		this.toolbar.getBtnDelete().setEnabled(false);
		this.toolbar.getBtnAccept().setEnabled(false);
		this.toolbar.getBtnCancel().setEnabled(false);

		// razlike zavise od fullAccess
		this.menubar.newItem.setEnabled(fullAccess);
		this.toolbar.getBtnNew().setEnabled(fullAccess);

		this.toolbar.getBtnReport().setEnabled(true);

	}

	@Override
	public void handleSelectionChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleCreate() {
	    TableFrame tablePanel = window.getTablePanel(); 
	    if (tablePanel == null) return;

	    FormPanel formPanel = tablePanel.getFormPanel();

	    formPanel.clearAll();
	    formPanel.prepareNewRow(tablePanel.getTableModel().getColumns());
	    formPanel.setVisible(true);

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

	    window.setAppState(new CreationState(window));
	}



	@Override
	public void handleChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSubmit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleFirst() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePrev() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleSpecificSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleSpecificSubmit() {
		// TODO Auto-generated method stub
		
	}
}