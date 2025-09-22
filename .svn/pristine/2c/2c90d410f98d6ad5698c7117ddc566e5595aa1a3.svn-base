

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JOptionPane;

import model.Model;
import view.MainView;
/**
 * Klasa LoginController upravlja logikom prijave korisnika.
 * Povezuje LoginModel i LoginView, osluškuje događaj klika na dugme za prijavu 
 * i na osnovu unesenih podataka odlučuje da li će otvoriti glavni prozor aplikacije
 * ili prikazati poruku o grešci.
 * @author Korisnik
 */
public class LoginController {
	/**
	 * Referenca na model podataka za prijavu.
	 */
    private LoginModel loginModel;
    
    
    /**
     * Referenca na prikaz za prijavu.
     */
    private LoginView loginView;
    
    
    
	/**
	 * Konstruktor klase LoginController.
	 * Inicijalizuje model i prikaz, te dodaje {@link ActionListener} na dugme za prijavu.
	 * @param loginModel instanca modela prijave
	 * @param loginView instanca prikaza prijave
	 */
	public LoginController(LoginModel loginModel, LoginView loginView) {
	       this.loginModel = loginModel;
	       this.loginView = loginView;

	       this.loginView.getLoginButton().addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent e) {
	               String username = loginView.getUsername();
	               String password = loginView.getPassword();

	              /**
	               * Poziva model za provjeru korisničkih podataka.
	               */

	               System.out.println("Login dugme kliknuto");
	               if(loginModel.login(username, password))
	               {
	            	   
	            	  openMainWindow();

	                  System.out.println("Otvorio main prozor");
	               }
	               else {
	                   JOptionPane.showMessageDialog(loginView,
	                           "Greška! Nisu uneseni ispravni podaci.",
	                           "Prijava neuspješna",
	                           JOptionPane.ERROR_MESSAGE);
	               }
	       }
	           
	           
	           
	           /**
	            * Otvara glavni prozor aplikacije i zatvara prozor za prijavu.
	            */
	           
	           private void openMainWindow() {
	        	   Model model = new Model(LoginModel.accessLevel);
	        	   
	        	   MainView appView = new MainView(model);
	        	   Controller cont = new Controller(model, appView);
	        	   appView.setVisible(true);
	        	   loginView.setVisible(false);
	           }
	       });
	       }
}

