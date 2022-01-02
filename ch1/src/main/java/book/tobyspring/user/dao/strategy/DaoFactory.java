package book.tobyspring.user.dao.strategy;

import book.tobyspring.user.dao.UserDaoByStrategy;
import book.tobyspring.user.dao.UserDaoFinal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

// 이렇게 일일히 정해주는 것도 귀찮다.
// 그냥 스프링에게 이 작업을 넘기는게 더 좋지 않을까??
// XML 설정정보와 같은 방식임.
@Configuration
public class DaoFactory {

    @Bean
    public UserDaoByStrategy userDaoByMySql() {
        return new UserDaoByStrategy(mysqlConnectionStrategy());
    }

    @Bean
    public UserDaoByStrategy userDaoByOracle() {
        return new UserDaoByStrategy(oracleConnectionStrategy());
    }

    @Bean
    public ConnectionStrategy mysqlConnectionStrategy() {
        return new MySqlStrategy();
    }

    @Bean
    public ConnectionStrategy oracleConnectionStrategy() {
        return new MySqlStrategy();
    }

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
    public UserDaoFinal userDao() {
        return new UserDaoFinal(dataSource());
    }
}
