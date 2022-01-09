package book.ch6.domain.dao;

import book.ch6.service.UserService;
import book.ch6.service.UserServiceImpl;
import book.ch6.service.UserServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

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

    // 프록시 설정
    @Bean
    public UserService userService() {
        return new UserServiceTx(userServiceImpl(), transactionManager());
    }

    @Bean
    public UserService userServiceImpl() {
        return new UserServiceImpl(userDao());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbcTemplate(dataSource());
    }
}
