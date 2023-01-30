package icu.zxb996.mp.web.config;

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
 * @author Gavin Zhang
 * @date 2023/1/10 19:36
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        // 创建 Docket 对象
        // 文档类型，使用 Swagger2
        return new Docket(DocumentationType.SWAGGER_2)
                // 设置 API 信息
                .apiInfo(this.apiInfo())
                // 扫描 Controller 包路径，获得 API 接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("icu.zxb996.mp.web.controller"))
                .paths(PathSelectors.any())
                // 构建出 Docket 对象
                .build();
    }

    /**
     * 创建 API 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("消息推送平台接口文档示例")
                .description("mp")
                // 版本号
                .version("1.0.0")
                // 联系人
                .contact(new Contact("apdo", "https://www.zxb996.icu", "zxb_worky@163.com"))
                .build();
    }
}
