package jp.co.genproject.ettewebserve.service.impl;

import jp.co.genproject.ettewebserve.dao.UserDao;
import jp.co.genproject.ettewebserve.dto.UserDto;
import jp.co.genproject.ettewebserve.entity.User;
import jp.co.genproject.ettewebserve.service.UserService;

public class UserServiceImpl implements UserService{
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }
    
    public void insertUser(UserDto userDto){
        userDao.insertUser(userDto);
    }

    public User findByloginId(String loginId){
        return userDao.findByloginId(loginId);
    }
}
