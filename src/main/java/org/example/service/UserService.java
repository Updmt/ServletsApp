package org.example.service;

import org.example.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User get(Long id);
    List<User> getAll();
    User update(User user);
    void delete(Long id);
}
