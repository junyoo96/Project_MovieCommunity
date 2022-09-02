package jun.moviecommunity.service;

import com.fasterxml.jackson.databind.type.LogicalType;
import jun.moviecommunity.domain.Role;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.nio.channels.IllegalSelectorException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import org.slf4j.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        User user = createUser();

        //when
        Long saveId = userService.join(user);

        //then
        assertEquals(user, userRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아이디_예외() throws Exception {
        //given
        User user1 = new User();
        user1.setName("myId");

        User user2 = new User();
        user2.setName("myId");

        //when
        userService.join(user1);
        userService.join(user2);

        //then
        fail("위에서 예외가 발생해야 한다.");
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_이메일_예외() throws Exception {
        //given
        User user1 = new User();
        user1.setEmail("myEmail@google.com");

        User user2 = new User();
        user2.setEmail("myEmail@google.com");

        //when
        userService.join(user1);
        userService.join(user2);

        //then
        fail("위에서 예외가 발생해야 한다.");
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_닉네임_예외() throws Exception {
        //given
        User user1 = new User();
        user1.setNickname("myNickName");

        User user2 = new User();
        user2.setNickname("myNickName");

        //when
        userService.join(user1);
        userService.join(user2);

        //then
        fail("위에서 예외가 발생해야 한다.");
    }

    @Test
    public void 회원수정() throws Exception {
        //given
        User user = createUser();
        Long saveId = userService.join(user);

        //when
        userService.updateUser(saveId, "changedPassword", "changedNickname");

        //then
        User findUser = userService.findOne(saveId);

        assertEquals("changedPassword", findUser.getPassword());
        assertEquals("changedNickname", findUser.getNickname());
    }

    @Test
    public void 회원삭제() throws Exception {
        //given
        User user1 = new User();
        user1.setName("myName1");
        Long saveId = userService.join(user1);

        User user2 = new User();
        user2.setName("myName2");
        userService.join(user2);

        //when
        userService.deleteUser(saveId);
        List<User> findAllUsers = userRepository.findAll();

        //then
        Assertions.assertThat(findAllUsers.size()).isEqualTo(1);
    }

    private User createUser(){
        User user = new User();
        user.setName("myId");
        user.setPassword("myPassword");
        user.setNickname("myNickName");
        user.setEmail("myEmail@gmail.com");
        user.setImagePath("myPath");
        user.setCreateDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setRole(Role.USER);
        return user;
    }

}