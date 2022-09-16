package jun.moviecommunity.repository;

import jun.moviecommunity.domain.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;

    public File findOne(Long id) {
        return em.find(File.class, id);
    }

    @Transactional
    public void delete(Long id) {
        em.remove(findOne(id));
    }
}
