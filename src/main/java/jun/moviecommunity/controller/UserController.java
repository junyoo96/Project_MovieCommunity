package jun.moviecommunity.controller;

import jun.moviecommunity.service.CreateUserRequest;
import jun.moviecommunity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원등록 화면
    **/
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/createUserForm";
    }

    /**
     * 회원등록
    **/
    @PostMapping("/members/new")
    public String create(@Valid UserForm form, BindingResult result) {

        if(result.hasErrors()) {
            return "users/createUserForm";
        }

        userService.join(new CreateUserRequest(
                form.getName(),
                form.getPassword(),
                form.getNickname(),
                form.getEmail()
        ));

        return "redirect:/";
    }
}
