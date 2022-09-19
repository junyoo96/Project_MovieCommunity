package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.File;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.PostRepository;
import jun.moviecommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 등록
    **/
    @Transactional
    public Long savePost(Long userId, String title, String content, Category category, List<String> fileUrlPaths){
        //엔티티 조회
        User user = userRepository.findOne(userId);

        //게시글 생성
        Post post = Post.createPost(user, title, content, category);

        //파일 생성
        List<File> files = new ArrayList<>();
        for(String fileUrlPath : fileUrlPaths){
            File file = File.createFile(post, fileUrlPath);
            files.add(file);
        }

        //게시글 저장
        postRepository.save(post);
        return post.getId();
    }

    /**
     * 게시글 전체 조회
    **/
    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    /**
     * 게시글 개별 조회
    **/
    public Post findOne(Long postId) { return postRepository.findOne(postId); }

    /**
     * 게시글 유저별 조회
    **/
    public List<Post> findPostsByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }

    /**
     * 게시글 수정
    **/
    @Transactional
    public Post updatePost(Long postId, String title, String content, Category category){
        Post post = postRepository.findOne(postId);
        post.change(title, content, category);
        return post;
    }

    /**
     * 게시글 삭제
    **/
    @Transactional
    public void deletePost(Long postId) { postRepository.delete(postId); }
}
