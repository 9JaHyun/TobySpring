package book.ch3.domain.daoV1;

import book.ch3.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final DataSource dataSource;
    private StatementStrategy st = new DeleteAllStatement();
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 딱히 유연성이 커보이지 않음.
    // 가장 큰 문제점은 정적으로 바인딩 되어 있는 것.
    // 동적 바인딩으로 바꾸어보자!
    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = st.makePreparedStatement(c);

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
