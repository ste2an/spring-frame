package service.impl;

import annotation.Provider;
import service.UserService;

@Provider
public class UserServiceImpl implements UserService {
    @Override
    public void run() {
        System.out.println("UserServiceImpl is running ...");
    }
}
