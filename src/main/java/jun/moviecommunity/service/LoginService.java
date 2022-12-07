package jun.moviecommunity.service;

import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    /**
     * null 이면 로그인 실패
    **/
    public UserDto login(String loginId, String password) {
        //패스워드 비교해서 같으면 user, 아니면 null 반환
        User loginUser = userRepository.findByLoginId(loginId).filter(u -> u.getPassword().equals(password)).orElse(null);
        return new UserDto(loginUser);
    }
}
