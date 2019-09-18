package com.tct.smart_lock.sevice.implement;

import com.tct.smart_lock.dao.UserDao;
import com.tct.smart_lock.domain.User;
import com.tct.smart_lock.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    public User FindNameAndPsw(String username, String password){
        return userDao.findByUsernameAndPassword(username,password);
    }
    public void save(User user1){
        userDao.save(user1);
    }
    public List<User> findByName(String username) {
        return userDao.findByUsername(username);
    }

    public int UpdatePassword(String username,String password){
        return userDao.updateUser(username,password);
    }
}
