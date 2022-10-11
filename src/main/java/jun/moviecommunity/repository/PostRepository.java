package jun.moviecommunity.repository;

import jun.moviecommunity.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }

    public List<Post> findAllByUserId(Long userId) {
        return em.createQuery("select p from Post p where p.author.id = :userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void delete(Long id) {
        em.remove(findOne(id));
    }

    public List<Post> searchByTitle(String keyword) {
        return em.createQuery("select p from Post p where p.title like :keyword")
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }
}

