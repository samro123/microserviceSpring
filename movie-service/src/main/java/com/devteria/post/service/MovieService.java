package com.devteria.post.service;

import com.devteria.post.dto.request.MovieRequest;
import com.devteria.post.dto.response.MovieResponse;
import com.devteria.post.entity.Movie;
import com.devteria.post.exception.AppException;
import com.devteria.post.exception.ErrorCode;
import com.devteria.post.mapper.MovieMapper;
import com.devteria.post.repository.MovieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {
    MovieRepository movieRepository;
    MovieMapper movieMapper;

    public MovieResponse createMovie(MovieRequest request){
        Movie movie = movieMapper.toMovie(request);
        movie.setCreatedDate(Instant.now());
        movie.setModifiedDate(Instant.now());
        movie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(movie);
    }

    public MovieResponse updateMovie(String id, MovieRequest request){
        Movie movie = movieRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.MOVIE_EXISTED));
        movie.setModifiedDate(Instant.now());
        movieMapper.updateUser(movie, request);
        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }

    public void deleteMovie(String id){
        movieRepository.deleteById(id);
    }

    public List<MovieResponse> getMovies(){
        return movieRepository.findAll().stream()
                .map(movieMapper::toMovieResponse).toList();
    }

}
