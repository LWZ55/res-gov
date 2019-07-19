package com.htsx.resgov.service;

import com.htsx.resgov.dao.UserMapper;
import com.htsx.resgov.entity.TestEntity;
import com.htsx.resgov.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestMy {

    @Autowired
    private UserMapper usermapper ;

    public int insertTest(Integer id){

      User user = new User();



        return usermapper.insert(user);
    }
}
