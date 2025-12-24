package com.example.websiteauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@SpringBootApplication(scanBasePackages = {
        "com.example.websiteauto",
        "com.example.analysis"
})
@EnableJpaRepositories(basePackages = {
        "com.example.websiteauto.repositories",
        "com.example.analysis.repositories"
})
@EntityScan(basePackages = {
        "com.example.websiteauto.entity",
        "com.example.analysis.entity"
})
public class WebSiteAutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSiteAutoApplication.class, args);
    }

}
