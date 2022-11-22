package jun.moviecommunity.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter @Setter
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // 무한 대댓글 관련 컬럼
    //REF : 댓글끼리 묶는 기능
    //RE_STEP : 순서 나타내기
    //RE_LEVEL : 들여쓰기 몇번할지
//    @Enumerated(value = EnumType.STRING)
//    private DeleteStatus isDeleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public static Comment createComment(User user, Post post, String content) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());
        return comment;
    }

    public static Comment createReply(User user, Post post, Comment parentComment, String content) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());
        comment.setParent(parentComment);
        return comment;
    }

    /**
     * 댓글 수정
    **/
    public void update(String content) {
        this.setContent(content);
        this.setUpdateDate(LocalDateTime.now());
    }

    /**
     * 댓글 좋아요
     **/
    public void increaseLikeCount() {
        this.setLikeCount(this.likeCount + 1);
    }
}
