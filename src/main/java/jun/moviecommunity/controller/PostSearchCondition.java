package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PostSearchCondition {
    String keyword;
    Category category;
}
