package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.Profile;
import com.visitinglecturer.vlms_backend.entity.QualificationVisitingLecturer;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.payload.response.QualificationResponse;
import com.visitinglecturer.vlms_backend.payload.response.RecommendationResponse;
import com.visitinglecturer.vlms_backend.repository.ProfileRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public RecommendationResponse getLecturerDetails(String nicNumber) {
        Optional<Profile> optionalProfile = profileRepository.findByNicNumber(nicNumber);

        if (optionalProfile.isEmpty()) {
            throw new RuntimeException("No lecturer found with NIC: " + nicNumber);
        }

        Profile profile = optionalProfile.get();

        Optional<User> optionalUser = userRepository.findByProfile(profile);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("No associated User found for NIC: " + nicNumber);
        }

        User user = optionalUser.get();

        if(user.getRoles().isEmpty()){
            throw new RuntimeException("No Roles  are found for NIC: " + nicNumber + ", Please update relevant tables");
        }

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        boolean isVisitingLecturer = user.getRoles().stream()
                .anyMatch(role -> "ROLE_VISITING_LECTURER".equals(role.getName()));

        RecommendationResponse response = new RecommendationResponse();

        if(isVisitingLecturer){
            response.setRole("ROLE_VISITING_LECTURER");
            response.setFullName(profile.getFullName());
            response.setEmail(profile.getEmail());
            response.setNicNumber(profile.getNicNumber());

            VisitingLecturer visitingLecturer = user.getVisitingLecturer();
            if (visitingLecturer != null) {
                response.setPhoneNumber(visitingLecturer.getPhoneNumber());
                response.setPrivateAddress(visitingLecturer.getPrivateAddress());
                response.setPresentEmployment(visitingLecturer.getPresentEmployment());
                response.setDepartment(visitingLecturer.getDepartment());

                // Fetch qualifications and map to QualificationResponse
                List<QualificationResponse> qualifications = visitingLecturer.getQualificationVisitingLecturers()
                        .stream()
                        .map(qvl -> new QualificationResponse(
                                qvl.getQualification().getTitle(),
                                qvl.getQualification().getLevel(),
                                qvl.getQualification().getUniversity()
                        ))
                        .collect(Collectors.toList());

                response.setQualifications(qualifications);

            }

            return response;
        }

        if (isAdmin) {
            response.setRole("ROLE_ADMIN");
        }

        /*if (!isVisitingLecturer) {
            throw new RuntimeException("User is Not a Visiting Lecturer for NIC: " + nicNumber);
        }*/

        return response;


    }
}






