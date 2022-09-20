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
    public Long save(Long userId, Long postId, String content) {
        User user = userRepository.findOne(userId);
        Post post = postRepository.findOne(postId);

        Comment comment = Comment.createComment(user, post, content);
        commentRepository.save(comment);
        return comment.getId();
    }

}
