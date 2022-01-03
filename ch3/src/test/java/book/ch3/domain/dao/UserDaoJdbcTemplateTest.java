package book.ch3.domain.dao;

import book.ch3.dao.DaoFactory;
import book.ch3.domain.User;
import book.ch3.domain.jdbctemplate.UserDaoJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = DaoFactory.class)
class UserDaoJdbcTemplateTest {
    @Autowired
    private UserDaoJdbcTemplate userDaoJdbcTemplate;

    @BeforeEach
    void init() {
        userDaoJdbcTemplate.deleteAll();
    }

    @DisplayName(value = "저장 및 조회 테스트")
    @Test
    void addAndGet() {
        User user = new User();
        user.setId("test1");
        user.setName("testName1");
        user.setPassword("testPassword1");

        userDaoJdbcTemplate.add(user);

        User findUser = userDaoJdbcTemplate.get("test1");
        assertEquals(user.getId(), findUser.getId());
        assertEquals(user.getName(), findUser.getName());
        assertEquals(user.getPassword(), findUser.getPassword());
    }

    @DisplayName(value = "전체 조회 테스트")
    @Test
    void getAll(){
        User user = new User();
        user.setId("test1");
        user.setName("testName1");
        user.setPassword("testPassword1");

        User user1 = new User();
        user1.setId("test2");
        user1.setName("testName2");
        user1.setPassword("testPassword2");

        User user2 = new User();
        user2.setId("test3");
        user2.setName("testName3");
        user2.setPassword("testPassword3");

        userDaoJdbcTemplate.add(user);
        userDaoJdbcTemplate.add(user1);
        userDaoJdbcTemplate.add(user2);

        List<User> all = userDaoJdbcTemplate.getAll();
        all.iterator().forEachRemaining(System.out::println);
    }

    @DisplayName(value = "jdbc 삭제 테스트")
    @Test
    void deleteAll(){
        User user = new User();
        user.setId("test1");
        user.setName("testName1");
        user.setPassword("testPassword1");

        userDaoJdbcTemplate.add(user);
        assertSame(1, userDaoJdbcTemplate.count());

        userDaoJdbcTemplate.deleteAll();
        assertSame(0, userDaoJdbcTemplate.count());

    }
}