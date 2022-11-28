package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostForm {

    private Long id;
    private Long authorId;

    private Category category;
    private List<String> categories;
    private String title;
    private String content;

    public PostForm(Long authorId, List<String> categories) {
        this.authorId = authorId;
        this.categories = categories;
    }
}
