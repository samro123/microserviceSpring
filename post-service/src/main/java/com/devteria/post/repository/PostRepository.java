package com.devteria.post.repository;

import com.devteria.post.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    //tim theo userId
    List<Post> findAllByUserId(String userId);
}
