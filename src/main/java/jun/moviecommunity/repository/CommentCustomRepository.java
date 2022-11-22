package jun.moviecommunity.repository;

import jun.moviecommunity.domain.Comment;

import java.util.List;

public interface CommentCustomRepository {

    List<Comment> findAllByPostId(Long postId);
}
