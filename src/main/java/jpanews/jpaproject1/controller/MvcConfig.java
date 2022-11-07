package jpanews.jpaproject1.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // 요청 - 뷰 연결
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("main");
        registry.addViewController("/sign-in").setViewName("sign-in");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/sign-up").setViewName("sign-up");
    }
}
