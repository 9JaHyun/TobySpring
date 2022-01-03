package book.ch3.domain.daoV2;

import book.ch3.domain.User;
import book.ch3.domain.daoV1.AddStatement;
import book.ch3.domain.daoV1.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 이 방법이 좋긴한데....
// StatementStrategy 구현클래스가 너무 많이 생기는 단점이 있다!
// 이런 경우에는 어떻게 하지? => 로컬클래스!
public class UserDaoV1 {
    private final DataSource dataSource;

    public UserDaoV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 가장 편한 방식은 아무래도 람다를 이용한 콜백 함수 만들기다. (익명 클래스)
    public void add(User user) {
        jdbcContextWithStatementStrategy((c) -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
    }

    // 아니면 이렇게 직접 로컬 클래스를 구현해도 된다.
    public void deleteAll() {
        class DeleteAllStatement implements StatementStrategy {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        }

        DeleteAllStatement deleteAllStatement = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(deleteAllStatement);
    }

    // 공통적으로 모두 사용이 가능한데, 각 클래스에 구현하는 것은 매우 비효율적. => 분리를 하자!
    private void jdbcContextWithStatementStrategy(StatementStrategy stmt) {
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
                try{
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
