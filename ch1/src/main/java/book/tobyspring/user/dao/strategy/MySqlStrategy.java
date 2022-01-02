package book.tobyspring.user.dao.strategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlStrategy implements ConnectionStrategy{
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/tobyspring", "root", "1234");
    }

}
