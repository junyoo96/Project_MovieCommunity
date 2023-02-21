package jun.moviecommunity.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jun.moviecommunity.controller.PostSearchCondition;
import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.domain.QUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jun.moviecommunity.domain.QPost.post;
import static jun.moviecommunity.domain.QUser.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllByCriteria(PostSearchCondition condition, Pageable pageable) {

        List<Post> posts = queryFactory.selectFrom(post)
                .join(post.author, user)
                .where(
                        containKeyword(condition.getKeyword()),
                        eqCategory(condition.getCategory())
                )
                .orderBy(
                    postSort(pageable)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchJoin().distinct().fetch(); //fetchJoin을 사용해 n + 1 문제 해결

        //count만 갖고오는 쿼리
        JPQLQuery<Post> count = queryFactory.selectFrom(post)
                .where(
                        containKeyword(condition.getKeyword()),
                        eqCategory(condition.getCategory())
                );

        return PageableExecutionUtils.getPage(posts, pageable, () -> count.fetchCount());
    }

    private BooleanExpression containKeyword(String keyword) {
        return keyword != null ? post.title.contains(keyword).or(post.content.contains(keyword)) : null;
    }

    private BooleanExpression eqCategory(Category category) {
        return category != null ? post.category.eq(category) : null;
    }

    private OrderSpecifier<?> postSort(Pageable page) {
        if(!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져옴
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅
                switch (order.getProperty()){
                    case "createDate":
                        return new OrderSpecifier(direction, post.createDate);
                    case "viewCount":
                        return new OrderSpecifier(direction, post.viewCount);
                    case "likeCount":
                        return new OrderSpecifier(direction, post.likeCount);
                }
            }
        }

        return null;
    }
}
