package jun.moviecommunity.service;

import jun.moviecommunity.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String authorNickName;
    private String content;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    //대댓글
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.authorNickName = comment.getAuthor().getNickname();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.createDate = comment.getCreateDate();
        this.updateDate = comment.getUpdateDate();
    }
}
