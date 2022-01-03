package book.ch3.dao;

import book.ch3.domain.jdbctemplate.UserDaoJdbcTemplate;
import book.ch3.domain.dao.UserDao;
import book.ch3.domain.daoV3.JdbcContext;
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
    public JdbcContext context() {
        return new JdbcContext(dataSource());
    }

    @Bean
    public UserDao userDao() {
        return new UserDao(dataSource());
    }

    @Bean
    public UserDaoJdbcTemplate jdbcTemplate() {
        return new UserDaoJdbcTemplate(dataSource());
    }
}
