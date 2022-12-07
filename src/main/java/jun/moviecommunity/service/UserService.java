package jun.moviecommunity.service;

import jun.moviecommunity.controller.UserForm;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입
    **/
    @Transactional
    public Long join(UserForm form) {

        User user = new User(
                form.getLoginId(),
                form.getPassword(),
                form.getName(),
                form.getNickname(),
                form.getEmail()
        );

        userRepository.save(user);
        return user.getId();
    }

    /**
     * 중복회원 검사
    **/
    public boolean validateDuplicateLoginId(String loginId) {
        Optional<User> findUserByLoginId = userRepository.findByLoginId(loginId);
        if(!findUserByLoginId.isEmpty()){
            return false;
        }

        return true;
    }

    /**
     * 중복닉네임 검사
     **/
    public boolean validateDuplicateNickname(String nickname) {
        Optional<User> findUserByNickname = userRepository.findByNickname(nickname);
        if(!findUserByNickname.isEmpty()){
            return false;
        }

        return true;
    }

    /**
     * 중복이메일 확인
    **/
    public boolean validateDuplicateEmail(String email) {
        Optional<User> findUserByEmail = userRepository.findByEmail(email);
        if(!findUserByEmail.isEmpty()){
            return false;
        }

        return true;
    }


    /**
     * 회원 전체 조회
    **/
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    /**
     * 회원 개별 조회
    **/
    public User findOne(Long userId) {
        return userRepository.findById(userId).get();
    }

    /**
     * 회원 로그인으로 조회
    *
     * @return*/
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).get();
    }

    /**
     * 회원 수정
    **/
    @Transactional
    public User updateUser(Long userId, String password, String name, String nickname, String email){
        User user = userRepository.findById(userId).get();
        user.change(password, name, nickname, email);
        return user;
    }

    /**
     * 회원 삭제
    **/
    @Transactional
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
