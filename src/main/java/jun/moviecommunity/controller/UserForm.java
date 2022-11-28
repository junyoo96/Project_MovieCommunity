package jun.moviecommunity.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class UserForm {

    @NotEmpty(message = "아이디는 필수사항 입니다")
    private String name;
    @NotEmpty(message = "비밀번호는 필수사항 입니다")
    private String password;
    @NotEmpty(message = "닉네임은 필수사항 입니다")
    private String nickname;
    @NotEmpty(message = "이메일은 필수사항 입니다")
    private String email;
}
