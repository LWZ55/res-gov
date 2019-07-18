package com.htsx.resgov.service;


import com.htsx.resgov.dao.TestDao;
import com.htsx.resgov.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//注入TestDao对象
@Service
public class TestService {

    @Autowired
    private TestDao testDao ;

    public TestEntity getById(Integer id){
        return testDao.getById(id);
    }
}