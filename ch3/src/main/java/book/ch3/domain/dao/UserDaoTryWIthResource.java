package book.ch3.domain.dao;

import book.ch3.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoTryWIthResource {
    private final DataSource dataSource;

    public UserDaoTryWIthResource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // try-with-resource 방식으로 사용
    public void add(User user) {
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        User user = new User();
        try(Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("select * from users where id = ?");) {
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            user.setId(rs.getString(1));
            user.setName(rs.getString(2));
            user.setPassword(rs.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");

        int result = ps.executeUpdate();
        System.out.println(result + "건 삭제 완료.");

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select count(*) from users");
        ResultSet rs = ps.executeQuery();

        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }
}
