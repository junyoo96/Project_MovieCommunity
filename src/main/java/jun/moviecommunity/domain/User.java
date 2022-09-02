package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username", length = 15)
    private String name;
    private String password;
    @Column(length = 15)
    private String nickname;
    private String email;
    private String imagePath;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Enumerated(EnumType.STRING)
    private Role role;

    //회원탈퇴시 게시물, 댓글 모두 삭제되게 cascade 설정하기
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    /**
     * 회원 수정시 변경가능한 정보만 변경하는 것을 보장하기 위해 setter 대신하는 함수
     * 패스워드, 닉네임만 수정가능
    **/
    public void change(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }
}
