package com.edusn.Digizenger.Demo.profile.utils;

import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.RelationShipDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileMapperUtils {

    private final StorageService storageService;

    public  RelationShipDto convertToRelationShipDto(Profile profile){
        RelationShipDto relationShipDto = new RelationShipDto();
        relationShipDto.setId(profile.getId());
        relationShipDto.setFirstName(profile.getUser().getFirstName());
        relationShipDto.setLastName(profile.getUser().getLastName());
        relationShipDto.setProfileCareer(profile.getProfileCareer());
        relationShipDto.setUsername(profile.getUsername());
        relationShipDto.setProfileLinkUrl(profile.getProfileLinkUrl());

        if(profile.getProfileImageName() != null){
            relationShipDto.setProfileImageName(profile.getProfileImageName());
            relationShipDto.setProfileImageUrl(storageService.getImageByName(profile.getProfileImageName()));
        }
        if(profile.getCoverImageName() != null) {
            relationShipDto.setCoverImageName(profile.getCoverImageName());
            relationShipDto.setCoverImageUrl(storageService.getImageByName(profile.getCoverImageName()));
        }
        relationShipDto.setFollowersCount(Long.valueOf(profile.getFollowers().size()));
        relationShipDto.setFollowingCount(Long.valueOf(profile.getFollowing().size()));
        relationShipDto.setNeighborsCount(Long.valueOf(profile.getNeighbors().size()));

        return relationShipDto;

    }
}
