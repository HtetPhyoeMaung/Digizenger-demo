package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Profile findByUser(User user);

    Boolean existsByUsername(String username);

    Optional<Profile> findByUsername(String username);

    @Query("SELECT p.followers FROM Profile p WHERE p.id = :profileId")
    Page<Profile> findFollowersByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT p.following FROM Profile p WHERE p.id = :profileId")
    Page<Profile> findFollowingByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT p.neighbors FROM Profile p WHERE p.id = :profileId")
    Page<Profile> findNeighborsByProfileId(Long profileId, Pageable pageable);

    @Query("""
    SELECT u 
    FROM Profile u
    LEFT JOIN u.neighbors n ON n.id = :currentUserId
    LEFT JOIN u.followers f ON f.id = :currentUserId
    LEFT JOIN u.following fl ON fl.id = :currentUserId
    WHERE u.id != :currentUserId
    ORDER BY CASE 
                WHEN n.id IS NOT NULL THEN 1
                WHEN fl.id IS NOT NULL THEN 2
                WHEN f.id IS NOT NULL THEN 3
                ELSE 3
             END
    """)
    Page<Profile> findPrioritizedProfile(Long currentUserId, Pageable pageable);


//

}
