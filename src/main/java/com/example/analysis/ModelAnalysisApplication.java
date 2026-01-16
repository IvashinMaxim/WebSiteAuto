package com.example.analysis;

import com.example.websiteauto.service.RegressionRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.analysis", "com.example.websiteauto"
})
@EnableJpaRepositories(basePackages = {
        "com.example.websiteauto.repositories",
        "com.example.analysis.repositories"
})
@EntityScan(basePackages = {
        "com.example.websiteauto.entity",
        "com.example.analysis.entity"
})
public class ModelAnalysisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModelAnalysisApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(RegressionRunner regressionRunner) {
        return args -> {
            try {
                regressionRunner.run();
            } catch (Exception e) {
                System.err.println("Ошибка во время выполнения регрессии: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("Завершение работы анализатора...");
                System.exit(0);
            }
        };
    }
}
