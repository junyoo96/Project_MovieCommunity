package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.File;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostForm {

    private Long id;
    private Long authorId;

    @NotNull(message = "카테고리를 선택해주세요")
    private Category category;
    private List<String> categories;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    //게시글 수정했는지 여부 확인
    private boolean isSame;

    public PostForm(List<String> categories) {
        this.categories = categories;
    }

    public boolean getIsSame() {
        return isSame;
    }

    public void setIsSame(boolean isSame) {
        isSame = isSame;
    }
}
