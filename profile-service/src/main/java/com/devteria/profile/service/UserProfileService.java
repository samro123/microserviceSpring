package com.devteria.profile.service;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(UserProfileCreationRequest request){
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getByUserId(String userId){
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id){
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles(){
        var profile =userProfileRepository.findAll();
        return profile.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }

    public UserProfileResponse getMyProfile(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userProfileMapper.toUserProfileResponse(profile);
    }
}
