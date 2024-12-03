package com.devteria.movie.repository;

import com.devteria.movie.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<File, String> {
}
