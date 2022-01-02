package book.tobyspring.user.dao.strategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleStrategy implements ConnectionStrategy{
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "root", "1234");
    }
}
