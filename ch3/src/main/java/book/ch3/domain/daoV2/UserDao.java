package book.ch3.domain.daoV2;

import book.ch3.domain.User;
import book.ch3.domain.daoV1.AddStatement;
import book.ch3.domain.daoV1.DeleteAllStatement;
import book.ch3.domain.daoV1.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 차라리 이렇게 템플릿을 만들자!
    private void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void add(User user) throws SQLException {
        AddStatement addStatement = new AddStatement(user);
        jdbcContextWithStatementStrategy(addStatement);
    }

    public void deleteAll() throws SQLException {
        DeleteAllStatement deleteAllStatement = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(deleteAllStatement);
    }
}
