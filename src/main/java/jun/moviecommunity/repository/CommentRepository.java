package jun.moviecommunity.repository;

import jun.moviecommunity.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findAllByPostId(Long postId) {
        return em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Comment> findAllByUserId(Long userId) {
        return em.createQuery("select c from Comment c where c.author.id = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void delete(Long id) {
        em.remove(findOne(id));
    }
}
