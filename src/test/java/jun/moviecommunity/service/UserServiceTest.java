package jun.moviecommunity.service;

import jun.moviecommunity.domain.Role;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(user, userRepository.findById(saveId).get());
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
        user1.setNickname("myNickname");

        User user2 = new User();
        user2.setNickname("myNickname");

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
        User updateUser = userService.updateUser(saveId, "newPassword", "newNickname");

        //then
        assertEquals("newPassword", updateUser.getPassword());
        assertEquals("newNickname", updateUser.getNickname());
    }

    @Test(expected = IllegalStateException.class)
    public void 회원수정_중복_닉네임_예외() throws Exception {
        //given
        User user = createUser();
        Long saveId = userService.join(user);

        //when
        userService.updateUser(saveId, "newPassword", "myNickname");

        //then
        fail("위에서 예외가 발생해야 한다.");
    }

    @Test
    public void 회원삭제() throws Exception {
        //given
        User user1 = new User();
        user1.setName("myName1");
        Long saveId = userService.join(user1);

        User user2 = new User();
        user2.setName("myName2");
        user2.setEmail("email2");
        user2.setNickname("nickname2");
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
        user.setNickname("myNickname");
        user.setEmail("myEmail@gmail.com");
        user.setImagePath("myPath");
        user.setCreateDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        user.setRole(Role.USER);
        return user;
    }

}