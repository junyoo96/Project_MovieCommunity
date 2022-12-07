package jun.moviecommunity.validator;

import jun.moviecommunity.controller.PostForm;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostUpdateValidator implements Validator {

    private final PostService postService;
    private final PostCreateValidator postCreateValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return Post.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("게시물 수정 검사");

        PostForm form = (PostForm) target;
        BindingResult bindingResult = (BindingResult) errors;
        Post post = postService.findOne(form.getId());

        if(form.getCategory() != null) {
            if (form.getCategory().equals(post.getCategory()) && form.getTitle().equals(post.getTitle()) && form.getContent().equals(post.getContent())) {
                bindingResult.addError(new FieldError("postForm",
                        "isSame",
                        "변경 내용이 없습니다"));
            }
        }

        //게시물 내용 비어있는지 검사
        postCreateValidator.validateContentBlank(form, bindingResult);
    }
}
