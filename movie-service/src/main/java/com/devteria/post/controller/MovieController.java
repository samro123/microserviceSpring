package com.devteria.post.controller;

import com.devteria.post.dto.ApiResponse;
import com.devteria.post.dto.request.MovieRequest;
import com.devteria.post.dto.response.MovieResponse;
import com.devteria.post.service.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieController {
    MovieService movieService;

    @PostMapping("/create")
    ApiResponse<MovieResponse> createMovie(@RequestBody MovieRequest request){
        return ApiResponse.<MovieResponse>builder()
                .result(movieService.createMovie(request))
                .build();
    }

    @GetMapping("/lists")
    ApiResponse<List<MovieResponse>> getMovies(){
        return ApiResponse.<List<MovieResponse>>builder()
                .result(movieService.getMovies())
                .build();
    }

    @PutMapping("/{movieId}")
    ApiResponse<MovieResponse> updateMovie(@PathVariable("movieId") String movieId, @RequestBody MovieRequest request){
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

}