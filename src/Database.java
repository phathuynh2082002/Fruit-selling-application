import java.sql.*;

public class Database {
    public Connection connection;
    public Database() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/ql_ban_hang",
                "root",
                "0358003858"
        );
    }
}
