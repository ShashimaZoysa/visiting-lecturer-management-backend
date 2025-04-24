package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.Profile;
import com.visitinglecturer.vlms_backend.entity.RecommendationForm;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.payload.response.VisitingLecturerDetailsResponse;
import com.visitinglecturer.vlms_backend.repository.ProfileRepository;
import com.visitinglecturer.vlms_backend.repository.RecommendationFormRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import com.visitinglecturer.vlms_backend.service.VisitingLecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitingLecturerServiceImpl implements VisitingLecturerService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RecommendationFormRepository recommendationFormRepository;

    @Autowired
    public VisitingLecturerServiceImpl(UserRepository userRepository,
                                       ProfileRepository profileRepository,
                                       RecommendationFormRepository recommendationFormRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.recommendationFormRepository = recommendationFormRepository;
    }

    @Override
    public VisitingLecturerDetailsResponse getVisitingLecturerDetails(String nicNumber) {
        // Fetch the profile by NIC number
        Profile profile = profileRepository.findByNicNumber(nicNumber)
                .orElseThrow(() -> new RuntimeException("No lecturer found with NIC: " + nicNumber));

        // Fetch the user associated with the profile
        User user = userRepository.findByProfile(profile)
                .orElseThrow(() -> new RuntimeException("No associated User found for NIC: " + nicNumber));

        VisitingLecturer visitingLecturer = user.getVisitingLecturer();
        if (visitingLecturer == null) {
            throw new RuntimeException("No visiting lecturer found for NIC: " + nicNumber);
        }

        // Fetch all valid recommendation forms for the visiting lecturer
        LocalDate currentDate = LocalDate.now();
        List<RecommendationForm> validRecommendations = recommendationFormRepository
                .findByVisitingLecturerAndAppointmentDateBeforeAndTerminationDateAfter(
                        visitingLecturer, currentDate, currentDate);

        if (validRecommendations.isEmpty()) {
            throw new RuntimeException("No valid recommendation form found for NIC: " + nicNumber);
        }

        // Filter courses where serviceType ID = 1 (External Online Tutor)
        List<VisitingLecturerDetailsResponse.CourseDetails> courseDetailsList = validRecommendations.stream()
                .filter(rec -> rec.getServiceType().getId() == 1) // Keep only External Online Tutor
                .map(rec -> new VisitingLecturerDetailsResponse.CourseDetails(
                        rec.getReferenceNumber(),
                        rec.getCourse().getCourseCode(),
                        rec.getServiceType().getTypeOfService()
                ))
                .collect(Collectors.toList());

        if (courseDetailsList.isEmpty()) {
            throw new RuntimeException("No valid courses found for Service Type: External Online Tutor (ID = 1)");
        }

        // Construct and return the response
        return new VisitingLecturerDetailsResponse(
                profile.getNicNumber(),
                profile.getFullName(),
                courseDetailsList
        );
    }

}


