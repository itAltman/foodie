package me.atm.foodie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Altman
 * @date 2019/11/03
 **/
// SpringBootApplication 注解默认只扫描本类所在的包以及其子包。也就是说，有可能其父包里面的文件不会被自动装配
//@SpringBootApplication(scanBasePackages = "me.atm")
@SpringBootApplication
// MapperScan 表示自己创建的 mapper 需要交给 Springboot 去扫描，否则无法使用。 而且必须是 tk 包下的，否则也会有问题
@MapperScan(basePackages = {"me.atm.mapper"})
@ComponentScan(basePackages = {"me.atm","org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        // 解决 es 集成报错：Error creating bean with name 'itemsController': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'itemServiceImpl': Injection of resource dependencies failed; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'elasticsearchTemplate' defined in class path resource [org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchDataAutoConfiguration.class]: Unsatisfied dependency expressed through method 'elasticsearchTemplate' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'elasticsearchClient' defined in class path resource [org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.elasticsearch.client.transport.TransportClient]: Factory method 'elasticsearchClient' threw exception; nested exception is java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(Application.class, args);
    }
}
