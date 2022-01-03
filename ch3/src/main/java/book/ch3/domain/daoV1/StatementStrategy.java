package book.ch3.domain.daoV1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;

}
