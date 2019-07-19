package com.htsx.resgov.controller;



import com.htsx.resgov.service.TestMy;
import com.htsx.resgov.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService ;

    @Autowired
    private TestMy testMy;

    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    public int test(@PathVariable Integer id){
        System.out.println("id:" + id);
        return testMy.insertTest(id);
    }

}
