package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "login_id")
    private String loginId;
    @Column(length = 15)
    private String name;
    private String password;
    @Column(length = 20)
    private String nickname;
    private String email;
    private String imagePath;
    @Enumerated(EnumType.STRING)
    private Role role;

    //회원탈퇴시 게시물, 댓글 모두 삭제되게 cascade 설정하기
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    /**
     * 회원 등록 시 생성자
    **/
    public User(String loginId, String password, String name, String nickname, String email) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.USER;
    }

    /**
     * 회원 수정시 변경가능한 정보만 변경하는 것을 보장하기 위해 setter 대신하는 함수
     * 패스워드, 닉네임만 수정가능
    **/
    public void change(String password, String name, String nickname, String email) {
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }
}
