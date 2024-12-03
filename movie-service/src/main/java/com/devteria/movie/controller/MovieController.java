package com.devteria.movie.controller;

import com.devteria.movie.dto.ApiResponse;
import com.devteria.movie.dto.PageResponse;
import com.devteria.movie.dto.request.MovieRequest;
import com.devteria.movie.dto.response.MovieResponse;
import com.devteria.movie.service.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieController {
    MovieService movieService;

    @PostMapping("/create")
    ApiResponse<MovieResponse> createMovie(@ModelAttribute MovieRequest request) {
        return ApiResponse.<MovieResponse>builder()
                .result(movieService.createMovie(request))
                .build();
    }

    @GetMapping("/lists")
    ApiResponse<PageResponse<MovieResponse>> getMovies(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false , defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<MovieResponse>>builder()
                .result(movieService.getMovies(page, size))
                .build();
    }

    @PutMapping("/{movieId}")
    ApiResponse<MovieResponse> updateMovie(@PathVariable("movieId") String movieId, @ModelAttribute MovieRequest request){
        return ApiResponse.<MovieResponse>builder()
                .result(movieService.updateMovie(movieId, request))
                .build();
    }

    @DeleteMapping("/{movieId}")
    ApiResponse<String> deleteMovie(@PathVariable("movieId") String movieId){
        movieService.deleteMovie(movieId);
        return ApiResponse.<String>builder()
                .result("Movie has been deleted")
                .build();
    }

    @GetMapping("/recommend")
    ApiResponse<List<MovieResponse>> getRecommend(){
        return ApiResponse.<List<MovieResponse>>builder()
                .result(movieService.getPopuler())
                .build();
    }

}