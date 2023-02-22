package jun.moviecommunity.service;

import jun.moviecommunity.domain.Comment;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.domain.User;
import jun.moviecommunity.repository.CommentCustomRepository;
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
    private final CommentCustomRepository commentCustomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 등록
    **/
    @Transactional
    public Long saveComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();

        Comment comment = Comment.createComment(user, post, content);
        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * 댓글 개별 조회
     **/
    public Comment findOne(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    /**
     * 게시글별 댓글 조회
    **/
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentCustomRepository.findAllByPostId(postId);
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
        Comment comment = commentRepository.findById(commentId).get();

        //삭제된 댓글이 아닌지 확인
        if (comment.getIsDeleted() == Comment.Status.REMAIN) {
            comment.update(content);
        }

        return comment;
    }

    /**
     * 댓글 삭제
    **/
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        //만약 대댓글이 있다면
        if(comment.getChildren().size() != 0) {
            comment.updateDeletedStatus(Comment.Status.DELETED);
            comment.update("삭제된 댓글입니다.");
        }
        else {
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    /**
     * 삭제 가능한 조상 댓글을 구하는 함수
    **/
    private Comment getDeletableAncestorComment(Comment comment) {
        // 현재 댓글의 부모를 구함
        Comment parent = comment.getParent();
        // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == Comment.Status.DELETED) {
            return getDeletableAncestorComment(parent);
        }
        // 삭제해야하는 댓글 반환
        return comment;
    }

    /**
     * 댓글 좋아요
    **/
    @Transactional
    public void like(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getIsDeleted() == Comment.Status.REMAIN) {
            comment.increaseLikeCount();
        }
    }

    /**
     * 대댓글 등록
     **/
    @Transactional
    public Long saveReply(Long userId, Long postId, Long commentId, String content) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        Comment comment = commentRepository.findById(commentId).get();

        Comment reply = Comment.createReply(user, post, comment, content);
        commentRepository.save(reply);
        return reply.getId();
    }
}
