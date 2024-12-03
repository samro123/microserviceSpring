package com.devteria.movie.controller;

import com.devteria.movie.dto.ApiResponse;
import com.devteria.movie.dto.PageResponse;
import com.devteria.movie.dto.request.PostRequest;
import com.devteria.movie.dto.response.PostResponse;
import com.devteria.movie.service.PostService;
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
public class PostController {
    PostService postService;

    @PostMapping("/create")
    ApiResponse<PostResponse> createPost(@RequestBody PostRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @PostMapping("/comment/{movieId}")
    ApiResponse<PostResponse> createComment(
            @PathVariable("movieId") String movieId,
            @RequestBody PostRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(postService.createComment(movieId, request))
                .build();
    }

    @GetMapping("/my-posts")
    ApiResponse<PageResponse<PostResponse>> myPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getMyPosts(page, size))
                .build();
    }

    @GetMapping("/comment/{movieId}")
    ApiResponse<List<PostResponse>> myComments(@PathVariable("movieId") String movieId){
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getComments(movieId))
                .build();
    }


}