package book.ch5.domain.dao;

import book.ch5.domain.Level;
import book.ch5.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbcTemplate implements UserDao{
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
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("Login"));
                    user.setRecommend(rs.getInt("Recommend"));
                    return user;
                }
            };

    @Override
    public void add(User user) {
        jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?", userMapper, id);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("update users set id = ?, name = ?, password = ?, level = ?, login = ?, recommend = ?",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    // 오버로딩을 통해 리스트로도 꺼내올 수 있다.
    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    public void deleteAll() {
        final int result = jdbcTemplate.update("delete from users");
        System.out.println(result + "건이 삭제되었습니다.");
    }


}
