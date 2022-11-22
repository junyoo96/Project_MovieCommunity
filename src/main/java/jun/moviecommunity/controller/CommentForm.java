package jun.moviecommunity.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {

    private Long id;
    private Long postId;
    private Long userId;
    private String content;
}
