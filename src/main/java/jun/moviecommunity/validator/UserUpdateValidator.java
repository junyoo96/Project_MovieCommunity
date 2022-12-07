package jun.moviecommunity.validator;

import jun.moviecommunity.controller.UserForm;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("회원수정 검사");

        UserForm form = (UserForm) target;
        BindingResult bindingResult = (BindingResult) errors;
        User user = userService.findByLoginId(form.getLoginId());

        //변경된 내용 없는지 여부 확인
        if (form.getName().equals(user.getName()) && form.getNickname().equals(user.getNickname()) && form.getEmail().equals(user.getEmail()) && form.getPassword().equals(user.getPassword())) {
            bindingResult.addError(new FieldError("userForm",
                    "isSame",
                    "변경 내용이 없습니다"));
        } else {
            //닉네임 중복검사
            if(!userService.validateDuplicateNickname(form.getNickname()) && !form.getNickname().equals(user.getNickname())) {
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
            if(!userService.validateDuplicateEmail(form.getEmail()) && !form.getEmail().equals(user.getEmail())) {
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
}
