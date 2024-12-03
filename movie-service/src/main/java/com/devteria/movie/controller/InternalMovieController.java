package com.devteria.movie.controller;

import com.devteria.movie.dto.ApiResponse;
import com.devteria.movie.dto.response.MovieResponse;
import com.devteria.movie.service.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalMovieController {
    MovieService movieService;
    @GetMapping("/internal/{movieId}")
    ApiResponse<MovieResponse> getMovie(@PathVariable String movieId){
        return ApiResponse.<MovieResponse>builder()
                .result(movieService.getMovie(movieId))
                .build();

    }
}
