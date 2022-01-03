package book.ch3.domain.dao;

import book.ch3.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 예외가 발생하면 자원반납을 할 방법이 없다.
// 이 경우에는  try-finally 또는 try-with-resource를 사용하자.
// 문제점: 코드가 너무 장황하다.
// 중복되는 부분들을 리팩토링하자.
public class UserDaoV2 {
    private final DataSource dataSource;

    public UserDaoV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // try-finally 방식으로 사용
    public void add(User user) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
            ps.setString(1, user.getId());

            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null){
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

    public User get(String id){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = new User();

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            rs = ps.executeQuery();

            rs.next();

            user.setId(rs.getString(1));
            user.setName(rs.getString(2));
            user.setPassword(rs.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try{
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
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
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();

            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null){
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
}
