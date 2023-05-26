package service.impl;

import annotation.Injection;
import annotation.Provider;
import dao.UserDao;
import service.UserService;

@Provider
public class UserServiceImpl implements UserService {

    @Injection
    private UserDao userDao;

    private String test1;
    private String test2;
    @Override
    public void run() {
        System.out.println("UserServiceImpl is running ...");
        userDao.run();
    }
}
