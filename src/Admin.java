import java.sql.SQLException;
public class Admin {
 public static void main(String[] args) throws SQLException {
  Database database = new Database();
  CLI_ADMIN cli = new CLI_ADMIN(database);
  cli.run();
 }
}
