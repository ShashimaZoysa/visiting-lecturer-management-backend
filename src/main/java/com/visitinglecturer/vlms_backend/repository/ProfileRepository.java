package com.visitinglecturer.vlms_backend.repository;

import com.visitinglecturer.vlms_backend.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Add a method to find Profile by NIC number
    Optional<Profile> findByNicNumber(String nicNumber);

}

