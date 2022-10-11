package jun.moviecommunity.service;

import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
    **/
    @Transactional
    public Long join(User user) {
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * 중복회원 확인
    **/
    private void validateDuplicateUser(User user) {
        //중복 아이디 검사
        List<User> findUsersByName = userRepository.findByName(user.getName());
        if(!findUsersByName.isEmpty()){
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        }

        //중복 이메일 검사
        List<User> findUsersByEmail = userRepository.findByEmail(user.getEmail());
        if(!findUsersByEmail.isEmpty()){
            throw new IllegalStateException("이미 사용중인 메일입니다.");
        }

        //중복 닉네임 검사
        validateDuplicateNickname(user.getNickname());
    }

    /**
     * 중복닉네임 확인
     **/
    private void validateDuplicateNickname(String nickname) {
        //중복 닉네임 검사
        List<User> findUsersByNickname = userRepository.findByNickname(nickname);
        if(!findUsersByNickname.isEmpty()){
            throw new IllegalStateException("이미 사용중인 닉네임입니다.");
        }
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
     * 회원 수정
    **/
    @Transactional
    public User updateUser(Long userId, String password, String nickname){
        User user = userRepository.findById(userId).get();
        validateDuplicateNickname(nickname);
        user.change(password, nickname);
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
