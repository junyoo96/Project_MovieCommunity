package jun.moviecommunity.service;

import jun.moviecommunity.service.api.MovieApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieApiClient movieApiClient;

    @Transactional(readOnly = true)
    public MovieResponseDto findByKeyword(String keyword) {
        return movieApiClient.requestMovie(keyword);

    }
}
