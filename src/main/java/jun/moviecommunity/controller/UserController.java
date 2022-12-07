package jun.moviecommunity.controller;

import jun.moviecommunity.domain.User;
import jun.moviecommunity.service.LoginService;
import jun.moviecommunity.service.UserDto;
import jun.moviecommunity.service.UserService;
import jun.moviecommunity.validator.UserCreateValidator;
import jun.moviecommunity.validator.UserUpdateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final UserCreateValidator userCreateValidator;
    private final UserUpdateValidator userUpdateValidator;

    /**
     * 회원 가입 화면
    **/
    @GetMapping("/members/new")
    public String createUserForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/createUserForm";
    }

    /**
     * 회원 가입
    **/
    @PostMapping("/members/new")
    public String create(@Valid UserForm form, BindingResult result, HttpServletRequest request) {

        userCreateValidator.validate(form, result);

        if(result.hasErrors()) {
            return "users/createUserForm";
        }

        //바로 로그인 처리
        Long userId = userService.join(form);
        UserDto loginUser = loginService.login(form.getLoginId(), form.getPassword());

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return "redirect:/";
    }

    /**
     * 마이페이지 조회
    **/
    @GetMapping("/users/myPage")
    public String myPage(HttpServletRequest request, Model model, HttpSession session) {
        UserDto userDto = (UserDto)session.getAttribute(SessionConst.LOGIN_USER);
        User findUser = userService.findOne(userDto.getId());

        UserForm form = new UserForm(
                findUser.getLoginId(),
                findUser.getPassword(),
                findUser.getName(),
                findUser.getNickname(),
                findUser.getEmail(),
                false
        );

        model.addAttribute("userForm", form);
        return "users/myPage";
    }

    /**
     * 회원 수정
    **/
    @PostMapping("/users/edit")
    public String update(@Valid UserForm form, BindingResult result, Model model, HttpSession httpSession) {
        log.info("회원 수정");

        userUpdateValidator.validate(form, result);

        if(result.hasErrors()) {
            log.info("회원 수정 에러");
            return "users/myPage";
        }

        UserDto userDto = (UserDto) httpSession.getAttribute(SessionConst.LOGIN_USER);
        userService.updateUser(userDto.getId(), form.getPassword(), form.getName(), form.getNickname(), form.getEmail());

        model.addAttribute("userForm", form);
        return "users/myPage";
    }
}
