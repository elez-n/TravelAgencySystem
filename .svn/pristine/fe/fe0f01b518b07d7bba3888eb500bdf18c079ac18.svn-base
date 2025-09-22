package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

/**
 * JFormattedTextField prilagodjen za tekstualni unos bez overwrite moda gdje je potrebno ograniciti samo duzinu unosa.
 * @author G4

 */
public class CustomTextField extends JFormattedTextField {
	private static final long serialVersionUID = 1L;
	private int limit;

    /**
     * Kontstruktor koji inicijalizuje tekstualno polje na osnovu proslijedjenog ogranicenja i formatera
     * 
     * @param limit maksimalan broj karaktera
     * @param formatter formater teksta
     */
    public CustomTextField(int limit, DefaultFormatter formatter) {
        super(formatter);
    	this.limit = limit;

    	 this.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {

				checkLength();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {

				checkLength();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {

				checkLength();
			}
		});
       }
    /**
     * Pomocna metoda za provjeru duzine unosa u documentListeneru
     */
    private void checkLength() {
        if (getText().length() > limit) {
            SwingUtilities.invokeLater(() -> {
                setText(getText().substring(0, limit));
                JOptionPane.showMessageDialog(null, "Maksimalan broj karaktera za ovo polje je " + limit + ".");
            });
        }
    }
   
    
}