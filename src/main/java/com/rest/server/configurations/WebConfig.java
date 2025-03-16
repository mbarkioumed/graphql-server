package com.rest.server.configurations;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.rest.server.models.User;
import com.rest.server.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class WebConfig implements WebMvcConfigurer {
 //cc
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/api/*");
        filterRegistrationBean.setName("etagFilter");
        return filterRegistrationBean;
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
                User demoUser = new User();
                demoUser.setUserId("1"); // Same ID used in frontend demo login
                demoUser.setUserEmail("demo@example.com");
                demoUser.setUserPassword("demopass"); // Set a password for the demo user
                demoUser.setUserFirstName("Demo");
                demoUser.setUserLastName("User");

                userRepository.save(demoUser);
                System.out.println("Demo user created successfully");
        };
    }

}