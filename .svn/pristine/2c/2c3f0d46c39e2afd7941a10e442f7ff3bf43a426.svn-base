package main;


import controller.LoginController;
import controller.LoginModel;
import controller.LoginView;

/* username: mmarkovic
 * password: 12345
 */

/**
 * Glavna klasa aplikacije koja pokrece korisnicki interfejs.
 * Prvo se prikazuje login forma, a nakon uspjesne autentifikacije 
 * moze se prikazati i glavna forma aplikacije (MainView).
 * 
 * @author G4
 *  */
public class Main {
	
	/**
	 * Ulazna tacka aplikacije.
	 * 
	 * Kreira se login forma i prikazuje korisniku.
	 * Nakon uspjesnog logovanja, moze se instancirati glavni prikaz aplikacije.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
            LoginView loginView = new LoginView();
            LoginModel loginModel = new LoginModel();
            LoginController loginController = new LoginController(loginModel, loginView);
            loginView.pack();
            loginView.setVisible(true);
    }
}