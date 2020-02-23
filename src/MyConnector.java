import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnector {

    private Connection connection;
    private String url;

    public MyConnector(String dbName) throws SQLException, ClassNotFoundException {
        this.url = "jdbc:mysql://localhost:3306/" + dbName + "?user=root&password=&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return connection;
    }

}