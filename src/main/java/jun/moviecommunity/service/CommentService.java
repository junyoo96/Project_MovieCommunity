package jun.moviecommunity.service;

import jun.moviecommunity.domain.Comment;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.CommentRepository;
import jun.moviecommunity.repository.PostRepository;
import jun.moviecommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 등록
    **/
    @Transactional
    public Long saveComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findOne(postId);

        Comment comment = Comment.createComment(user, post, content);
        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * 댓글 개별 조회
     **/
    public Comment findOne(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    /**
     * 게시글별 댓글 조회
    **/
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    /**
     * 유저별 댓글 조회
    **/
    public List<Comment> findCommentsByUserId(Long userId) {
        return commentRepository.findAllByUserId(userId);
    }

    /**
     * 댓글 수정
    **/
    @Transactional
    public Comment updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findOne(commentId);
        comment.change(content);
        return comment;
    }

    /**
     * 댓글 삭제
    **/
    @Transactional
    public void deleteComment(Long commentId) {commentRepository.delete(commentId); }

    /**
     * 대댓글 등록
    **/

}
