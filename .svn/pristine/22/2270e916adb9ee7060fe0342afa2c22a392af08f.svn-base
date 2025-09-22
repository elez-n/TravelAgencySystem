package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * {@code Statusbar} predstavlja statusnu traku aplikacije koja se obično nalazi
 * na dnu glavnog prozora.
 * 
 * @author G4
 */
public class Statusbar extends JPanel{
	 	
		private static final long serialVersionUID = 1L;
		private JLabel selectedTableLabel;
	    private JLabel stateLabel;
	    private JLabel currentRowLabel;

	    
	    /**
	     * Konstruktor koji inicijalizuje statusnu traku sa tri labela i odgovarajućim stilom.
	     */
	    public Statusbar() {
	    	 setLayout(new BorderLayout());
	         setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
	         setBackground(new Color(173, 216, 230));
	         setPreferredSize(new Dimension(100, 40)); 

	         selectedTableLabel = new JLabel("Tabela: ");
	         stateLabel = new JLabel("Stanje: ");
	         currentRowLabel = new JLabel("Red: ");

	         Font font = new Font("SansSerif", Font.BOLD, 12);
	         selectedTableLabel.setFont(font);
	         stateLabel.setFont(font);
	         currentRowLabel.setFont(font);
	         
	         selectedTableLabel.setForeground(Color.WHITE);
	         stateLabel.setForeground(Color.WHITE);
	         currentRowLabel.setForeground(Color.WHITE);

	         selectedTableLabel.setHorizontalAlignment(SwingConstants.LEFT);
	         stateLabel.setHorizontalAlignment(SwingConstants.CENTER);
	         currentRowLabel.setHorizontalAlignment(SwingConstants.RIGHT);

	         JPanel contentPanel = new JPanel(new GridLayout(1, 3));
	         contentPanel.setOpaque(false);
	         contentPanel.add(selectedTableLabel);
	         contentPanel.add(stateLabel);
	         contentPanel.add(currentRowLabel);

	         add(contentPanel, BorderLayout.CENTER);
	     }

	     public void setSelectedTable(String tableName) {
	         selectedTableLabel.setText("Tabela: " + tableName);
	     }

	     public void setState(String state) {
	         stateLabel.setText("Stanje: " + state);
	     }

	     public void setCurrentRow(int selected, int total) {
	         currentRowLabel.setText("Red: " + selected + " / " + total);
	     }
}