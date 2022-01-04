package book.ch5;

import book.ch5.domain.Level;
import book.ch5.domain.User;
import book.ch5.domain.dao.DaoFactory;
import book.ch5.domain.dao.UserDao;
import book.ch5.domain.dao.UserDaoJdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = DaoFactory.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    private List<User> userList = new ArrayList<>();

    @BeforeAll
    void init() {
        User user1 = new User("test1", "testName1", "testPassword1", Level.BASIC, 1, 0);
        User user2 = new User("test2", "testName2", "testPassword2", Level.SILVER, 55, 10);
        User user3 = new User("test3", "testName3", "testPassword3", Level.GOLD, 100, 40);

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @Test
    void upgradeLevels() {
        for (User user : userList) {
            userDao.add(user);
        }
        userService.upgradeLevels();
        checkLevel(userList.get(0), Level.BASIC);
        checkLevel(userList.get(1), Level.SILVER);
        checkLevel(userList.get(2), Level.GOLD);

    }

    private void checkLevel(User user, Level expectedLevel) {
        User updateUser = userDao.get(user.getId());
        assertEquals(updateUser.getLevel(), expectedLevel);

    }
}