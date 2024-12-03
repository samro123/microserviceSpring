package com.devteria.movie.repository;

import com.devteria.movie.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByIdIn(List<String> movieIds);
}
