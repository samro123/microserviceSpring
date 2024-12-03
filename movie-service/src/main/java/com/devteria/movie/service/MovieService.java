package com.devteria.movie.service;

import com.devteria.movie.dto.PageResponse;
import com.devteria.movie.dto.request.MovieRequest;
import com.devteria.movie.dto.response.MovieRecommendAIResponse;
import com.devteria.movie.dto.response.MovieResponse;
import com.devteria.movie.entity.Movie;
import com.devteria.movie.exception.AppException;
import com.devteria.movie.exception.ErrorCode;
import com.devteria.movie.mapper.MovieMapper;
import com.devteria.movie.repository.MovieRepository;
import com.devteria.movie.repository.httpClient.MovieRecommendAIClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {
    MovieRepository movieRepository;
    MovieMapper movieMapper;
    MovieRecommendAIClient movieRecommendAIClient;
    UploadVideo uploadVideo;

    public MovieResponse createMovie(MovieRequest request){
        Movie movie = movieMapper.toMovie(request);
        movie.setCreatedDate(Instant.now());
        movie.setModifiedDate(Instant.now());
        movie.setVideoUrl(uploadVideo.uploadVideo(request.getVideoUrl()));
        movie.setPosterUrl(uploadVideo.uploadImage(request.getPosterUrl()));
        movie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(movie);
    }

    public MovieResponse updateMovie(String id, MovieRequest request) {
        Movie movie = movieRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.MOVIE_EXISTED));
        movie.setModifiedDate(Instant.now());

            movie.setVideoUrl(uploadVideo.uploadVideo(request.getVideoUrl()));


            String newVideoUrl = uploadVideo.uploadImage(request.getPosterUrl());
            movie.setPosterUrl(newVideoUrl);


        movieMapper.updateMovie(movie, request);
        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }

    public void deleteMovie(String id){
        movieRepository.deleteById(id);
    }

    public PageResponse<MovieResponse> getMovies(int page, int size){
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page -1, size, sort);
        var pageData = movieRepository.findAll(pageable);
        return PageResponse.<MovieResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(movieMapper::toMovieResponse).toList())
                .build();
    }

    public MovieResponse getMovie(String id){
        Movie movie = movieRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.MOVIE_EXISTED));
        movie.setModifiedDate(Instant.now());
        return movieMapper.toMovieResponse(movie);
    }
    public String getMovieAI(){
        List<MovieRecommendAIResponse> movieRecommendAIResponses = null;
        try {
            movieRecommendAIResponses =  movieRecommendAIClient.getMovieAI().getResult();
            for(MovieRecommendAIResponse index: movieRecommendAIResponses){
                log.info(index.getMovieId());
            }
        }catch (Exception e){
            log.error("Error while getting movie AI", e);
        }
        return "AI";
    }
    public  List<MovieResponse> getPopuler(){
        List<MovieRecommendAIResponse> movieRecommendAIResponses = null;
        try {
            movieRecommendAIResponses =  movieRecommendAIClient.getMovieAI().getResult();
            for(MovieRecommendAIResponse index: movieRecommendAIResponses){
                log.info(index.getMovieId());
            }
        }catch (Exception e){
            log.error("Error while getting movie AI", e);
        }
        List<String> movieIds = movieRecommendAIResponses.stream()
                .map(MovieRecommendAIResponse::getMovieId)
                .collect(Collectors.toList());

        return movieRepository.findByIdIn(movieIds).stream().map(movieMapper::toMovieResponse).toList();
    }


}
