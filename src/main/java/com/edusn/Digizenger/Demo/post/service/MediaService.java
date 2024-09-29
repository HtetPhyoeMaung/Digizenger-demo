package com.edusn.Digizenger.Demo.post.service;


import com.edusn.Digizenger.Demo.post.entity.Media;
import com.edusn.Digizenger.Demo.post.entity.Post;

import java.util.List;

public interface MediaService {


    List<Media> updateMedia(Post post, List<Media> media);
}
