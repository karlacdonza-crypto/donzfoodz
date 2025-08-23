package br.com.donza.donzfoodz;

import io.awspring.cloud.messaging.config.annotation.EnableSqs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSqs
public class DonzfoodzApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonzfoodzApplication.class, args);
	}

}
