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

    public static Comment createComment(User user, Post post, String content) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());
        return comment;
    }

    public void change(String content) {
        this.setContent(content);
        this.setUpdateDate(LocalDateTime.now());
    }
}
