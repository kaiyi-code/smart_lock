package com.tct.smart_lock.sevice;

import com.tct.smart_lock.domain.User;

import java.util.List;

public interface UserService {
    User FindNameAndPsw(String username, String password);
    void save(User user1);
    List<User> findByName(String username);
    int UpdatePassword(String username, String password);
}
