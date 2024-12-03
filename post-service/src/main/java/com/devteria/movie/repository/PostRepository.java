package com.devteria.movie.repository;

import com.devteria.movie.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    //tim theo userId
    Page<Post> findAllByUserId(String userId, Pageable pageable);
    List<Post> findAllByMovieId(String movieId);
}
