package jun.moviecommunity.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    @NotBlank(message = "아이디는 필수사항 입니다")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수사항 입니다")
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수사항 입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수사항 입니다")
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수사항 입니다")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    //회원 수정했는지 여부 확인
    private boolean isSame;

    public boolean getIsSame() {
        return isSame;
    }

    public void setIsSame(boolean isSame) {
        this.isSame = isSame;
    }
}