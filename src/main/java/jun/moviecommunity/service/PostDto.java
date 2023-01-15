package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class PostDto {

    private Long id;
    private Long authorId;
    private String authorNickName;
    private String title;
    private String content;
    private Category category;
    private int viewCount;
    private int likeCount;
    private String createDate;
    private String updateDate;

    public PostDto(Post post) {
        this.id = post.getId();
        this.authorId = post.getAuthor().getId();
        this.authorNickName = post.getAuthor().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(post.getCreateDate());
        this.updateDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(post.getUpdateDate());
    }
}
