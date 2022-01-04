package book.ch5.domain.dao;

import book.ch5.domain.User;

import java.util.List;

public interface UserDao {
    public void add(User user);
    public int count();
    public User get(String id);
    public List<User> getAll();
    public void update(User user);
    public void deleteAll();
}
