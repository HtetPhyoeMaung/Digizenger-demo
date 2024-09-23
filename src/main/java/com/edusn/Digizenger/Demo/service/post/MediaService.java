package com.edusn.Digizenger.Demo.service.post;


import com.edusn.Digizenger.Demo.entity.post.Media;
import com.edusn.Digizenger.Demo.entity.post.Post;

import java.util.List;

public interface MediaService {


    List<Media> updateMedia(Post post, List<Media> media);
}
