package jun.moviecommunity.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jun.moviecommunity.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jun.moviecommunity.domain.QComment.*;

//참고 : https://kukekyakya.tistory.com/9?category=1022639

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createDate.asc()
                ).fetch();
    }
}
