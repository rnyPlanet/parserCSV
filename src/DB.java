import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DB {
    private String dbName;
    private String tableName = "";

    private Connection con;
    private MyConnector connector;

    private List<List<String>> rowsFromFile;

    public DB(String dbName, List<List<String>> rowsFromFile) {
        this.dbName = dbName;
        this.rowsFromFile = rowsFromFile;

        try {
            connector = new MyConnector(dbName);
        } catch (SQLException | ClassNotFoundException e) {
            createDatabase();
            System.out.println(e.getMessage());

            try {
                connector = new MyConnector(dbName);
            } catch (SQLException | ClassNotFoundException ignored) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void createDatabase() {
        try {
            Statement statement = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=&serverTimezone=UTC").createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    final int[] maxLength = {0};

    private void maxStringLe() {
        rowsFromFile
                .forEach(i -> i
                        .forEach(j -> {
                            if (j.length() > maxLength[0]) {
                                maxLength[0] = j.length();
                            }
                        }));
    }

    public void createTable() {
        try {
            Statement statement = connector.getConnection().createStatement();

            this.tableName = dbName.substring(0, dbName.length() / 2);
            StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + " (");

            rowsFromFile.get(0).forEach(x ->
                    sql.append("`").append(x).append("`")
                            .append(" VARCHAR(").append(maxLength[0]).append("), ")
            );

            sql.replace(sql.length() - 2, sql.length() - 1, "");        // delete last comma
            sql.append(")");

            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void writeRows() {
        try {
            Statement statement = connector.getConnection().createStatement();

            StringBuffer sql = new StringBuffer();

            rowsFromFile.stream().skip(1)           // skip headers
                    .forEach(i -> {
                        sql.append("INSERT INTO " + dbName + "." + tableName + " VALUES  (");

                        i.forEach(j -> sql.append("\"").append(j).append("\"").append(", "));

                        sql.replace(sql.length() - 2, sql.length() - 1, "");
                        sql.append("); ");

                        try {
                            statement.executeUpdate(sql.toString());
                        } catch (SQLException e) {
                            // System.out.println(i.get(0));
                        }

                        sql.delete(0, sql.length());
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeRowsFromFileToDb() {
        maxStringLe();
        createTable();
        writeRows();
    }


}
