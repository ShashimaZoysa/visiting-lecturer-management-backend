package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.Profile;
import com.visitinglecturer.vlms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByProfile(Profile profile);

    Optional<User> findByProfile_NicNumber(String nicNumber);

    //Optional<User> findByProfile_NicNumber(String nicNumber);


}
