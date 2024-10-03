package com.devteria.post.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Builder
@Document(value = "post")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {
    @MongoId
    String id;
    String title;
    String category;
    String description;
    String trailerUrl;
    String videoUrl;
    String posterUrl;
    Instant createdDate;
    Instant modifiedDate;
}
