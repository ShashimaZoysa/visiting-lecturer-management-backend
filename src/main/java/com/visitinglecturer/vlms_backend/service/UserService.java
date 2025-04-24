package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.Profile;
import com.visitinglecturer.vlms_backend.entity.Role;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.entity.VisitingLecturer;
import com.visitinglecturer.vlms_backend.entity.Qualification;
import com.visitinglecturer.vlms_backend.entity.QualificationVisitingLecturer;
import com.visitinglecturer.vlms_backend.payload.request.QualificationRequest;
import com.visitinglecturer.vlms_backend.payload.request.UserRequest;
import com.visitinglecturer.vlms_backend.payload.response.UserResponse;
import com.visitinglecturer.vlms_backend.repository.ProfileRepository;
import com.visitinglecturer.vlms_backend.repository.QualificationRepository;
import com.visitinglecturer.vlms_backend.repository.QualificationVisitingLecturerRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import com.visitinglecturer.vlms_backend.repository.VisitingLecturerRepository;
import com.visitinglecturer.vlms_backend.repository.RoleRepository;  // Add RoleRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private VisitingLecturerRepository visitingLecturerRepository;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private QualificationVisitingLecturerRepository qualificationVisitingLecturerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HashService hashService;

    public User saveUser(UserRequest userRequest) {
        // Check if the user already exists
        User user = userRepository.findByUsername(userRequest.getUsername()).orElse(null);

        if (user == null) {
            user = new User();
            user.setUsername(userRequest.getUsername());
        }

        // Handle Profile
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
        }
        profile.setEmail(userRequest.getEmail());
        profile.setGender(userRequest.getGender());
        profile.setNicNumber(userRequest.getNicNumber());
        profile.setFullName(userRequest.getFullName());
        profileRepository.save(profile);
        user.setProfile(profile);

        // Hash and set the password
        try {
            String hashedPassword = hashService.hashString(userRequest.getPassword());
            user.setPassword(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }

        // Handle Roles
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }

        Role role = roleRepository.findByName(userRequest.getRole());
        if (role == null) {
            throw new IllegalArgumentException("Invalid role: " + userRequest.getRole());
        }
        roles.add(role);
        user.setRoles(roles);

        if (userRequest.getRole().equals("ROLE_VISITING_LECTURER")) {
            handleVisitingLecturerData(user, userRequest);
        }

        return userRepository.save(user);

    }

    private void handleVisitingLecturerData(User user, UserRequest userRequest) {
        // Handle Visiting Lecturer
        VisitingLecturer visitingLecturer = user.getVisitingLecturer();
        if (visitingLecturer == null) {
            visitingLecturer = new VisitingLecturer();
        }

        visitingLecturer.setPrivateAddress(userRequest.getPrivateAddress());
        visitingLecturer.setPhoneNumber(userRequest.getPhoneNumber());
        visitingLecturer.setPresentEmployment(userRequest.getPresentEmployment());
        visitingLecturer.setDepartment(userRequest.getDepartment());
        visitingLecturer = visitingLecturerRepository.save(visitingLecturer);
        user.setVisitingLecturer(visitingLecturer);

        // Handle Qualifications
        if (userRequest.getQualifications() != null) {
            for (QualificationRequest qualificationRequest : userRequest.getQualifications()) {

                Qualification qualification = new Qualification();
                qualification.setTitle(qualificationRequest.getTitle());
                qualification.setLevel(qualificationRequest.getLevel());
                qualification.setUniversity(qualificationRequest.getUniversity());

                qualification = qualificationRepository.save(qualification);

                // Link Qualification and Visiting Lecturer
                QualificationVisitingLecturer qvl = new QualificationVisitingLecturer();
                qvl.setVisitingLecturer(visitingLecturer);
                qvl.setQualification(qualification);
                qualificationVisitingLecturerRepository.save(qvl);
            }
        }
    }

    public UserResponse viewUser(String username) {
        // Check if the user exists
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        // Populate the response object
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());

        // Extract role
        if (!user.getRoles().isEmpty()) {
            response.setRole(user.getRoles().iterator().next().getName());
        }

        Profile profile = user.getProfile();
        if (profile != null) {
            response.setFullName(profile.getFullName());
            response.setNicNumber(profile.getNicNumber());
            response.setGender(profile.getGender());
            response.setEmail(profile.getEmail());
        }

        // Set phoneNumber if the user is a Visiting Lecturer
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_VISITING_LECTURER"))) {
            VisitingLecturer vl = user.getVisitingLecturer();
            if (vl != null) {
                response.setPhoneNumber(vl.getPhoneNumber());
            }
        }

        return response;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public VisitingLecturer getVisitingLecturerByUserId(Long userId) {
        return visitingLecturerRepository.findById(userId).orElse(null);
    }
}








