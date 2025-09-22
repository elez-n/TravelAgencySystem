package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controller.LoginModel;

/**
 * Utility klasa za rad sa skladištenim proceduarama (Stored Procedures)
 * u bazi podataka koristeći JDBC.
 * Generički izvršavač skladištenih procedura nad trenutnom konekcijom.
 * Za SQL Server koristi EXEC sintaksu umesto JDBC { call ... } da bi se izbegla greška "Incorrect syntax near '{'".
 * 
 * Klasa obezbjeđuje metode za:
 * Izvršavanje procedura koje vrše INSERT/UPDATE/DELETE operacije
 * Izvršavanje procedura koje vraćaju podatke (ResultSet)
 * Čitanje svih rezultata u obliku liste.
 * 
 * @author G4
 */
public class StoredProcedures {

    private final DbManipulation dbManipulation;

    public StoredProcedures(DbManipulation dbManipulation) {
        this.dbManipulation = dbManipulation;
    }

    public Connection getConnection() throws SQLException {
        Connection c = dbManipulation != null ? dbManipulation.getConnection() : null;
        if (c == null) throw new SQLException("No DB connection");
        return c;
    }
    
    
    /**
     * Sanitizuje ime procedure tako što uklanja nepotrebne dijelove:
     * tačka-zarez na kraju
     * sve poslije zagrade '('
     * ključne riječi CALL/EXEC/EXECUTE
     * 
     * @param procedureName ime procedure
     * @return čisto ime procedure
     */
    private static String sanitizeProcedureName(String procedureName) {
        if (procedureName == null) return "";
        String trimmed = procedureName.trim();
        if (trimmed.endsWith(";")) trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
        int paren = trimmed.indexOf('(');
        if (paren >= 0) {
            trimmed = trimmed.substring(0, paren).trim();
        }
        String lower = trimmed.toLowerCase();
        if (lower.startsWith("call ")) {
            trimmed = trimmed.substring(5).trim();
        } else if (lower.startsWith("exec ")) {
            trimmed = trimmed.substring(5).trim();
        } else if (lower.startsWith("execute ")) {
            trimmed = trimmed.substring(8).trim();
        }
        return trimmed;
    }

    
    /**
     * Gradi SQL string u EXEC formatu za izvršavanje procedure.
     * 
     * @param procedureName ime procedure
     * @param paramCount broj parametara koje procedura prima
     * @return SQL string u formatu: {@code EXEC procedure ?, ?, ...}
     */
    private static String buildExecSyntax(String procedureName, int paramCount) {
        String proc = sanitizeProcedureName(procedureName);
        StringBuilder builder = new StringBuilder();
        builder.append("EXEC ").append(proc);
        if (paramCount > 0) {
            builder.append(" ");
            for (int i = 0; i < paramCount; i++) {
                builder.append("?");
                if (i < paramCount - 1) builder.append(", ");
            }
        }
        return builder.toString();
    }

    
    /**
     * Izvršava skladištenu proceduru koja vrši izmjene u bazi (INSERT, UPDATE, DELETE).
     * 
     * @param procedureName ime procedure
     * @param params lista parametara koji se prosljeđuju proceduri
     * @return broj redova koje je procedura izmijenila
     * @throws SQLException ako dođe do greške pri izvršavanju
     */
    public int executeUpdate(String procedureName, List<Object> params) throws SQLException {
        String sql = buildExecSyntax(procedureName, params != null ? params.size() : 0);
        System.out.println("EXECUTE UPDATE: " + sql + " | params=" + (params == null ? 0 : params.size()));
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }
            return statement.executeUpdate();
        }
    }
    
    
    /**
     * Izvršava update koristeći HashMap parametara.
     * Ne koristi prosljeđene kolone, samo HashMap.
     */
    public boolean update(String procedure, HashMap<String, Object> params) {
        if (procedure == null || procedure.isEmpty()) return false;

        int paramCount = params != null ? params.size() : 0;
        String sql = buildExecSyntax(procedure, paramCount);

        //System.out.println("UPDATE with HashMap: " + sql);
        //System.out.println("Params: " + params);

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            if (params != null) {
                int index = 1;
                for (String key : params.keySet()) {
                    statement.setObject(index++, params.get(key));
                }
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Izvršava skladištenu proceduru koja vraća podatke.
     * 
     * @param procedureName ime procedure
     * @param params lista parametara
     * @return {@link ResultSet} sa rezultatima
     * @throws SQLException ako dođe do greške pri izvršavanju
     */
    public java.sql.ResultSet executeRetrieve(String procedureName, List<Object> params) throws SQLException {
        if ("Retrieve_VoziloUPoslovnici".equalsIgnoreCase(procedureName)) {
            if (params == null) {
                params = new ArrayList<>();
            }
            params.add(LoginModel.userId);  
        }
        String sql = buildExecSyntax(procedureName, params != null ? params.size() : 0);
        //System.out.println("EXECUTE RETRIEVE: " + sql + " | params=" + (params == null ? 0 : params.size()));
        PreparedStatement statement = getConnection().prepareStatement(sql);
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
        }
        return statement.executeQuery();
    }

    
    /**
     * Cita sve rezultate koje vrati skladistena procedura
     * 
     * @param procedureName ime procedure
     * @param params lista parametara koje procedura prima
     * @param columnOrder redoslijed kolona koje treba da se citaju,
     * ako je {@code null} ili prazan, cita se po indeksu
     * @return lista redova gdje je svaki red lista objekata (kolona)
     * @throws SQLException ako dodje do greske pri izvrsavanju ili citanju
     */
    public List<List<Object>> readAll(String procedureName, List<Object> params, List<String> columnOrder) throws SQLException {
        String sql = buildExecSyntax(procedureName, params != null ? params.size() : 0);
       // System.out.println("EXECUTE QUERY: " + sql + " | params=" + (params == null ? 0 : params.size()));
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            boolean hasResultSet = statement.execute();
            ResultSet rs = null;
            while (true) {
                if (hasResultSet) {
                    rs = statement.getResultSet();
                    break;
                }
                int updateCount = statement.getUpdateCount();
                if (updateCount == -1) {
                    break;
                }
                hasResultSet = statement.getMoreResults();
            }

            List<List<Object>> rows = new ArrayList<>();
            if (rs != null) {
                try (ResultSet result = rs) {
                    while (result.next()) {
                        List<Object> row = new ArrayList<>();
                        if (columnOrder != null && !columnOrder.isEmpty()) {
                            for (String col : columnOrder) {
                                try {
                                    Object value = result.getObject(col);
                                   // System.out.println("Column " + col + " = " + value);
                                    row.add(value);
                                } catch (SQLException ex) {
                                    int idx;
                                    try {
                                        idx = result.findColumn(col);
                                        Object value = result.getObject(idx);
                                        //System.out.println("Column " + col + " (by index " + idx + ") = " + value);
                                        row.add(value);
                                    } catch (SQLException e2) {
                                        //System.out.println("Column " + col + " not found, setting null");
                                        row.add(null);
                                    }
                                }
                            }
                        } else {
                            int count = result.getMetaData().getColumnCount();
                            //System.out.println("Reading row with " + count + " columns by index");
                            for (int i = 1; i <= count; i++) {
                                Object value = result.getObject(i);
                                //System.out.println("Column " + i + " = " + value);
                                row.add(value);
                            }
                        }
                        rows.add(row);
                        //System.out.println("Row added: " + row);
                    }
                }
                //System.out.println("Total rows read: " + rows.size());
            } else {
                //System.out.println("No ResultSet found!");
            }
            return rows;
        }
    }
}


