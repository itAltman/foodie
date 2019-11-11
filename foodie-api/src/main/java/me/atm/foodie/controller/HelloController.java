package me.atm.foodie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Altman
 * RestController 注解默认返回的数据是 json 格式的
 * @date 2019/11/03
 **/
//@Controller
@RestController
@ApiIgnore
public class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("hello")
    public Object hello() {
        // 日志级别 ERROR>WARN>INFO>DEBUG
        log.info("info ==== hello");
        log.debug("debug ==== hello");
        log.warn("warn ==== hello");
        log.error("error ==== hello");
        return "Hello World!!!";
    }
}
