package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.enums.VerificationStatus;
import com.visitinglecturer.vlms_backend.payload.response.GroupedChecklistResponse;
import com.visitinglecturer.vlms_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.visitinglecturer.vlms_backend.payload.response.GroupWorkloadChecklistResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;

@Service
public class WorkloadService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WorkloadRepository workloadRepository;

    @Autowired
    private WorkloadGroupRepository workloadGroupRepository;

    @Autowired
    private WorkloadActivityRepository workloadActivityRepository;

    @Autowired
    private WorkloadTypeRepository workloadTypeRepository;

    @Autowired
    private VisitingLecturerRepository visitingLecturerRepository;

    @Autowired
    private RecommendationFormRepository recommendationFormRepository;

    @Autowired
    private WorkloadVerificationRepository workloadVerificationRepository;

    @Autowired
    private WorkloadVerificationStatusRepository workloadVerificationStatusRepository;

    @Autowired
    private VerifiedGroupWorkloadRepository verifiedGroupWorkloadRepository;

    @Autowired
    private VerifiedCourseWorkloadRepository verifiedCourseWorkloadRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void enterWorkload(int numberOfGroups, String[] groupNumbers, Long activityId, String activityNumber, double workloadHours, String referenceNumber) {

        // Fetch the RecommendationForm by referenceNumber
        RecommendationForm recommendationForm = recommendationFormRepository.findByReferenceNumber(referenceNumber);
        if (recommendationForm == null) {
            throw new RuntimeException("Recommendation form not found with reference number: " + referenceNumber);
        }

        // Extract required entities from RecommendationForm
        Course course = recommendationForm.getCourse();
        VisitingLecturer visitingLecturer = recommendationForm.getVisitingLecturer();

        // Get the workload activity
        WorkloadActivity workloadActivity = workloadActivityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        // Create a shared WorkloadType
        WorkloadType workloadType = new WorkloadType();
        workloadType.setWorkloadHours(workloadHours);
        workloadType.setActivityNumber(activityNumber);
        workloadType.setCourse(course);
        workloadType.setWorkloadActivity(workloadActivity);
        workloadType = workloadTypeRepository.save(workloadType);

        // Loop to create groups and workloads
        for (int i = 0; i < numberOfGroups; i++) {
            String groupNumber = groupNumbers[i];

            WorkloadGroup workloadGroup = workloadGroupRepository.findByGroupNumberAndCourseId(groupNumber, course.getId())
                    .orElseGet(() -> {
                        WorkloadGroup newGroup = new WorkloadGroup();
                        newGroup.setGroupNumber(groupNumber);
                        newGroup.setCourse(course);
                        newGroup.setVisitingLecturer(visitingLecturer);
                        newGroup.setRecommendationForm(recommendationForm);
                        return workloadGroupRepository.save(newGroup);
                    });

            Workload workload = new Workload();
            workload.setWorkloadGroup(workloadGroup);
            workload.setWorkloadType(workloadType);
            workloadRepository.save(workload);
        }
    }
    // Method to fetch all workload activities from the database
    public List<WorkloadActivity> getAllWorkloadActivities() {
        return workloadActivityRepository.findAll();
    }

    public List<GroupedChecklistResponse> getWorkloadChecklistByReference(String referenceNumber) {
        RecommendationForm recommendationForm = recommendationFormRepository.findByReferenceNumber(referenceNumber);
        if (recommendationForm == null) {
            throw new RuntimeException("Recommendation form not found for reference: " + referenceNumber);
        }

        List<WorkloadGroup> workloadGroups = workloadGroupRepository.findByRecommendationForm_ReferenceNumber(referenceNumber);
        List<GroupedChecklistResponse> groupedChecklist = new ArrayList<>();

        for (WorkloadGroup group : workloadGroups) {
            List<Workload> workloads = workloadRepository.findByWorkloadGroup_Id(group.getId());
            List<GroupWorkloadChecklistResponse> activityList = new ArrayList<>();

            for (Workload workload : workloads) {
                WorkloadType type = workload.getWorkloadType();
                WorkloadActivity activity = type.getWorkloadActivity();

                WorkloadVerification verification = workloadVerificationRepository
                        .findTopByWorkload_IdOrderByVerificationDateDesc(workload.getId());

                String status = "— Not Verified —";
                String verifiedBy = null;

                if (verification != null) {
                    status = verification.getVerificationStatus().getStatus().name();
                    if (verification.getVerifiedByUser() != null) {
                        // Fetch the full name from the user's profile
                        User user = verification.getVerifiedByUser();
                        if (user != null && user.getProfile() != null) {
                            verifiedBy = user.getProfile().getFullName(); // Get full name
                        }
                    }
                }

                // Add the activity to the list with the full name as "verifiedBy"
                activityList.add(new GroupWorkloadChecklistResponse(
                        workload.getId(),
                        activity.getActivityName(),
                        type.getActivityNumber(),
                        type.getWorkloadHours(),
                        status,
                        verifiedBy
                ));
            }

            groupedChecklist.add(new GroupedChecklistResponse(group.getGroupNumber(), activityList));
        }

        return groupedChecklist;
    }


    @Transactional
    public void verifyWorkload(Long workloadId, VerificationStatus status) {
        // Step 1: Fetch the workload
        Workload workload = workloadRepository.findById(workloadId)
                .orElseThrow(() -> new RuntimeException("Workload not found"));

        // Step 2: Fetch the verification status
        WorkloadVerificationStatus verificationStatus = workloadVerificationStatusRepository
                .findByStatus(status)
                .orElseThrow(() -> new RuntimeException("Verification status not found"));

        // Step 3: Get the authenticated admin user
        User admin = getAuthenticatedAdmin();

        // Step 4: Create or update the WorkloadVerification
        WorkloadVerification verification = workloadVerificationRepository
                .findByWorkload(workload)
                .orElse(new WorkloadVerification());  // Create a new verification entry if it doesn't exist

        // Set or update the WorkloadVerification fields
        verification.setWorkload(workload);
        verification.setVerificationStatus(verificationStatus);
        verification.setVerificationDate(new Date());
        verification.setVerifiedByUser(status == VerificationStatus.VERIFIED ? admin : null);

        // Save the updated or new verification record
        workloadVerificationRepository.save(verification);

        // Step 5: Update VerifiedGroupWorkload
        WorkloadGroup group = workload.getWorkloadGroup(); // Fetch the group for this workload
        VisitingLecturer lecturer = group.getVisitingLecturer();
        Course course = group.getCourse();

        // Fetch all workloads in the group and calculate total verified hours using a more efficient query
        double totalVerifiedHours = workloadVerificationRepository
                .findByWorkload_WorkloadGroupAndVerificationStatus_Status(group, VerificationStatus.VERIFIED)
                .stream()
                .mapToDouble(v -> v.getWorkload().getWorkloadType().getWorkloadHours())
                .sum();

        // Create or update the VerifiedGroupWorkload
        VerifiedGroupWorkload groupWorkload = verifiedGroupWorkloadRepository
                .findByVisitingLecturerAndCourseAndGroup(lecturer, course, group)
                .orElse(new VerifiedGroupWorkload());

        groupWorkload.setVisitingLecturer(lecturer);
        groupWorkload.setCourse(course);
        groupWorkload.setGroup(group);
        groupWorkload.setTotalVerifiedHours(totalVerifiedHours);
        groupWorkload.setLastUpdated(new Date());

        // Save or update the VerifiedGroupWorkload
        verifiedGroupWorkloadRepository.save(groupWorkload);

        // Step 6: Update VerifiedCourseWorkload
        double courseLevelVerified = verifiedGroupWorkloadRepository
                .findByVisitingLecturerAndCourse(lecturer, course)
                .stream()
                .mapToDouble(VerifiedGroupWorkload::getTotalVerifiedHours)
                .sum();

        VerifiedCourseWorkload courseWorkload = verifiedCourseWorkloadRepository
                .findByVisitingLecturerAndCourse(lecturer, course)
                .orElse(new VerifiedCourseWorkload());

        courseWorkload.setVisitingLecturer(lecturer);
        courseWorkload.setCourse(course);
        courseWorkload.setCourseLevelVerifiedHours(courseLevelVerified);

        // Save or update the VerifiedCourseWorkload
        verifiedCourseWorkloadRepository.save(courseWorkload);
    }

    @Transactional
    public void deleteWorkloadAndUpdateVerification(Long workloadId) {
        // Fetch the workload
        Workload workload = workloadRepository.findById(workloadId)
                .orElseThrow(() -> new RuntimeException("Workload not found"));

        // Fetch the associated WorkloadVerification and delete it
        workloadVerificationRepository.findByWorkload(workload)
                .ifPresent(workloadVerificationRepository::delete);

        // Now delete the workload
        workloadRepository.delete(workload);

        // === Recalculate and Update VerifiedGroupWorkload ===
        WorkloadGroup group = workload.getWorkloadGroup();  // Accessing the correct method
        VisitingLecturer lecturer = group.getVisitingLecturer();
        Course course = group.getCourse();

        // Calculate total verified hours for the group after deletion
        List<Workload> groupWorkloads = workloadRepository.findByWorkloadGroup(group);
        double totalVerifiedHours = groupWorkloads.stream()
                .filter(w -> {
                    return workloadVerificationRepository.findByWorkload(w)
                            .map(v -> v.getVerificationStatus().getStatus() == VerificationStatus.VERIFIED)
                            .orElse(false);
                })
                .mapToDouble(w -> w.getWorkloadType().getWorkloadHours())
                .sum();

        // Update the VerifiedGroupWorkload record
        VerifiedGroupWorkload groupWorkload = verifiedGroupWorkloadRepository
                .findByVisitingLecturerAndCourseAndGroup(lecturer, course, group)
                .orElse(new VerifiedGroupWorkload());

        groupWorkload.setVisitingLecturer(lecturer);
        groupWorkload.setCourse(course);
        groupWorkload.setGroup(group);
        groupWorkload.setTotalVerifiedHours(totalVerifiedHours);
        groupWorkload.setLastUpdated(new Date());

        if (totalVerifiedHours == 0) {
            // If no verified hours are left for the group, delete the VerifiedGroupWorkload
            verifiedGroupWorkloadRepository.delete(groupWorkload);
        } else {
            // Save or update the VerifiedGroupWorkload
            verifiedGroupWorkloadRepository.save(groupWorkload);
        }

        // === Recalculate and Update VerifiedCourseWorkload ===
        List<VerifiedGroupWorkload> groupSummaries = verifiedGroupWorkloadRepository
                .findByVisitingLecturerAndCourse(lecturer, course);

        double courseLevelVerified = groupSummaries.stream()
                .mapToDouble(VerifiedGroupWorkload::getTotalVerifiedHours)
                .sum();

        VerifiedCourseWorkload courseWorkload = verifiedCourseWorkloadRepository
                .findByVisitingLecturerAndCourse(lecturer, course)
                .orElse(new VerifiedCourseWorkload());

        courseWorkload.setVisitingLecturer(lecturer);
        courseWorkload.setCourse(course);
        courseWorkload.setCourseLevelVerifiedHours(courseLevelVerified);

        if (courseLevelVerified == 0) {
            // If no verified hours are left for the course, delete the VerifiedCourseWorkload
            verifiedCourseWorkloadRepository.delete(courseWorkload);
        } else {
            // Save or update the VerifiedCourseWorkload
            verifiedCourseWorkloadRepository.save(courseWorkload);
        }
    }



    private User getAuthenticatedAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                    .orElseThrow(() -> new SecurityException("Only admins can verify workloads."));
        }
        throw new SecurityException("Unauthorized access.");
    }

}



