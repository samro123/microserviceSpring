package com.devteria.notification.mapper;

import com.devteria.notification.dto.request.UserProfileCreationRequest;
import com.devteria.notification.dto.response.UserProfileResponse;
import com.devteria.notification.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")// component bao cho  mapstruct biet day 1 cai bean
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreationRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile entity);
}
