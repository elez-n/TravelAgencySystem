/***********************************************************************
 * Module:  MainView.java
 * Author:  Korisnik
 * Purpose: Defines the Class MainView
 ***********************************************************************/

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Model;
import model.TreeElement;
import model.states.IAppState;


/**
 *Klasa {@code MainView} predstavlja glavni grafički prozor aplikacije
 * "TransPlan - Agencija za prevoz putnika".
 *
 * Klasa takođe prati trenutno stanje aplikacije preko {@link IAppState} i trenutno otvorenu tabelu.
 * 
 * @author G4
 */
public class MainView extends JFrame{
   
   public JDesktopPane desktopPane;

   public TableFrame tablePanel = null;
   public Toolbar toolbar;
   public Table table;
   public Menubar menubar;
   public Statusbar statusbar;
   public BrowserPanel browserPanel;
   public JPanel mainPanel;
   private IAppState appState;
   private TreeElement.Table currentTable;


   
   /**
    * Konstruktor koji inicijalizuje glavni prozor i sve njegove komponente.
    * 
    * @param model model aplikacije koji sadrži podatke i stablo tabela
    */
   public MainView(Model model) {
       setTitle("TransPlan - Agencija za prevoz putnika");
       setSize(1000, 700);
       setLocationRelativeTo(null); 
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       ImageIcon logoo = new ImageIcon("resources/logoo.png");
       setIconImage(logoo.getImage());

       menubar = new Menubar();
       setJMenuBar(menubar);
       
       toolbar = new Toolbar();
       getContentPane().add(toolbar, BorderLayout.NORTH);

       desktopPane = new JDesktopPane();
       getContentPane().add(desktopPane, BorderLayout.CENTER);
       
       browserPanel = new BrowserPanel(model, this); 
       browserPanel.setPreferredSize(new Dimension(250, 0)); 
       getContentPane().add(browserPanel, BorderLayout.WEST);

       statusbar = new Statusbar();
       getContentPane().add(statusbar, BorderLayout.SOUTH);
       
   }
   

   
	public void setAppState(IAppState appState) {
		this.appState = appState;
	}

    public BrowserPanel getBrowserPanel() {
		return browserPanel;
	}



	public IAppState getAppState()
	{
		return appState;
	}

   public TableFrame getTablePanel() {
	return tablePanel;
}

   public Toolbar getToolbar() {
	return toolbar;
   }

   public Table getTable() {
	return table;
   }

   public Menubar getMenubar() {
	return menubar;
   }

   public Statusbar getStatusbar() {
	return statusbar;
   }

   public JDesktopPane getDesktopPane() {
       return desktopPane;
   }
   
   public boolean hasOpenTable() {
	    return currentTable != null;
	}

	public void setCurrentTable(TreeElement.Table table) {
	    this.currentTable = table;
	}

	public TreeElement.Table getCurrentTable() {
	    return currentTable;
	}
	
    /**
     * Zatvara sve trenutno otvorene komponente aplikacije i vraća stanje
     * korisničkog interfejsa na početno.
     * 
     * Uklanja sve prikazane unutrašnje prozore iz {desktopPane}, 
     * resetuje prikazanu tabelu i trenutno selektovani red, briše selekciju 
     * u stablu aplikacionog browsera i postavlja aplikaciju u 
     * {model.states.IdleState}.
     * 
     */
	public void closeAll() {
	    desktopPane.removeAll();
	    desktopPane.revalidate();
	    desktopPane.repaint();

	    tablePanel = null;
	    currentTable = null;

	    statusbar.setSelectedTable("");
	    statusbar.setCurrentRow(0, 0);

	    browserPanel.getTree().clearSelection();

	    setAppState(new model.states.IdleState(this));
	}



}