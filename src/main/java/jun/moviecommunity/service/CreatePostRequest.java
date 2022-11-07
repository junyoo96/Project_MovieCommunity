package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePostRequest {
    private Long authorId;
    private Category category;
    private String title;
    private String content;
//    private List<String> fileUrlPaths;
}
