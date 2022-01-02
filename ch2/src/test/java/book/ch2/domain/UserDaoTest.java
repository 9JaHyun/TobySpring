package book.ch2.domain;

import book.ch2.dao.DaoFactory;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {
    private UserDao userDao;

    @BeforeAll()
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.userDao = context.getBean("userDao", UserDao.class);
    }

    @BeforeEach
    void init() throws SQLException {
        userDao.deleteAll();
    }

    @DisplayName(value = "회원 저장 및 조회테스트")
    @Test
    void userAddGetTest() throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setId("TestId1");
        user.setName("TestName1");
        user.setPassword("TestPassword1");

        userDao.add(user);

        User findUser = userDao.get("TestId1");

        assertEquals(user.getId(), findUser.getId());
        assertEquals(user.getName(), findUser.getName());
        assertEquals(user.getPassword(), findUser.getPassword());

        assertThrows(SQLException.class, () -> userDao.get("TestId2"));
    }

    @DisplayName(value = "전체 삭제 테스트")
    @Test
    void deleteAllTest() throws SQLException {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());
    }

    @DisplayName(value = "회원 수 조회 테스트")
    @Test
    void countTest() throws SQLException {
        User user = new User();
        user.setId("TestId1");
        user.setName("TestName1");
        user.setPassword("TestPassword1");

        User user2 = new User();
        user2.setId("TestId2");
        user2.setName("TestName2");
        user2.setPassword("TestPassword2");

        User user3 = new User();
        user3.setId("TestId3");
        user3.setName("TestName3");
        user3.setPassword("TestPassword3");

        userDao.add(user);
        userDao.add(user2);
        userDao.add(user3);
        int count = userDao.getCount();
        assertEquals(3, count);
    }

}
