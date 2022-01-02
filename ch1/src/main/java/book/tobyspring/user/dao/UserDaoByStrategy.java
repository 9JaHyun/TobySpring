package book.tobyspring.user.dao;

import book.tobyspring.user.dao.strategy.ConnectionStrategy;
import book.tobyspring.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoByStrategy {
    private final ConnectionStrategy connectionStrategy;

    // 합성 방식으로 인해 동적 바인딩이 가능 (DI)
    public UserDaoByStrategy(ConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionStrategy.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionStrategy.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();
        user.setId(rs.getString(1));
        user.setName(rs.getString(2));
        user.setPassword(rs.getString(3));

        rs.close();
        ps.close();
        c.close();

        return user;
    }
}
