package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePostRequest {
    Long authorId;
    Category category;
    String title;
    String content;
}
