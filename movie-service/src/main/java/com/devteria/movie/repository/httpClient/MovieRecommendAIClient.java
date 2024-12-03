package com.devteria.movie.repository.httpClient;

import com.devteria.movie.dto.ApiResponse;
import com.devteria.movie.dto.response.MovieRecommendAIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "movie-ai-service", url = "${app.services.movie-ai.url}")
public interface MovieRecommendAIClient {
    @GetMapping("/recommend")
    ApiResponse<List<MovieRecommendAIResponse>> getMovieAI();
}
