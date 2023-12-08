
import java.sql.SQLException;

public class Customer {

    public static void main(String[] args) throws SQLException {
        Database database = new Database();
        CLI_CUSTOMER cli = new CLI_CUSTOMER(database);
        cli.run();
    }
}