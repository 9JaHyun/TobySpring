package book.ch5;

import book.ch5.domain.Level;
import book.ch5.domain.User;
import book.ch5.domain.dao.DaoFactory;
import book.ch5.domain.dao.UserDaoJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        User user = new User("test1", "testName1", "testPassword1", Level.BASIC, 1, 0);

        userDaoJdbcTemplate.add(user);

        User findUser = userDaoJdbcTemplate.get("test1");
        assertEquals(user.getId(), findUser.getId());
        assertEquals(user.getName(), findUser.getName());
        assertEquals(user.getPassword(), findUser.getPassword());
        assertEquals(user.getLevel(), findUser.getLevel());
        assertEquals(user.getLogin(), findUser.getLogin());
        assertEquals(user.getRecommend(), findUser.getRecommend());
    }

    @DisplayName(value = "전체 조회 테스트")
    @Test
    void getAll(){
        User user1 = new User("test1", "testName1", "testPassword1", Level.BASIC, 1, 0);
        User user2 = new User("test2", "testName2", "testPassword2", Level.SILVER, 55, 10);
        User user3 = new User("test3", "testName3", "testPassword3", Level.GOLD, 100, 40);

        userDaoJdbcTemplate.add(user1);
        userDaoJdbcTemplate.add(user2);
        userDaoJdbcTemplate.add(user3);

        List<User> all = userDaoJdbcTemplate.getAll();
        all.iterator().forEachRemaining(System.out::println);
    }

    @DisplayName(value = "jdbc 삭제 테스트")
    @Test
    void deleteAll(){
        User user = new User("test1", "testName1", "testPassword1", Level.BASIC, 1, 0);

        userDaoJdbcTemplate.add(user);
        assertSame(1, userDaoJdbcTemplate.count());

        userDaoJdbcTemplate.deleteAll();
        assertSame(0, userDaoJdbcTemplate.count());
    }
}