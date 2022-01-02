package book.tobyspring.user.dao;

import book.tobyspring.user.dao.strategy.DaoFactory;
import book.tobyspring.user.dao.strategy.MySqlStrategy;
import book.tobyspring.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        badUserDaoTest();
//        compositionUserDaoTest();
//        daoFactoryTest();
//        daoTestByApplicationContext();

    }

    private static void badUserDaoTest() throws ClassNotFoundException, SQLException {
        UserDaoBadCase userDao = new UserDaoBadCase();

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

//        userDao.add(user);
        System.out.println(user.getId() + "성공");

        User findUser = userDao.get("whiteship");
        System.out.println(findUser.getId());
        System.out.println(findUser.getName());
        System.out.println(findUser.getPassword());
    }

    private static void compositionUserDaoTest() throws SQLException, ClassNotFoundException {
        // 클라이언트에서 동적 바인딩을 하는 방식을 의존주입 역전이(DI)라고 한다
        // 구현 코드들이 결합력이 줄어들어서 유연성을 확보할 수 있다. (개방 폐쇠 원칙)
        UserDaoByStrategy userDao = new UserDaoByStrategy(new MySqlStrategy());

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

//        userDao.add(user);
        System.out.println(user.getId() + "성공");

        User findUser = userDao.get("whiteship");
        System.out.println(findUser.getId());
        System.out.println(findUser.getName());
        System.out.println(findUser.getPassword());
    }

    // 테스트에서 UserDao를 생성할 책임은 없기 때문에 DaoFactory로 분리
    private static void daoFactoryTest() throws SQLException, ClassNotFoundException {
        DaoFactory daoFactory = new DaoFactory();
        // 클라이언트는 내부 구현을 알 필요 없이 찾아서 사용만 하면 된다!

        UserDaoByStrategy userDaoByMysql = daoFactory.userDaoByMySql();
        UserDaoByStrategy userDaoByOracle = daoFactory.userDaoByOracle();

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

//        userDao.add(user);
        System.out.println(user.getId() + "성공");

        User findUser = userDaoByMysql.get("whiteship");
        System.out.println(findUser.getId());
        System.out.println(findUser.getName());
        System.out.println(findUser.getPassword());
    }

    private static void daoTestByApplicationContext() throws SQLException, ClassNotFoundException {
        // Bean을 생성하는 ApplicationContext
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDaoByStrategy userDaoByMySql = context.getBean("userDaoByMySql", UserDaoByStrategy.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

//        userDao.add(user);
        System.out.println(user.getId() + "성공");

        User findUser = userDaoByMySql.get("whiteship");
        System.out.println(findUser.getId());
        System.out.println(findUser.getName());
        System.out.println(findUser.getPassword());

    }
}
