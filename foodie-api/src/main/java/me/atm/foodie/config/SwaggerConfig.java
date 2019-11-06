package me.atm.foodie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 文档配置类
 *
 * @author Altman
 * @date 2019/11/05
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // 原路径 - http://localhost:8088/swagger-ui.html
    // 换肤  -  http://localhost:8088/doc.html

    // 配置swagger2核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 指定api类型为swagger2
                .apiInfo(apiInfo())                     // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("me.atm.foodie.controller"))   // 指定controller包
                .paths(PathSelectors.any())                     // 所有controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口api")                  // 文档页标题
                .contact(new Contact("Altman",
                        "https://www.atm.me",
                        "altman798393546@gmail.com"))      // 联系人信息
                .description("专为天天吃货提供的api文档")           // 详细信息
                .version("1.0.0")                               // 文档版本号
                .termsOfServiceUrl("https://www.atm.me")        // 网站地址
                .build();
    }

}
