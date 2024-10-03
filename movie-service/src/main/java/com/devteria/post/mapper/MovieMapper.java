package com.devteria.post.mapper;

import com.devteria.post.dto.request.MovieRequest;
import com.devteria.post.dto.response.MovieResponse;
import com.devteria.post.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toMovie(MovieRequest request);
    MovieResponse toMovieResponse(Movie movie);

    void updateUser(@MappingTarget Movie movie, MovieRequest request);
}
