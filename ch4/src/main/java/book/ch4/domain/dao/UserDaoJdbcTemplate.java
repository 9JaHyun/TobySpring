package book.ch4.domain.dao;

import book.ch4.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 예외처리는 무조건 해야 한다.
// 예외는 체크예외, 언체크예외가 있는데 왠만하면 체크예외를 언체크예외로 변환시키자!!!
// 체크예외 : 언체크예외를 제외한 모든 예외, 반드시 예외 처리를 해야 한다. (컴파일 에러 발생)
// 언체크예외 : 런타임을 상속하는 예외, (프로그램에 오류가 있을 때 발생하도록 의도된 것들이기 때문)
public class UserDaoJdbcTemplate {
    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 만약 rowMapper의 중복이 많이 생긴다면, 이렇게 외부로 빼서 리팩토링을 실시하자.
    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            };

    public void add(User user) {
        jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?", userMapper, id);
    }

    // 오버로딩을 통해 리스트로도 꺼내올 수 있다.
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    public void deleteAll() {
        final int result = jdbcTemplate.update("delete from users");
        System.out.println(result + "건이 삭제되었습니다.");
    }


}
