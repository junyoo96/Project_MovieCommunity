package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PostForm {

    private Long id;
    private Long authorId;
    private Category category;
    private List<String> categories;
    private String title;
    private String content;

    public PostForm() {

    }

    public PostForm(Long authorId, List<String> categories) {
        this.authorId = authorId;
        this.categories = categories;
    }

    public PostForm(Long id, Long authorId, Category category, List<String> categories, String title, String content) {
        this.id = id;
        this.authorId = authorId;
        this.category = category;
        this.categories = categories;
        this.title = title;
        this.content = content;
    }
}
