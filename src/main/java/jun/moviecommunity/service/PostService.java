package jun.moviecommunity.service;

import jun.moviecommunity.controller.PostSearchCondition;
import jun.moviecommunity.controller.SmartEditor;
import jun.moviecommunity.domain.File;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.CommentRepository;
import jun.moviecommunity.repository.PostCustomRepository;
import jun.moviecommunity.repository.PostRepository;
import jun.moviecommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostCustomRepository postCustomRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시글 등록
    **/
    @Transactional
    public Long savePost(CreatePostRequest request){
        //엔티티 조회
        User user = userRepository.findById(request.getAuthorId()).get();

        //게시글 생성
        Post post = Post.createPost(user, request.getTitle(), request.getContent(), request.getCategory(), request.getFiles());

        //게시글 저장
        postRepository.save(post);
        return post.getId();
    }

    /**
     * 게시글 전체 조회
    **/
//    public Page<PostDto> findPosts(Pageable pageable) {
//        return postRepository.findAll(pageable).map(PostDto::new);
//    }
    public Page<PostDto> findPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostDto::new);
    }

    /**
     * 게시글 개별 조회
    **/
    public Post findOne(Long postId) {return postRepository.findById(postId).get(); }

    /**
     * 게시글 유저별 조회
    **/
    public List<Post> findPostsByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }

    /**
     * 게시물 제목 또는 내용으로 게시물 검색
     **/
    public Page<PostDto> findPostsByTitleOrContent(String searchKeyword, Pageable pageable) {
        return postRepository.findAllByTitleOrContent(searchKeyword, pageable).map(PostDto::new);
    }

    /**
     * 게시글 수정
    **/
    @Transactional
    public Post updatePost(UpdatePostRequest request){
        Post post = postRepository.findById(request.getId()).get();
        for (File file: request.getFiles()) {
            log.info("수정직전요청" + file.getFileName());
        }
        post.change(request.getTitle(), request.getContent(), request.getCategory(), request.getFiles());
        for (File file: post.getFiles()) {
            log.info("수정직전" + file.getFileName());
        }
        return post;
    }

    /**
     * 게시글 삭제
    **/
    @Transactional
    public void deletePost(Long postId) {
        commentRepository.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
    }

//    /**
//     * 게시글 검색
//    **/
//    public Page<Post> searchByKeyword(String keyword, PageRequest pageRequest) {
//        return postRepository.findAllByTitleOrContent(keyword, pageRequest);
//    }

    /**
     * 게시물 방문
    **/
    @Transactional
    public Post updateVisit(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.increaseVisitCount();
        return post;
    }

    /**
     * 게시물 좋아요
    **/
    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.increaseLikeCount();
    }

    /**
     * Amazon S3에 이미지 저장
    **/
    public SmartEditor singleImageUpload(MultipartFile imgFile) {
        LocalDate today = LocalDate.now();

        String fileName = imgFile.getOriginalFilename();
        System.out.println("사진이름");
        System.out.println(fileName);
        String filePath = "static/images/" + String.format("%02", today.getMonthValue()) + "/";
        //나중에 구현
//        String fileUrl = S3IamgeUpload(imgFile, filePath);
        String fileUrl = "s3url";

        SmartEditor smartEditor = new SmartEditor(fileUrl, fileName, "true");

        return smartEditor;
    }

    public Page<PostDto> findPostsByCriteria(PostSearchCondition postSearchCondition, Pageable pageable) {
        return postCustomRepository.findAllByCriteria(postSearchCondition, pageable).map(PostDto::new);
    }
}