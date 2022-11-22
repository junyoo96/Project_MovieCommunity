package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.service.CreatePostRequest;
import jun.moviecommunity.service.PostDto;
import jun.moviecommunity.service.PostService;
import jun.moviecommunity.service.UpdatePostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final int pagingSize = 2;

    /**
     * 게시물 등록 페이지
    **/
    @GetMapping("/posts/new")
    public String createPost(Model model) {
        PostForm form = new PostForm(1L, Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()));
        model.addAttribute("postForm", form);
        return "posts/createPostForm";
    }

    /**
     * 게시물 등록
    **/
    @PostMapping("/posts/new")
    public String create(PostForm form, BindingResult result) {
//        if(result.hasErrors()) {
//            return "posts/createPostForm";
//        }

        postService.savePost(new CreatePostRequest(
                form.getAuthorId(),
                form.getCategory(),
                form.getTitle(),
                form.getContent()
        ));

        return "redirect:/posts";
    }

//    /**
//     * 게시물 등록
//     **/
//    @PostMapping("/posts/new")
//    public ResponseEntity create(@RequestBody CreatePostRequest createPostRequest) {
//        postService.savePost(createPostRequest);
//        return ResponseEntity.ok(HttpStatus.OK);
////        return "redirect:/posts";
//    }

    /**
     * 게시물 전체 조회 - 페이징 적용
    **/
    @GetMapping("/posts")
    public String list(Model model, @PageableDefault(size = pagingSize, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword) {

        Page<PostDto> list = null;

        if (searchKeyword == null) {
            list = postService.findAll(pageable);
        }
        else {
            list = postService.findAllByTitleOrContent(searchKeyword, pageable);
        }

        model.addAttribute("paging", list);
        return "posts/postList";
    }

    /**
     * 게시물 상세 조회
    **/
    @GetMapping("/posts/{postId}")
    public String findPost(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.updateVisit(postId);
        model.addAttribute("post", new PostDto(post));
        return "posts/post";
    }

    /**
     * 게시물 수정 페이지
    **/
    @GetMapping("/posts/{postId}/edit")
    public String updatePostForm(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.findOne(postId);

        PostForm form = new PostForm(
                post.getId(),
                post.getAuthor().getId(),
                post.getCategory(),
                Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()),
                post.getTitle(),
                post.getContent()
        );

        model.addAttribute("postForm", form);
        return "posts/updatePostForm";
    }

    /**
     * 게시물 수정
    **/
    @PostMapping("/posts/{postId}/edit")
    public String update(@ModelAttribute("postForm") PostForm form) {
        postService.updatePost(new UpdatePostRequest(form.getId(), form.getTitle(), form.getContent(), form.getCategory()));
        return "redirect:/posts";
    }

    /**
     * 게시물 삭제
    **/
    @GetMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }

    /**
     * 게시물 좋아요
    **/
    //TODO 나중에 User id 받아서 좋아요한 user가 누군지 저장하는거 하기
    @PutMapping("/posts/{postId}/like")
    private ResponseEntity like(@PathVariable("postId") Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
