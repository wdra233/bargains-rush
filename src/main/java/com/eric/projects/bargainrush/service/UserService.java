package com.eric.projects.bargainrush.service;

import com.eric.projects.bargainrush.dao.UserDao;
import com.eric.projects.bargainrush.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public boolean tx() {
        User u1 = new User();
        u1.setId(1);
        u1.setName("u1");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(2);
        u2.setName("u2");
        userDao.insert(u2);

        return true;
    }
}
