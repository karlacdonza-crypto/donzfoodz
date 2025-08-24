package br.com.donza.donzfoodz.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("local") // 👉 só carrega essa config quando o perfil "local" estiver ativo
public class BeanLoggerConfig {

    @Bean
    CommandLineRunner listBeans(ApplicationContext ctx) {
        return args -> {
            System.out.println("=== Beans carregados pelo Spring Boot ===");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName + " -> " + ctx.getBean(beanName).getClass().getName());
            }
        };
    }
}
