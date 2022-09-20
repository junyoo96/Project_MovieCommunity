package jun.moviecommunity.service;

import jun.moviecommunity.domain.*;
import jun.moviecommunity.repository.CommentRepository;
import org.assertj.core.api.Assertions;
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
@Transactional
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
        Long commentId = commentService.saveComment(user.getId(), postId, "newComment");

        //then
        Comment comment = commentRepository.findOne(commentId);

        assertEquals(user.getId(), comment.getAuthor().getId());
        assertEquals(postId, comment.getPost().getId());
        assertEquals("newComment", comment.getContent());
    }

    @Test
    public void 댓글조회() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        Long postId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post = postService.findOne(postId);
        Long commentId = commentService.saveComment(user.getId(), post.getId(), "newContent");

        //when
        Comment findComment = commentService.findOne(commentId);

        //then
        assertEquals("newContent", findComment.getContent());
    }

    @Test
    public void 게시글별댓글조회() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        Long post1Id = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post1 = postService.findOne(post1Id);
        Long comment1Id = commentService.saveComment(user.getId(), post1.getId(), "newContent1");
        Comment comment1 = commentRepository.findOne(comment1Id);

        Long post2Id = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post2 = postService.findOne(post2Id);
        Long comment2Id = commentService.saveComment(user.getId(), post2.getId(), "newContent2");
        Comment comment2 = commentRepository.findOne(comment2Id);

        //when
        List<Comment> findComments = commentService.findCommentsByPostId(post2Id);

        //then
        assertEquals("newContent2", findComments.get(0).getContent());
    }

    @Test
    public void 회원별댓글조회() throws Exception {
        //given
        User user1 = new User();
        user1.setName("user1");
        userService.join(user1);

        User user2 = new User();
        user2.setName("user2");
        userService.join(user2);

        Long post1Id = postService.savePost(user1.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post1 = postService.findOne(post1Id);
        Long comment1Id = commentService.saveComment(user1.getId(), post1.getId(), "newContent1");
        Long comment2Id = commentService.saveComment(user2.getId(), post1.getId(), "newContent2");

        //when
        List<Comment> comments = commentService.findCommentsByUserId(user2.getId());

        //then
        assertEquals("newContent2", comments.get(0).getContent());
    }

    @Test
    public void 댓글수정() throws Exception {
        //given
        User user = createUser();
        userService.join(user);

        Long postId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post = postService.findOne(postId);

        Long commentId = commentService.saveComment(user.getId(), postId, "newComment");

        //when
        Comment comment = commentService.updateComment(commentId, "updatedComment");

        //then
        assertEquals("updatedComment", comment.getContent());
    }

    @Test
    public void 댓글삭제() throws Exception {
        //given
        User user = createUser();
        userService.join(user);

        Long postId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        Post post = postService.findOne(postId);

        Long commentId = commentService.saveComment(user.getId(), postId, "newComment");

        //when
        commentService.deleteComment(commentId);
        Comment removedComment = commentService.findOne(commentId);

        //then
        Assertions.assertThat(removedComment).isNull();

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