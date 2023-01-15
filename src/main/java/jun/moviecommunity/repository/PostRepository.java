package jun.moviecommunity.repository;

import jun.moviecommunity.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 사용자 id로 게시물 조회
    **/
    @Query("select p from Post p where p.author.id = :userId")
    List<Post> findAllByUserId(Long userId);

    /**
     * 게시물 제목 또는 내용으로 게시물 검색
    **/
    @Query(
        value = "SELECT p FROM Post p WHERE p.title LIKE %:searchKeyword% OR p.content LIKE %:searchKeyword%",
        countQuery = "SELECT COUNT(p.id) FROM Post p WHERE p.title LIKE %:searchKeyword% OR p.content LIKE %:searchKeyword%"
    )
    Page<Post> findAllByTitleOrContent(@Param("searchKeyword") String searchKeyword, Pageable pageable);
}