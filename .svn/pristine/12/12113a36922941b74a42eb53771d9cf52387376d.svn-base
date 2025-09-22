package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


/**
 * Klasa MSSQLjdbcConnection predstavlja konkretnu implementaciju konekcije ka Microsoft SQL Server bazi podataka.
 * Nasljeđuje apstraktnu klasu {@link dbConnection} i implementira metodu za uspostavljanje konekcije
 * koristeći JDBC drajver i parametre kao što su adresa servera, port, naziv baze, korisničko ime i lozinka.
 * Konekcija se uspostavlja pomoću klase {@link DriverManager} i dodatnih sigurnosnih parametara.
 * 
 * @author G4
 */
public class MSSQLjdbcConnection extends dbConnection {

	
	/**
	 * Implementacija metode za uspostavljanje konekcije ka MSSQL bazi podataka.
	 * 
	 * @param address adresa servera baze
	 * @param port port baze
	 * @param dbName naziv baze
	 * @param user korisničko ime
	 * @param password lozinka
	 */
    @Override
    public Connection GetConnection(String address, String port, String dbName, String user, String password) {
        
        this.address = address.trim();
        this.dbName = dbName.trim();
        this.port = port.trim();
        
        if (this.port != null && !this.port.isEmpty()) {
            this.fullAddress = this.address + ":" + this.port; 
        } else {
            this.fullAddress = this.address;
        }
        
        this.url = "jdbc:sqlserver://" + this.fullAddress + ";";
        
        if (!isConnectionOpen()) {
            this.user = user.trim();
            this.password = password.trim();
            
            try {
                Properties properties = new Properties();
                properties.put("databaseName", this.dbName);
                properties.put("user", this.user);
                properties.put("password", this.password);
                properties.put("encrypt", "true");
                properties.put("trustServerCertificate", "true");
                properties.put("integratedSecurity", "false");
                
                conn = DriverManager.getConnection(url, properties);
                return conn;
            }
            catch (Exception e) {
                ErrorHandlerMethod(null, e.getMessage());                
            }
            return null;
        }
        return conn;
    }
}