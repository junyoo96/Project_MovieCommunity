package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String authorNickName;
    private String title;
    private String content;
    private Category category;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public PostDto(Post post) {
        this.id = post.getId();
        this.authorNickName = post.getAuthor().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
    }
}
