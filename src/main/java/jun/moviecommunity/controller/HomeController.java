package jun.moviecommunity.controller;

import jun.moviecommunity.service.LoginService;
import jun.moviecommunity.service.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    //TODO - 나중에 지우기
    private final LoginService loginService;

    @RequestMapping("/")
    public String home(HttpServletRequest request) {

        //TODO - 나중에 지우기(로그인 매번하기 번거로워서 작성
        if (request.getSession().getAttribute(SessionConst.LOGIN_USER ) == null) {
            UserDto loginUser = loginService.login("test", "test!");
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_USER, loginUser);
        }
        //TODO - 끝

        return "home";
    }
}
