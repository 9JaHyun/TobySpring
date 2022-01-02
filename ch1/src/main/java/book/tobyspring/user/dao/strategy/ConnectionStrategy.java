package book.tobyspring.user.dao.strategy;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionStrategy {
    public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
