package book.ch5;

import book.ch5.domain.Level;
import book.ch5.domain.User;
import book.ch5.domain.dao.UserDao;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;

// 이제 여러 트랜잭션을 지원하는 만큼 UserService도 이에 구애되지 않게 만들어한다.
public class UserServiceV3 {
    private static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_SILVER = 30;
    private final UserDao userDao;
    private final PlatformTransactionManager transactionManager;

    public UserServiceV3(UserDao userDao, PlatformTransactionManager transactionManager) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() throws SQLException {
        // 이런 경우에는 PlatformTransactionManager가 있다.
        // 트랜잭션 경계설정을 위한 추상 인터페이스
        // 얘도 생각해보면 빈으로 만들 수 있지 않을까?
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_SILVER);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level" + currentLevel);
        }
    }
}
