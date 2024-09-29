package com.edusn.Digizenger.Demo.post.service.impl;

import com.edusn.Digizenger.Demo.post.entity.Media;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.repo.MediaRepository;
import com.edusn.Digizenger.Demo.post.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private MediaRepository mediaRepository;
    @Override
    public List<Media> updateMedia(Post post, List<Media> requestPostMedia) {
        List<Media> updatedMediaList = new ArrayList<>();
        Media newMedia = new Media();
        for (Media requestMedia : requestPostMedia) {

            post.getMedia().forEach(existMedia -> {
                if (existMedia.getId().equals(requestMedia.getId())) {
                    // modify exist media
                    existMedia.setId(requestMedia.getId());
                    existMedia.setMediaUrl(requestMedia.getMediaUrl());
                    existMedia.setMediaType(requestMedia.getMediaType());
                    updatedMediaList.add(existMedia);
                } else if (requestMedia.getId()==null) {
                    // add new media
                    newMedia.setPost(post);
                    newMedia.setMediaType(requestMedia.getMediaType());
                    newMedia.setMediaUrl(requestMedia.getMediaUrl());
                }
            });
        }
        updatedMediaList.add(newMedia);

        return updatedMediaList;
    }

}
