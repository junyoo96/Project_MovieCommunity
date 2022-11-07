package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePostRequest {
    private Long id;
    private String title;
    private String content;
    private Category category;
}
