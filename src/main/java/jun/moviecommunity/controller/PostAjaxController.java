package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.service.PostDto;
import jun.moviecommunity.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostAjaxController {

    private final PostService postService;
    private final int pagingSize = 5;

    /**
     * 게시물 전체 조회 - 페이징 적용 - ajax 적용
     **/
    @GetMapping("/posts/list")
    public Map<String, Object> list(@PageableDefault(size = pagingSize, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword, Category category) {

        Page<PostDto> pagingList = null;

        log.info("확인확인 {} {}", pageable.getPageSize(), pageable.getSort());

        if ((searchKeyword == null || searchKeyword.equals("null")) && category == null) {
            log.info("그냥 검색");
            pagingList = postService.findPosts(pageable);
        }
        else {
            log.info("검색 {} {}", searchKeyword, category);
//            pagingList = postService.findPostsByTitleOrContent(searchKeyword, pageable);
            pagingList = postService.findPostsByCriteria(new PostSearchCondition(searchKeyword, category), pageable);
        }

        String sortValue = pagingList.getSort().toString().replace(": ", ",");
        List<String> categories = Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList());

//        model.addAttribute("paging", pagingList);
//        model.addAttribute("searchKeyword", searchKeyword); //검색어
//        model.addAttribute("sortValue", sortValue); //정렬 기준
//        model.addAttribute("categories", categories); //카테고리

        Map<String, Object> result = new HashMap<>();
        result.put("paging", pagingList);
        result.put("sortValue", sortValue);
        result.put("searchKeyword", searchKeyword);
//        result.put("")

        return result;
    }


}
