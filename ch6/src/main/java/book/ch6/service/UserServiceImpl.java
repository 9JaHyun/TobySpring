package book.ch6.service;

import book.ch6.domain.Level;
import book.ch6.domain.User;
import book.ch6.domain.dao.UserDao;

import java.sql.SQLException;
import java.util.List;

// 이젠 트랜잭션 경계설정을 하는 코드와 내부 로직의 코드를 분리해야 한다.
public class UserServiceImpl implements UserService{
    private static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_SILVER = 30;
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    public void upgradeLevels() throws SQLException {
            upgradeLevelsInternal();
    }

    // 메서드 분리
    private void upgradeLevelsInternal() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
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
