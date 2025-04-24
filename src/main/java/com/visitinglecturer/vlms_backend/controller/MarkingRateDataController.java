package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.MarkingType;
import com.visitinglecturer.vlms_backend.entity.MarkingActivity;
import com.visitinglecturer.vlms_backend.entity.MarkingRateType;
import com.visitinglecturer.vlms_backend.service.MarkingRateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marking-data")
@CrossOrigin(origins = "http://localhost:5173")
public class MarkingRateDataController {

    private final MarkingRateDataService markingRateDataService;

    @Autowired
    public MarkingRateDataController(MarkingRateDataService markingRateDataService) {
        this.markingRateDataService = markingRateDataService;
    }

    /**
     * Get all Marking Types
     * @return List of MarkingType
     */
    @GetMapping("/marking-types")
    public ResponseEntity<List<MarkingType>> getAllMarkingTypes() {
        List<MarkingType> markingTypes = markingRateDataService.getAllMarkingTypes();
        return ResponseEntity.ok(markingTypes);
    }

    /**
     * Get all Marking Activities
     * @return List of MarkingActivity
     */
    @GetMapping("/marking-activities")
    public ResponseEntity<List<MarkingActivity>> getAllMarkingActivities() {
        List<MarkingActivity> markingActivities = markingRateDataService.getAllMarkingActivities();
        return ResponseEntity.ok(markingActivities);
    }

    /**
     * Get all Marking Rate Types
     * @return List of MarkingRateType
     */
    @GetMapping("/marking-rate-types")
    public ResponseEntity<List<MarkingRateType>> getAllMarkingRateTypes() {
        List<MarkingRateType> markingRateTypes = markingRateDataService.getAllMarkingRateTypes();
        return ResponseEntity.ok(markingRateTypes);
    }

    /**
     * Get Rate Types mapped to a specific Marking Type
     * @param markingTypeId The ID of the marking type.
     * @return List of MarkingRateType mapped to the given Marking Type
     */
    @GetMapping("/marking-type/{markingTypeId}/rate-types")
    public ResponseEntity<List<MarkingRateType>> getRateTypesByMarkingType(@PathVariable Long markingTypeId) {
        List<MarkingRateType> rateTypes = markingRateDataService.getRateTypesByMarkingType(markingTypeId);
        if (rateTypes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rateTypes);
    }
}

