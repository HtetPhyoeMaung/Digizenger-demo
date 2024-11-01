package com.edusn.Digizenger.Demo.utilis;

import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CommonUtil {

    @Autowired
    private StorageService storageService;

    public List<ProfileDto> getFlickDtoListFromPost(Post post){
        List<ProfileDto> flickUserDtoList = new LinkedList<>();
        post.getFlicks().forEach(flick -> {
            Profile flickUser = flick.getUser().getProfile();
            ProfileDto flickUserDto = new ProfileDto();
            flickUserDto.setId(flickUser.getId());
            flickUserDto.setProfileImageUrl( flickUser.getProfileImageName()!=null?
                    storageService.getImageByName(flickUser.getProfileImageName()):"");
            flickUserDto.setUsername(flickUser.getUsername());

            flickUserDtoList.add(flickUserDto);

        });
        return flickUserDtoList;
    }
}
