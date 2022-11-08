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
    private String authorName;
    private String title;
    private String content;
    private Category category;
    private int viewCount;
    private int likeCount;
    private LocalDate createDate;
    private LocalDate updateDate;

    public PostDto(Post post) {
        this.id = post.getId();
        this.authorName = post.getAuthor().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createDate = post.getCreateDate().toLocalDate();
        this.updateDate = post.getUpdateDate().toLocalDate();
    }
}
