package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Comment;
import jun.moviecommunity.service.CommentDto;
import jun.moviecommunity.service.CommentService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

//참고 : https://devofroad.tistory.com/59
//https://memostack.tistory.com/245

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
    **/
    @PostMapping("/comments/new")
    public ResponseEntity create(@RequestBody @Valid CreateCommentRequest createCommentRequest, BindingResult result) {

        //댓글 빈칸인 경우
        if(result.hasErrors()) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }

        //댓글 저장
        commentService.saveComment(createCommentRequest.getUserId(), createCommentRequest.getPostId(), createCommentRequest.getContent());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 댓글 조회
    **/
    @GetMapping("/comments/list/{postId}")
    public List<CommentDto> list(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentDto dto = new CommentDto(c);
            map.put(dto.getId(), dto);
            if (c.getParent()!=null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });

        return result;
    }

    /**
     * 댓글 수정
    **/
    @PutMapping("/comments/{commentId}/edit")
    public ResponseEntity edit(@PathVariable("commentId") Long id, @RequestBody @Valid UpdateCommentRequest request, BindingResult result) {
        //댓글 내용이 비어있을 경우
        if(result.hasErrors()) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }

        //댓글 수정
        commentService.updateComment(id, request.getContent());
        Comment findComment = commentService.findOne(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 댓글 삭제
    **/
    @DeleteMapping("/comments/{commentId}/delete")
    public ResponseEntity delete(@PathVariable("commentId") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 댓글 좋아요
    **/
    @PutMapping("/comments/{commentId}/like")
    private ResponseEntity like(@PathVariable("commentId") Long commentId) {
        commentService.like(commentId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 댓글 등록 DTO
    **/
    @Data
    static class CreateCommentRequest {
        private Long postId;
        private Long userId;

        @NotBlank
        private String content;
    }

    /**
     * 댓글 수정 DTO
    **/
    @Data
    static class UpdateCommentRequest {
        @NotBlank
        private String content;
    }

    /**
     * 대댓글 등록
    **/
    @PostMapping("/comments/reply/new")
    public ResponseEntity createReply(@RequestBody @Valid CreateReplyRequest createReplyRequest, BindingResult result) {
        //댓글 내용이 비어있을 경우
        if(result.hasErrors()) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }

        //대댓글 저장
        commentService.saveReply(createReplyRequest.getUserId(), createReplyRequest.getPostId(), createReplyRequest.getCommentId(), createReplyRequest.getContent());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 대댓글 등록 DTO
    **/
    @Data
    static class CreateReplyRequest {
        private Long userId;
        private Long postId;
        private Long commentId;

        @NotBlank
        private String content;
    }
}