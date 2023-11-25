package com.ripple.friend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自定义 Swagger 接口文档的配置
 */
@Configuration
@EnableSwagger2WebMvc
// 让该bean对象只在某些特定的环境下生效（注入到容器中），生产环境 prod 不生效，当项目上线时就不能访问接口文档了
@Profile({"dev", "test"})
public class SwaggerConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 这里一定要标注你控制器的位置（扫描包路径）
                .apis(RequestHandlerSelectors.basePackage("com.ripple.friend.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    /**
     * api 信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云漪伙伴匹配系统")
                .description("接口文档")
                .termsOfServiceUrl("https://github.com/cloud-ripple")
                .contact(new Contact("bobo", "https://github.com/cloud-ripple", "xxx@qq.com"))
                .version("1.0")
                .build();
    }
}
