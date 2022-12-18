package jun.moviecommunity.repository;

import jun.moviecommunity.controller.PostSearchCondition;
import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface PostCustomRepository {
    Page<Post> findAllByCriteria(PostSearchCondition condition, Pageable pageable);
}
