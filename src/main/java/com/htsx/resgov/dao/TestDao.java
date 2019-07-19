package com.htsx.resgov.dao;


import com.htsx.resgov.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;


//接口的方法对应mappper.xml的增删改查语句的id
@Mapper  //添加注解
public interface TestDao {

    TestEntity getById(Integer id);

}