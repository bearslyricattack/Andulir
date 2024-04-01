package org.andulir.controller;

import org.andulir.annotation.ATest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @ATest(2)
    public void test(List<User> users,Integer integer) {
        System.out.println("test");
    }
}


