package com.devteria.movie.service;

import com.devteria.movie.dto.PageResponse;
import com.devteria.movie.dto.request.PostRequest;
import com.devteria.movie.dto.response.PostResponse;
import com.devteria.movie.dto.response.UserProfileResponse;
import com.devteria.movie.entity.Post;
import com.devteria.movie.mapper.PostMapper;
import com.devteria.movie.repository.PostRepository;
import com.devteria.movie.repository.httpClient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    DateTimeFormatter dateTimeFormatter;
    PostRepository postRepository;
    PostMapper postMapper;
    ProfileClient profileClient;

    public PostResponse createPost(PostRequest request){
       // get UserId trong token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Post post = Post.builder()
                .content(request.getContent())
                .userId(authentication.getName())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PostResponse createComment(String movieId, PostRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("MovieID: {}", movieId);
        Post post = Post.builder()
                .content(request.getContent())
                .movieId(movieId)
                .userId(authentication.getName())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PageResponse<PostResponse> getMyPosts(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserProfileResponse userProfile = null;
        try {
            userProfile = profileClient.getProfile(userId).getResult();
        }catch (Exception e){
            log.error("Error while getting user profile", e);
        }


        //fiter trong post Object
        Sort sort = Sort.by("createdDate").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByUserId(userId, pageable);
        String username = userProfile !=null ? userProfile.getUsername() : null;
        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            postResponse.setUsername(username);
            return postResponse;
        }).toList();
        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }

    public List<PostResponse> getComments(String movieId){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//        UserProfileResponse userProfile = null;
//        try {
//            userProfile = profileClient.getProfile(userId).getResult();
//        }catch (Exception e){
//            log.error("Error while getting user profile", e);
//        }
//        //fiter trong post Object
//        Sort sort = Sort.by("createdDate").descending();
//        String username = userProfile !=null ? userProfile.getUsername() : null;
//        return postRepository.findAllByMovieId(movieId).stream().map(post -> {
//            var postResponse = postMapper.toPostResponse(post);
//            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
//            postResponse.setUsername(username);
//            return postResponse;
//        }).toList();
        List<Post> posts = postRepository.findAll();
        Map<String, UserProfileResponse> userProfileResponses = new HashMap<>();
        posts.stream()
                .map(Post::getUserId)
                .distinct()
                .forEach(userId ->{
                    try{
                        UserProfileResponse userProfileResponse = profileClient.getProfile(userId).getResult();
                        userProfileResponses.put(userId, userProfileResponse);
                    }catch (Exception e){
                        log.error("Error while getting user profile for userId: " + userId, e);
                    }
                });
        return postRepository.findAllByMovieId(movieId).stream()
                .map(post -> {
                    var postResponse = postMapper.toPostResponse(post);
                    postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
                    UserProfileResponse userProfile = userProfileResponses.get(post.getUserId());
                    String username = (userProfile != null) ? userProfile.getUsername() : null;
                    postResponse.setUsername(username);
                    return postResponse;
                }).toList();

    }



}
