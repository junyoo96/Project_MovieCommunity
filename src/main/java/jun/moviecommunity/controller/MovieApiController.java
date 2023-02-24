package jun.moviecommunity.controller;

import jun.moviecommunity.service.MovieResponseDto;
import jun.moviecommunity.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MovieApiController {
    private final MovieService movieService;

    @GetMapping("/api/movies")
    public MovieResponseDto findMovies(String keyword) {
        return movieService.findByKeyword(keyword);
    }
}
