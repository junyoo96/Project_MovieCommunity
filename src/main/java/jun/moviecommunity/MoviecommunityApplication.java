package jun.moviecommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //JPA Auditing 기능 사용
@SpringBootApplication
public class MoviecommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviecommunityApplication.class, args);
	}

}
