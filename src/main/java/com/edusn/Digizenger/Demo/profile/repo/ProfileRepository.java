package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Profile findByUser(User user);

    Profile findByProfileLinkUrl(String profileUrl);
}
