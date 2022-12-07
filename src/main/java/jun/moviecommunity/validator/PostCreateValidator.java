package jun.moviecommunity.validator;

import jun.moviecommunity.controller.PostForm;
import jun.moviecommunity.domain.Post;
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
public class PostCreateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Post.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("게시글 작성 검사");

        PostForm form = (PostForm) target;
        BindingResult bindingResult = (BindingResult) errors;

        validateContentBlank(form, bindingResult);
    }

    public void validateContentBlank(PostForm form, BindingResult bindingResult) {
        String contentBlankValue = "<p>&nbsp;</p>";

        //게시글 내용 비어있는지 검사
    if (form.getContent().equals(contentBlankValue)) {
            log.info("게시물 내용 비어있음");
            bindingResult.addError(new FieldError("postForm",
                    "content",
                    form.getContent(),
                    false,
                    null,
                    null,
                    "내용을 입력해주세요"));
        }
    }
}
