package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter @Setter
public class Comment extends BaseTimeEntity{

    public enum Status {
        DELETED, REMAIN
    }

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

    //무한 대댓글 관련 컬럼
    //REF : 댓글끼리 묶는 기능
    //RE_STEP : 순서 나타내기
    //RE_LEVEL : 들여쓰기 몇번할지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    //삭제된 댓글인지 여부
    //댓글 삭제할 때 삭제할 댓글에 대댓글이 존재한다면 일단 놔두기 위해 사용
    private Status isDeleted;

    public static Comment createComment(User user, Post post, String content) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setIsDeleted(Status.REMAIN);
        return comment;
    }

    public static Comment createReply(User user, Post post, Comment parentComment, String content) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setParent(parentComment);
        comment.setIsDeleted(Status.REMAIN);
        return comment;
    }

    /**
     * 댓글 수정
    **/
    public void update(String content) {
        this.setContent(content);
    }

    /**
     * 댓글 좋아요
     **/
    public void increaseLikeCount() {
        this.setLikeCount(this.likeCount + 1);
    }

    public void updateDeletedStatus(Status status) {
        this.setIsDeleted(status);
    }

    public void setIsDeleted(Status status) {
        this.isDeleted = status;
    }
}
