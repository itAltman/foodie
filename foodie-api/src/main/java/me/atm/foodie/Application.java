package me.atm.foodie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Altman
 * @date 2019/11/03
 **/
@SpringBootApplication(scanBasePackages = "me.atm")
// MapperScan 表示自己创建的 mapper 需要交给 Springboot 去扫描，否则无法使用。 而且必须是 tk 包下的，否则也会有问题
@MapperScan(basePackages = {"me.atm.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
