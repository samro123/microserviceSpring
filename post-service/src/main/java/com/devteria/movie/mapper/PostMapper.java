package com.devteria.movie.mapper;

import com.devteria.movie.dto.response.PostResponse;
import com.devteria.movie.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
