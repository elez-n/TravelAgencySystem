package db;


import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;


/**
 *Apstraktna klasa DbConnection predstavlja osnovnu (apstraktnu) klasu za upravljanje konekcijama ka bazama podataka.
 * 
 * Ova klasa sadrži osnovne metode za uspostavljanje i provjeru validnosti konekcije,
 * kao i za zatvaranje konekcije. Takođe, sadrži i podatke o parametrima potrebnim za konekciju,
 * kao što su adresa, port, ime baze, korisnicko ime i lozinka.
 * @author G4
 */

public abstract class dbConnection {
	
	/**
	 * Objekat konekcije ka bazi podataka.
	 */
	protected Connection conn = null;
	
	/**
	 * Parametri konekcije.
	 */
	String address = null;
	String fullAddress = null;
	String dbName = null;
	String port = null;
	String driver = null;
	String url = null;
	String user = null;
	String password = null;
	
	/**
	 * Apstraktna metoda za uspostavljanje konekcije ka bazi podataka.
	 * Implementaciju ove metode treba definisati u izvedenim klasama.
	 * @param address adresa servera baze
	 * @param port port baze
	 * @param dbName naziv baze
	 * @param user korisničko ime
	 * @param password lozinka
	 * @return objekat konekcije
	 */
	public abstract Connection GetConnection(String address, String port, String dbName, String user, String password); {
		
	}
	
	
	/**
	 * Zatvara konekciju ako je otvorena.
	 * U slučaju greške prikazuje se poruka korisniku.
	 */
	public void closeConnection() {
		if(isConnectionOpen())
		{
			try {
				conn.close();
			} catch (SQLException e) {
				ErrorHandlerMethod(null, e.getMessage());
			}
			finally {
				conn = null;
			}
		}
	}
	
	
	/**
	 * Provjera da li je konekcija otvorena.
	 * @return true ako je konekcija otvorena, false ako nije.
	 */
	public boolean isConnectionOpen() {
		return conn != null;
	}
	
	
	/**
	 * Provjera da li je konekcija validna u zadatom vremenskom intervalu.
	 * @param timeout broj sekundi za provjeru validnosti.
	 * @return true ako je konekcija validna, false ako nije.
	 */
	public boolean isConnectionValid(int timeout) {
		if(isConnectionOpen())
		{
			try {
				return conn.isValid(timeout);
			} catch (SQLException e) {
				ErrorHandlerMethod(null, e.getMessage());
			}
		}				
		return false;		
	}
	
	public Connection getConn() {
		return conn;
	}

	public String getAddress() {
		return address;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public String getDbName() {
		return dbName;
	}

	public String getPort() {
		return port;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
	
	
    /**
     * Prikazuje poruku o grešci korisniku putem dijaloga.
     * @param component komponenta na koju se dijalog odnosi (može biti null).
     * @param errorMessage tekst greške koji se prikazuje.
     */ 
	protected void ErrorHandlerMethod(Component component, String errorMessage) {
		JOptionPane.showMessageDialog(component, errorMessage, "Error", 0);
	}

}