package book.ch5;

import book.ch5.domain.Level;
import book.ch5.domain.User;
import book.ch5.domain.dao.UserDao;
import book.ch5.domain.dao.UserDaoJdbcTemplate;

import java.util.List;

public class UserService {
    private static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_SILVER = 50;
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
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
