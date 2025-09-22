package model;

import java.util.Vector;

/**
 * Klasa TreeElement je apstraktna klasa koja predstavlja element u hijerarhijskoj strukturi stabla.
 * 
 * Klasa omogucava dodavanje, pristup i upravljanje potklasama koje mogu biti
 * paketi, tabele ili kolone u modelu baze podataka.
 * @author G4
 */
public abstract class TreeElement {
	public String code = null;
	public String name = null;

	private Vector<TreeElement> treeElements = new Vector<TreeElement>();

	@Override
	public String toString() {
		return this.name;
	}
	

	/**
	 * Metoda koja vraca indeks prosljedjenog elementa unutar liste pod-elemenata.
	 * 
	 * @param element element ciji se indeks trazi
	 * @return indeks elementa ili -1 ako element ne postoji u listi
	 */
	public int getIndexOfElement(TreeElement element) {
		return treeElements.indexOf(element);
	}

	
	/**
	 * Metoda koja dodaje novi element u listu pod-elemenata.
	 * @param element novi element koji se dodaje
	 */
	public void addElement(TreeElement element) {
		treeElements.add(element);
	}

	
	/**
	 * Vraca sve pod-elemente trenutnog elementa.
	 * @return lista pod-elemenata
	 */
	public Vector<TreeElement> getAllElements() {
		return treeElements;
	}

	
	/**
	 * Dohvata element na odredjenom indeksu.
	 * 
	 * @param index indeks elementa
	 * @return element na trazenoj poziciji
	 */
	public TreeElement getElementAt(int index) {
		return treeElements.get(index);
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Klasa koja predstavlja paket u modelu stabla.
	 *
	 */
	public static class Package extends TreeElement {

		public Package() {
		}
		

		/**
		 * Konstruktor koji inicijalizuje paket sa zadatim imenom.
		 * 
		 * @param name
		 */
		public Package(String name) {
			this.name = name;
		}
	}
	
	
	
	  	
	/**
	 * Klasa koja predstavlja tabelu u modelu stabla.
	 * 
	 * Tabela moze da sadrzi kolone i reference na druge tabele,
	 * kao i informacije o procedurama za manipulaciju podacima.
	 */
public static class Table extends TreeElement{
		
		private Vector<Table> refrences = new Vector<TreeElement.Table>();
		
		private String createSProc = null;
		private String retrieveSProc = null;
		private String updateSProc = null;
		private String deleteSProc = null;
		protected String admin="false";
		protected String menadzer="false";
		protected String operater="false";
		protected String upravnik="false";
		
		
		public String getAdmin() {
			return this.admin;
		}		
		
		public void setAdmin(String admin) {
			this.admin = admin;
		}
		
		public Boolean isAdmin()
		{
			if(getAdmin().equals("true"))
				return true;
			return false;
		}
		
		public String getMenadzer() {
			return this.menadzer;
		}		
		
		public void setMenadzer(String menadzer) {
			this.menadzer = menadzer;
		}
		
		public Boolean isMenadzer()
		{
			if(getMenadzer().equals("true"))
				return true;
			return false;
		}
		
		public String getOperater() {
			return this.operater;
		}		
		
		public void setOperater(String operater) {
			this.operater = operater;
		}
		
		public Boolean isOperater()
		{
			if(getOperater().equals("true"))
				return true;
			return false;
		}
		
		public String getUpravnik() {
			return this.upravnik;
		}		
		
		public void setUpravnik(String upravnik) {
			this.upravnik = upravnik;
		}
		
		public Boolean isUpravnik()
		{
			if(getUpravnik().equals("true"))
				return true;
			return false;
		}
		
		public String getCreateSProc() {
			return createSProc;
		}
		public void setCreateSProc(String createSProc) {
			this.createSProc = createSProc;
		}
		public String getRetrieveSProc() {
			return retrieveSProc;
		}
		public void setRetrieveSProc(String retrieveSProc) {
			this.retrieveSProc = retrieveSProc;
		}
		public String getUpdateSProc() {
			return updateSProc;
		}
		public void setUpdateSProc(String updateSProc) {
			this.updateSProc = updateSProc;
		}
		public String getDeleteSProc() {
			return deleteSProc;
		}
		public void setDeleteSProc(String deleteSProc) {
			this.deleteSProc = deleteSProc;
		}
				
		public void addReference(Table reference) {
			refrences.add(reference);
		}
		
		public Vector<Table> getAllRefernces(){
			return refrences;
		}	
		
		public java.util.List<Column> getAllColumns() {
	        java.util.List<Column> columns = new java.util.ArrayList<>();
	        for (TreeElement element : getAllElements()) {
	            if (element instanceof Column) {
	                columns.add((Column) element);
	            }
	        }
	        return columns;
	    }
			
	}


/**
 * Klasa koja predstavlja kolonu u tabeli.
 * 
 * Kolona moze biti primarni ili strani kljuc, moze biti NULL ili AUTO_INCREMENT,
 * i poseduje tip, velicinu i referencu ka drugoj tabeli i koloni.
 */
	public static class Column extends TreeElement{
		private Boolean primary = null;
		private Boolean foreign = null;
		private Boolean nullable = null;
		private Boolean autoIncrement = null;
		private String refColumn;
		private String refTable;
		private String type;
		private int size;
		private int scale;
		private String dbName;
		
		public Column() {
			
		}
		
		public Column(String cName) {
		    this.name = cName;   
		}

		public String getRefColumn() {
			return refColumn;
		}

		public void setRefColumn(String refColumn) {
			this.refColumn = refColumn;
		}

		public String getRefTable() {
			return refTable;
		}

		public void setRefTable(String refTable) {
			this.refTable = refTable;
		}

		public Boolean isPrimary() {
			return primary;
		}

		public void setPrimary(Boolean primary) {
			this.primary = primary;
		}

		public Boolean isNullable() {
			return nullable;
		}

		public void setNullable(Boolean nullable) {
			this.nullable = nullable;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
		}

		public Boolean isForeign() {
			return foreign;
		}
		public void setForeign(Boolean foreign) {
			this.foreign=foreign;
		}
		
		public void setAutoIncrement(Boolean autoIncrement) {
			this.autoIncrement = autoIncrement;
		}

		public Boolean getAutoIncrement() {
			return autoIncrement;
		}
		
		public boolean isAutoIncrement() {
			return autoIncrement;
		}
		
		public String getDbName() {
			return dbName;
		}
		
		public void setDbName(String name)
		{
			this.dbName = name;
		}
	}
}