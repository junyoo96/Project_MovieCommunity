package jun.moviecommunity.service;

import jun.moviecommunity.domain.*;
import jun.moviecommunity.repository.CommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired UserService userService;
    @Autowired PostService postService;

    private final static Logger log = LoggerFactory.getLogger(CommentServiceTest.class);

    @Test
    public void 댓글등록() throws Exception {
        //given
        User user = createUser();
        userService.join(user);

        Long postId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post = postService.findOne(postId);

        //when
        Long commentId = commentService.save(user.getId(), postId, "newComment");

        //then
        Comment comment = commentRepository.findOne(commentId);

        assertEquals(user.getId(), comment.getAuthor().getId());
        assertEquals(postId, comment.getPost().getId());
        assertEquals("newComment", comment.getContent());
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

    private List<String> createFileUrlPaths() {
        List<String> fileUrlPaths = new ArrayList<>();
        fileUrlPaths.add("path/image1.png");
        fileUrlPaths.add("path/image2.png");
        return fileUrlPaths;
    }
}