package book.ch5.domain.dao;

import book.ch5.UserService;
import book.ch5.UserServiceV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/tobyspring");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public UserDaoJdbcTemplate jdbcTemplate() {
        return new UserDaoJdbcTemplate(dataSource());
    }

    @Bean
    public UserService userService() {
        return new UserService(jdbcTemplate());
    }

    @Bean
    public UserServiceV2 userServiceV2() {
        return new UserServiceV2(jdbcTemplate(), dataSource());
    }
}
