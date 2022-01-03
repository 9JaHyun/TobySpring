package book.ch3.domain.jdbctemplate;

import book.ch3.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        // 직접 내부 구현을 한 케이스
        jdbcTemplate.update(c -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
        // 내장 콜백을 사용하는 케이스
//        jdbcTemplate.update("insert into users(id, name, password) values (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    // 옛날에는 queryForInt()를 많이 사용했으나 3.2.2 버전이 넘어가면서 deprecated 되었다. 대신 queryForObject(sql, type) 를 사용한다.
    // 만약 파라미터를 넣어야 하는 경우에는 queryForObject(sql, rowMapper, element을 사용한다.). 대신 queryForObject(sql, type) 를 사용한다.
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?",
                (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }, id);

        /*        return jdbcTemplate.queryForObject("select * from users where id = ?", userMapper, id);*/
    }

    // 오버로딩을 통해 리스트로도 꺼내올 수 있다.
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    public void deleteAll() {
        // 직접 내부 구현을 한 케이스
        final int result = jdbcTemplate.update(c -> c.prepareStatement("delete from users"));

        // 내장 콜백을 사용하는 케이스 (결과는 같다)
//        final int result = jdbcTemplate.update("delete from users");

        System.out.println(result + "건이 삭제되었습니다.");
    }


}
