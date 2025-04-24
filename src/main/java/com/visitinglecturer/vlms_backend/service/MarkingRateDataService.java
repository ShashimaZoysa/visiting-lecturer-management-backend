package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.MarkingType;
import com.visitinglecturer.vlms_backend.entity.MarkingActivity;
import com.visitinglecturer.vlms_backend.entity.MarkingRateType;
import com.visitinglecturer.vlms_backend.repository.MarkingTypeRepository;
import com.visitinglecturer.vlms_backend.repository.MarkingActivityRepository;
import com.visitinglecturer.vlms_backend.repository.MarkingRateTypeRepository;
import com.visitinglecturer.vlms_backend.repository.MarkingTypeRateTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkingRateDataService {

    @Autowired
    private MarkingTypeRepository markingTypeRepository;

    @Autowired
    private MarkingActivityRepository markingActivityRepository;

    @Autowired
    private MarkingRateTypeRepository markingRateTypeRepository;

    @Autowired
    private MarkingTypeRateTypeRepository markingTypeRateTypeRepository;

    /**
     * Get all Marking Types
     */
    public List<MarkingType> getAllMarkingTypes() {
        return markingTypeRepository.findAll();
    }

    /**
     * Get all Marking Activities
     */
    public List<MarkingActivity> getAllMarkingActivities() {
        return markingActivityRepository.findAll();
    }

    /**
     * Get all Marking Rate Types
     */
    public List<MarkingRateType> getAllMarkingRateTypes() {
        return markingRateTypeRepository.findAll();
    }

    /**
     * Get Marking Rate Types mapped to a specific Marking Type
     */
    public List<MarkingRateType> getRateTypesByMarkingType(Long markingTypeId) {
        return markingTypeRateTypeRepository.findRateTypesByMarkingType(markingTypeId);
    }
}


