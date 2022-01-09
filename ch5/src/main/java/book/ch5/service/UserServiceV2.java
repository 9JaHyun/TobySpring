package book.ch5;

import book.ch5.domain.Level;
import book.ch5.domain.User;
import book.ch5.domain.dao.UserDao;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// 트랜젝션 적용 시작
public class UserServiceV2 {
    private static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_SILVER = 30;
    private UserDao userDao;
    private DataSource dataSource;

    public UserServiceV2(UserDao userDao, DataSource dataSource) {
        this.userDao = userDao;
        this.dataSource = dataSource;
    }

    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();   // 저장소 비우기
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
