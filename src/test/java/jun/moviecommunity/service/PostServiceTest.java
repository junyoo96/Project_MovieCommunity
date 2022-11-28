package jun.moviecommunity.service;

import jun.moviecommunity.domain.*;
import jun.moviecommunity.repository.FileRepository;
import jun.moviecommunity.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired UserService userService;
    @Autowired
    FileRepository fileRepository;

    private final static Logger log = LoggerFactory.getLogger(PostServiceTest.class);

    @Test
    public void 게시글등록() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        //when
        Long postId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        //then
        Post getPost = postRepository.findById(postId).get();

        assertEquals(user.getId(), getPost.getAuthor().getId());
        assertEquals("newTitle", getPost.getTitle());
        assertEquals("newContent", getPost.getContent());
        assertEquals(Category.INTRODUCTION, getPost.getCategory());
        assertEquals("path/image1.png", getPost.getFiles().get(0).getUrlPath());
        assertEquals("path/image2.png", getPost.getFiles().get(1).getUrlPath());
    }

    @Test
    public void 게시글전체조회() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        Long post1Id = postService.savePost(user.getId(), "title1", "content1", Category.INTRODUCTION, createFileUrlPaths());
        Long post2Id = postService.savePost(user.getId(), "title2", "content2", Category.REVIEW, createFileUrlPaths());

        //when
        List<Post> findAllPosts = postService.findPosts();

        //then
        Assertions.assertThat(findAllPosts.size()).isEqualTo(2);
    }

    @Test
    public void 게시글조회() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        Long saveId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());

        //when
        Post findPost = postService.findOne(saveId);

        //then
        assertEquals(user.getId(), findPost.getAuthor().getId());
        assertEquals("newTitle", findPost.getTitle());
        assertEquals("newContent", findPost.getContent());
        assertEquals(Category.INTRODUCTION, findPost.getCategory());
        assertEquals("path/image1.png", findPost.getFiles().get(0).getUrlPath());
        assertEquals("path/image2.png", findPost.getFiles().get(1).getUrlPath());
    }

    @Test
    public void 유저게시글조회() throws Exception {
        //given
        User user1 = new User();
        user1.setName("user1Id");
        userService.join(user1);

        User user2 = new User();
        user2.setName("user2Id");
        user2.setEmail("email2");
        user2.setNickname("nickname2");
        userService.join(user2);

        Long post1Id = postService.savePost(user1.getId(), "title1", "content1", Category.INTRODUCTION, createFileUrlPaths());
        Long post2Id = postService.savePost(user1.getId(), "title2", "content2", Category.INTRODUCTION, createFileUrlPaths());
        Long post3Id = postService.savePost(user2.getId(), "title3", "content3", Category.REVIEW, createFileUrlPaths());

        //when
        List<Post> findPosts = postService.findPostsByUserId(user1.getId());

        //then
        Assertions.assertThat(findPosts.size()).isEqualTo(2);

    }

    @Test
    public void 게시글수정() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        Long saveId = postService.savePost(user.getId(), "newTitle", "newContent", Category.INTRODUCTION, createFileUrlPaths());

        //when
        Post findPost = postService.findOne(saveId);
        //삭제할 파일
        File findFile = findPost.getFiles().get(0);
        findPost.getFiles().remove(findFile);
        fileRepository.delete(findFile.getId());

        //TODO - 나중에 updatePost에서 삭제할 파일도 비교해서 삭제하는 방식으로 해야할듯
        Post updatePost = postService.updatePost(findPost.getId(), "updatedTitle", "updatedContent", Category.REVIEW);
        File removedFile = fileRepository.findOne(findFile.getId());

        //then
        assertEquals("updatedTitle", updatePost.getTitle());
        assertEquals("updatedContent", updatePost.getContent());
        assertEquals(Category.REVIEW, updatePost.getCategory());
        Assertions.assertThat(removedFile).isNull();
    }

    @Test
    public void 게시글삭제() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        Long post1Id = postService.savePost(user.getId(), "title1", "content1", Category.INTRODUCTION, createFileUrlPaths());
        Long post2Id = postService.savePost(user.getId(), "title2", "content2", Category.REVIEW, createFileUrlPaths());

        //when
        postService.deletePost(post1Id);
        List<Post> findAllPosts = postRepository.findAll();

        //then
        Assertions.assertThat(findAllPosts.size()).isEqualTo(1);
    }

    @Test
    public void 게시글검색() throws Exception {
        //given
        User user = createUser();
        userService.join(user);
        postService.savePost(user.getId(), "keyword", "newContent", Category.INTRODUCTION, createFileUrlPaths());
        postService.savePost(user.getId(), "newTitle", "keyword", Category.INTRODUCTION, createFileUrlPaths());
        postService.savePost(user.getId(), "newTitle1", "newContent1", Category.INTRODUCTION, createFileUrlPaths());
        postService.savePost(user.getId(), "newTitle2", "newContent2", Category.INTRODUCTION, createFileUrlPaths());
        postService.savePost(user.getId(), "newTitle3", "newContent3", Category.INTRODUCTION, createFileUrlPaths());

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createDate"));

        //when
        Page<Post> page = postService.searchByKeyword("keyword", pageRequest);

        //then
        List<Post> content = page.getContent();

        Assertions.assertThat(content.size()).isEqualTo(1);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }

    private List<String> createFileUrlPaths() {
        List<String> fileUrlPaths = new ArrayList<>();
        fileUrlPaths.add("path/image1.png");
        fileUrlPaths.add("path/image2.png");
        return fileUrlPaths;
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