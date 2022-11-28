package jun.moviecommunity.service;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdatePostRequest {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private List<File> files;
}
