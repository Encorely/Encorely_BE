package spring.encorely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EncorelyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncorelyApplication.class, args);
	}

}
