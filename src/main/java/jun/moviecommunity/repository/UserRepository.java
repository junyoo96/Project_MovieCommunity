package jun.moviecommunity.repository;

import jun.moviecommunity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 회원 아이디(Username)로 조회
    **/
    List<User> findByName(String name);

    /**
     * 회원 이메일(Email)로 조회
     **/
    List<User> findByEmail(String email);


    /**
     * 회원 닉네임(Nickname)으로 조회
    **/
    List<User> findByNickname(String nickname);

}

//@Repository
//@Slf4j
//public class UserRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    /**
//     * 회원 등록
//    **/
//    public void save(User user) { em.persist(user); }
//
//    /**
//     * 회원 ID로 조회
//    **/
//    public User findOne(Long id) {
//        return em.find(User.class, id);
//    }
//
//    /**
//     * 전체 회원 조회
//    **/
//    public List<User> findAll() {
//        return em.createQuery("select u from User u", User.class)
//                .getResultList();
//    }
//
//    /**
//     * 회원 아이디(Username)로 조회
//    **/
//    public List<User> findByName(String name) {
//        return em.createQuery("select u from User u where u.name = :name", User.class)
//                .setParameter("name", name)
//                .getResultList();
//    }
//
//    /**
//     * 회원 이메일(Username)로 조회
//     **/
//    public List<User> findByEmail(String email) {
//        return em.createQuery("select u from User u where u.email = :email", User.class)
//                .setParameter("email", email)
//                .getResultList();
//    }
//
//    /**
//     * 회원 닉네임(Nickname)으로 조회
//    **/
//    public List<User> findByNickname(String nickname) {
//        return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
//                .setParameter("nickname", nickname)
//                .getResultList();
//    }
//
//    /**
//     * 회원 삭제
//     **/
//    public void delete(Long id) {
////        em.createQuery("delete from User u where u.id = :id")
////                .setParameter("id", id);
//        em.remove(findOne(id));
//    }
//}
