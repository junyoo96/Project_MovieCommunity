package jun.moviecommunity.controller;

import jun.moviecommunity.service.LoginService;
import jun.moviecommunity.service.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
        log.info("로그인");

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        UserDto loginUser = loginService.login(form.getLoginId(), form.getPassword());

        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 존재하지 않거나 틀립니다");
            return "login/loginForm";
        }

        //로그인 성공 처리 로직
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return "redirect:" + redirectURL;
    }

    /**
     * 로그아웃
    **/
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.info("로그아웃");

        //false로 해야 session이 없을 때 신규 세션을 생성안함
        HttpSession session = request.getSession(false);
        if (session != null) {
            //세션과 그 안에 데이터 모두 다 제거됨
            session.invalidate();
        }

        return "home";
    }
}