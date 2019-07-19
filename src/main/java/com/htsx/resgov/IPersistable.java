package com.htsx.resgov;

import org.springframework.stereotype.Component;

/*
* DDL:类名——》Order——》
* */
public interface IPersistable<T> {
    Integer classId();

//    void insert(IXStepEvent data);
//
//    default void save(IXStepEvent data) {
//            Order order = new Order();
//        order.setClOrdId(data.getString(11));
//    }




}
