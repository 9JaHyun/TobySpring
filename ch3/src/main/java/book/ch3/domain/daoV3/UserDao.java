package book.ch3.domain.daoV3;

import book.ch3.domain.User;
import book.ch3.domain.daoV1.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// JdbcContext에서 다 처리를 해주기 때문에, 더이상 UserDao에서는 DataSource를 알 필요가 없다.
public class UserDao {
    private final JdbcContext jdbcContext;

    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(User user) {
        jdbcContext.workWithStatementStrategyV1((c) -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
    }

    public void deleteAll() {
        jdbcContext.workWithStatementStrategyV1(c -> {
            return c.prepareStatement("Delete from users");
        });
    }
}
