package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.service.CreatePostRequest;
import jun.moviecommunity.service.PostDto;
import jun.moviecommunity.service.PostService;
import jun.moviecommunity.service.UpdatePostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    /**
     * 게시물 등록 페이지
    **/
    @GetMapping("/posts/new")
    public String createPost(Model model) {

        PostForm form = new PostForm(1L, Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()));
        log.info("확인", "들어옴");
        model.addAttribute("postForm", form);
        return "posts/createPostForm";
    }

    /**
     * 게시물 등록
    **/
    @PostMapping("/posts/new")
    public String create(@Valid PostForm form, BindingResult result) {
        if(result.hasErrors()) {
            return "posts/createPostForm";
        }

        postService.savePost(new CreatePostRequest(
                form.getAuthorId(),
                form.getCategory(),
                form.getTitle(),
                form.getContent()
        ));
        return "redirect:/posts";
    }

    /**
     * 게시물 전체 조회 - 페이징 적용
    **/
    @GetMapping("/posts")
    public String list(Model model, @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("paging", postService.findAll(pageable));
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
    public String updatePost(@ModelAttribute("postForm") PostForm form) {
        postService.updatePost(new UpdatePostRequest(form.getId(), form.getTitle(), form.getContent(), form.getCategory()));
        return "redirect:/posts";
    }
}
