package jun.moviecommunity;

import jun.moviecommunity.controller.SessionConst;
import jun.moviecommunity.service.UserDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@EnableJpaAuditing //JPA Auditing 기능 사용
@SpringBootApplication
public class MoviecommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviecommunityApplication.class, args);
	}

//	//Auditing
//	@Bean
//	public AuditorAware<String> auditorProvider(HttpSession session) {
//		UserDto userDto = (UserDto) session.getAttribute(SessionConst.LOGIN_USER);
//		return () -> Optional.of(String.valueOf(userDto.getId()));
//	}

}
