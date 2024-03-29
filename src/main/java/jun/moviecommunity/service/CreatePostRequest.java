package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePostRequest {
    Long authorId;
    Category category;
    String title;
    String content;
    List<File> files;
}
