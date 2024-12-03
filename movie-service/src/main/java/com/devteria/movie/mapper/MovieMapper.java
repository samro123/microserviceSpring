package com.devteria.movie.mapper;

import com.devteria.movie.dto.request.MovieRequest;
import com.devteria.movie.dto.response.MovieResponse;
import com.devteria.movie.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MovieMapper {
    @Mapping(source = "videoUrl", target = "videoUrl")
    @Mapping(source = "posterUrl", target = "posterUrl")
    Movie toMovie(MovieRequest request);

    MovieResponse toMovieResponse(Movie movie);

    @Mapping(source = "videoUrl", target = "videoUrl")
    @Mapping(source = "posterUrl", target = "posterUrl")
    void updateMovie(@MappingTarget Movie movie, MovieRequest request);

    default String map(MultipartFile file) {
        return file != null ? file.getOriginalFilename() : null;
    }
}
