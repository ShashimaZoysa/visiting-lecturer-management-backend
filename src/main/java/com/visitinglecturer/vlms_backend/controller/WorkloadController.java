package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.Workload;
import com.visitinglecturer.vlms_backend.entity.WorkloadActivity;
import com.visitinglecturer.vlms_backend.enums.VerificationStatus;
import com.visitinglecturer.vlms_backend.payload.request.WorkloadRequest;
import com.visitinglecturer.vlms_backend.payload.response.GroupedChecklistResponse;
import com.visitinglecturer.vlms_backend.repository.WorkloadRepository;
import com.visitinglecturer.vlms_backend.service.WorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workloads")
@CrossOrigin(origins = "http://localhost:5173")
public class WorkloadController {

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private WorkloadRepository workloadRepository;


    // Endpoint to enter workload details using @RequestBody
    @PostMapping("/enter")
    public ResponseEntity<String> enterWorkload(@RequestBody WorkloadRequest workloadRequest) {
        try {
            // Call the service method to enter workload
            workloadService.enterWorkload(
                    workloadRequest.getNumberOfGroups(),
                    workloadRequest.getGroupNumbers(),
                    workloadRequest.getActivityId(),
                    workloadRequest.getActivityNumber(),
                    workloadRequest.getWorkloadHours(),
                    workloadRequest.getReferenceNumber()
            );
            return new ResponseEntity<>("Workload entered successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error entering workload: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activities")
    public List<WorkloadActivity> getAllWorkloadActivities() {
        return workloadService.getAllWorkloadActivities();
    }

    @GetMapping("/checklist")
    public List<GroupedChecklistResponse> getChecklist(@RequestParam String referenceNumber) {
        return workloadService.getWorkloadChecklistByReference(referenceNumber);
    }

    @PostMapping("/verify/{workloadId}")
    public ResponseEntity<String> verifyWorkload(
            @PathVariable Long workloadId,
            @RequestParam VerificationStatus status) {

        try {
            workloadService.verifyWorkload(workloadId, status);
            return ResponseEntity.ok("Workload verification successful.");
        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + se.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }




    @DeleteMapping("/delete/{workloadId}")
    public ResponseEntity<String> deleteWorkload(@PathVariable Long workloadId) {
        try {
            workloadService.deleteWorkloadAndUpdateVerification(workloadId);
            return ResponseEntity.ok("Workload deleted and verification updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }




}


