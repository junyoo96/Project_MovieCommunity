package jun.moviecommunity.service;

import jun.moviecommunity.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long authorId;
    private String authorNickName;
    private String content;
    private int likeCount;
    private String createDate;
    private String updateDate;
    //대댓글
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.authorId = comment.getAuthor().getId();
        this.authorNickName = comment.getAuthor().getNickname();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(comment.getCreateDate());
        this.updateDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(comment.getUpdateDate());
    }
}
