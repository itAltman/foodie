package me.self.foodie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController 默认返回的数据是 json 格式的
 *
 * @author Altman
 * @date 2019/11/03
 **/
//@Controller
@RestController
public class HelloController {

    @GetMapping("hello")
    public Object hello() {
        return "Hello World!!!";
    }
}
