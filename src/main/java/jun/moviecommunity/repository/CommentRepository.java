package jun.moviecommunity.repository;

import jun.moviecommunity.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 사용자 id로 댓글 조회
    **/
    @Query("select c from Comment c where c.author.id = :userId")
    List<Comment> findAllByUserId(@Param("userId") Long userId);

    /**
     * 게시물 id로 댓글 조회
    **/
    @Query("select c from Comment c where c.post.id = :postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    /**
     * 게시물 id로 댓글 삭제
    **/
    void deleteAllByPostId(Long postId);
}