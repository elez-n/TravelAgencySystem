package db;
import java.sql.Connection;


/**
 * Klasa testConnection sluzi za testiranje konekcije ka bazi podataka.
 * 
 * Ova klasa koristi metodu {@link DbManipulation#createConnection()} za kreiranje konkecije
 * na osnovu konfiguracije iz XML fajla.
 * Nakon uspostavljanja konekcije provjerava se da li je 
 * konekcija validna u zadatom vremenskom intervalu, a zatim se konekcija zatvara.
 * 
 * Korisna je za dijagnostiku i provjeru ispravnosti parametara konekcije.
 */
public class testConnection {
	
	
	/**
	 * Glavna metoda koja se izvrsava prilikom pokretanja programa.
	 * 
	 * Kreira konekciju ka bazi, provjera da li je konekcija uspjesno uspostavljena
	 * i da li je validna, te zatvara konekciju nakon testiranja.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
        DbManipulation dbManipulation = DbManipulation.createConnection();

        if (dbManipulation != null) {
            Connection conn = dbManipulation.getConnection();

            if (conn != null) {
                System.out.println("Konekcija uspješno uspostavljena!");

                try {
                    if (conn.isValid(5)) {
                        System.out.println("Konekcija je validna.");
                    } else {
                        System.out.println("Konekcija nije validna.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dbManipulation.closeConnection();
                }
            } else {
                System.out.println("Konekcija nije uspostavljena (conn je null).");
            }
        } else {
            System.out.println("Neuspješno kreiranje konekcije.");
        }
    }

}
