package jun.moviecommunity;

import jun.moviecommunity.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 인터셉터 등록
    **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //로그인 인터셉터 등록
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**") //모든 경로에 인터셉터 적용
                .excludePathPatterns("/",
                        "/static/**",
                        "/*.ico", "/error",
                        "/login",
                        "/logout",
                        "/members/new"
                ); //다음 url은 로그인 인터셉터 적용안함
    }
}