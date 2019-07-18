package com.htsx.resgov.controller;

import org.springframework.stereotype.Component;

@Component
public class PrintTest {

    public String test(){
        System.out.println("try and success");
        return "success";
    }
}
