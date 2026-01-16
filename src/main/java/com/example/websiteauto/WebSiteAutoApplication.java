package com.example.websiteauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class WebSiteAutoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSiteAutoApplication.class, args);
    }
}
