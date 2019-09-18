package com.tct.smart_lock.dao;

import com.tct.smart_lock.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {
    public User findByUsernameAndPassword(String username, String password);
    public List<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.username = ?1")
    public int updateUser(String username, String password);
}
