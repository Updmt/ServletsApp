package org.example.service;

import org.example.model.User;

import java.util.List;

public interface UserService {
    public void save(User user);
    public User get(Long id);
    public List<User> getAll();
    public User update(User user);
    public void delete(Long id);
}
