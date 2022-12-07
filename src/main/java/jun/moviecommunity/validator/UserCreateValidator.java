package jun.moviecommunity.validator;

import jun.moviecommunity.controller.UserForm;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreateValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("회원가입 검사");

        UserForm form = (UserForm) target;
        BindingResult bindingResult = (BindingResult) errors;

        //아이디 중복검사
        if (!userService.validateDuplicateLoginId(form.getLoginId())) {
            log.info("아이디 중복");
            bindingResult.addError(new FieldError("userForm",
                    "loginId",
                    form.getLoginId(),
                    false,
                    null,
                    null,
                    "이미 존재하는 아이디입니다"));
        }

        //닉네임 중복검사
        if (!userService.validateDuplicateNickname(form.getNickname())) {
            log.info("닉네임 중복");
            bindingResult.addError(new FieldError("userForm",
                    "nickname",
                    form.getNickname(),
                    false,
                    null,
                    null,
                    "이미 존재하는 닉네임입니다"));
        }

        //이메일 중복검사
        if(!userService.validateDuplicateEmail(form.getEmail())) {
            log.info("이메일 중복");
            bindingResult.addError(new FieldError("userForm",
                    "email",
                    form.getEmail(),
                    false,
                    null,
                    null,
                    "이미 존재하는 이메일입니다"));
        }
    }
}
