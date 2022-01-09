package book.ch6.service;

import book.ch6.domain.User;

import java.sql.SQLException;

public interface UserService{
    public void add(User user);
    public void upgradeLevels() throws SQLException;
}
