package jun.moviecommunity.repository;

import jun.moviecommunity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 회원 아이디(loginId)로 조회
    **/
    Optional<User> findByLoginId(String loginId);

    /**
     * 회원 이메일(Email)로 조회
     **/
    Optional<User> findByEmail(String email);

    /**
     * 회원 닉네임(Nickname)으로 조회
    **/
    Optional<User> findByNickname(String nickname);
}
