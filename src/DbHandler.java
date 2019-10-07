import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHandler {

    private static String CON_STR = null;
    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance(String connectionString) throws SQLException {
        //CON_STR = connectionString;
        if (instance == null)
            instance = new DbHandler(connectionString);
        return instance;
    }

    private Connection connection;

    private DbHandler(String connectionString) throws SQLException {
        CON_STR = connectionString;
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + this.prepareYourAnus(tableName) + " (\n" //private String blogName
                + "	postId text NOT NULL UNIQUE,\n"
                + "	tags text NOT NULL,\n"
                + "	filenames text NOT NULL,\n"
                + " date TIMESTAMP NOT NULL);";

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void getDatabaseMetaData() {
        try {
            DatabaseMetaData dbmd = this.connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String prepareYourAnus(String stringToPrepareYourAnus) {
        stringToPrepareYourAnus = stringToPrepareYourAnus.replace(".", "");
        stringToPrepareYourAnus = stringToPrepareYourAnus.replace("explore/tags/", "");
        return stringToPrepareYourAnus;
    }

    public ArrayList<String> returnOveralLTaggsFreomTable(String table) {
        //String sql = "SELECT postId, tags, filenames, date FROM latex WHERE tags LIKE '%cats%';";
        String sql = "SELECT tags FROM " + table + ";";


        ArrayList<String> tags = new ArrayList<String>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

             while (rs.next()) {
                System.out.println(rs.getString("tags"));
                tags.add(rs.getString("tags").replace("#",""));
             }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tags;
    }

    public String getTagsByFilename(String filename, String table) {
        String sql = "SELECT tags FROM " + table + " WHERE filenames LIKE '%" + filename + "%';";
        String returnPlzThis = "";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                System.out.println(filename + " " + rs.getString("tags"));
                returnPlzThis = rs.getString("tags");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return returnPlzThis;
    }


}
