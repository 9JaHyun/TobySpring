package book.tobyspring.user.dao;

import book.tobyspring.user.domain.User;

import java.sql.*;

// UserDaoV1에서의 의문점은, 여러 DB에 대한 대응력을 향상시키기 위해 유연성을 높일 필요성이 생김
// 첫번째 방법은 상속을 통한 확장(별로 권장하는 방법은 아님)
public abstract class UserDaoV2 {
    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();     // 커넥션 분리 성공

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

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
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}

// 추상클래스로 만들어서 이를 상속해 오버라이딩 전략을 사용하면 유연성있는 설계가 가능하다.
// 문제점 발생!
//      상속은 단일 상속만 가능! (만약 다른 이유로 인해 상속을 사용하고 있다면???)
class MySqlUserDao extends UserDaoV2 {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/tobyspring", "root", "1234");
    }
}


class OracleUserDal extends UserDaoV2 {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "root", "1234");
    }
}