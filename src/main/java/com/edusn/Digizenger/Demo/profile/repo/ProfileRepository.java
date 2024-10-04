package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Profile findByUser(User user);

    Profile findByProfileLinkUrl(String profileUrl);

    @Query("SELECT p.followers FROM Profile p WHERE p.id = :profileId")
    Page<Profile> findFollowersByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT p.following FROM Profile p WHERE p.id = :profileId")
    Page<Profile> findFollowingByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT p.neighbors FROM Profile p WHERE p.id = :profileId")
    Page<Profile> findNeighborsByProfileId(Long profileId, Pageable pageable);
}
