package book.tobyspring.user.dao;

import book.tobyspring.user.domain.User;

import java.sql.*;

//중복 코드가 너무 많다.
// 유연성도 제로에 가까움.
// 유연성을 늘려주기 위한 방법
// 1st. 관심사의 분리
public class UserDaoV1 {
    // UserDao의 관심사는 총 2가지
    // 1. 커넥션 생성
    // 2. 쿼리
    // 이들을 분리하라!
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
    
    // 커넥션 생성만 담당하는 메서드로 분리 성공
    // 여기서 의문점. 만약 mysql이 아닌 다른 db와의 커넥션을 생성하려면 일일히 메서드들을 모두 추가해야 하나??
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/tobyspring", "root", "1234");
    }
}
