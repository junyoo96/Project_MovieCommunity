package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter @Setter
@ToString
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", length = 20)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String title;

    @Column(length=5000)
    private String content;

    private Category category;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    //Post가 삭제될 때 관련된 File도 모두 삭제하기 위해 CascadeType.ALL로 설정
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<File> files = new ArrayList<>();

    /**
     * 연관관계 편의 메소드
    **/
    public void setFiles(List<File> files) {
        if (files != null) {
            for (File file : files) {
                if(file.getPost() != this) {
                    file.setPost(this);
                }
            }
        }
    }

    public static Post createPost(User author, String title, String content, Category category, List<File> files) {
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setFiles(files);
        post.setCreateDate(LocalDateTime.now());
        post.setUpdateDate(LocalDateTime.now());
        return post;
    }

    /**
     * 게시물 수정
    **/
    public void change(String title, String content, Category category, List<File> files) {
        this.setTitle(title);
        this.setContent(content);
        this.setCategory(category);
        this.setFiles(files);
        this.setUpdateDate(LocalDateTime.now());

        //TODO - DB의 file과 비교해서 사용하지 않는 file은 삭제하고 S3에서도 실제 해당 파일 삭제
    }

    /**
     * 게시물 조회수
    **/
    public void increaseVisitCount() {
        this.setViewCount(this.viewCount + 1);
    }

    /**
     * 게시물 좋아요
    **/
    public void increaseLikeCount() {
        this.setLikeCount(this.likeCount + 1);
    }
}
