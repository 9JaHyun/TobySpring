package book.ch3.domain.dao;

import book.ch3.dao.DaoFactory;
import book.ch3.domain.User;
import book.ch3.domain.dao.UserDao;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// getBean 형식으로 가지고 오지 않을 경우 이 방법을 통해 주입을 시도하자
@ContextConfiguration(classes = DaoFactory.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @BeforeAll()
    void setUp() {
        // 첫번째 방법으로는 컨텍스트를 강제로 호출해서 빈을 사용해도 된다.
        // 이 방법이 아니면, 경우 @ContextConfiguration에 등록하자
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        this.userDao = context.getBean("userDao", UserDao.class);
    }

    @BeforeEach
    void init() throws SQLException {
        System.out.println(userDao);
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
