package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.MarkingActivity;
import com.visitinglecturer.vlms_backend.entity.MarkingRate;
import com.visitinglecturer.vlms_backend.entity.MarkingRateType;
import com.visitinglecturer.vlms_backend.entity.MarkingType;
import com.visitinglecturer.vlms_backend.repository.MarkingActivityRepository;
import com.visitinglecturer.vlms_backend.repository.MarkingRateRepository;
import com.visitinglecturer.vlms_backend.repository.MarkingRateTypeRepository;
import com.visitinglecturer.vlms_backend.repository.MarkingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarkingRateService {

    @Autowired
    private MarkingRateRepository markingRateRepository;

    @Autowired
    private MarkingTypeRepository markingTypeRepository;

    @Autowired
    private MarkingActivityRepository markingActivityRepository;

    @Autowired
    private MarkingRateTypeRepository markingRateTypeRepository;

    // Save a single marking rate
    public MarkingRate saveMarkingRate(MarkingRate markingRate) {
        if (markingRate.getMarkingType() == null || markingRate.getMarkingType().getMarkingTypeId() == null) {
            throw new IllegalArgumentException("MarkingType ID cannot be null");
        }

        if (markingRate.getMarkingActivity() == null || markingRate.getMarkingActivity().getId() == null) {
            throw new IllegalArgumentException("MarkingActivity ID cannot be null");
        }

        if (markingRate.getMarkingRateType() == null || markingRate.getMarkingRateType().getId() == null) {
            throw new IllegalArgumentException("MarkingRateType ID cannot be null");
        }

        // Fetch and set existing references
        MarkingType existingMarkingType = markingTypeRepository.findById(markingRate.getMarkingType().getMarkingTypeId())
                .orElseThrow(() -> new RuntimeException("MarkingType not found with ID: " + markingRate.getMarkingType().getMarkingTypeId()));

        MarkingActivity existingMarkingActivity = markingActivityRepository.findById(markingRate.getMarkingActivity().getId())
                .orElseThrow(() -> new RuntimeException("MarkingActivity not found with ID: " + markingRate.getMarkingActivity().getId()));

        MarkingRateType existingMarkingRateType = markingRateTypeRepository.findById(markingRate.getMarkingRateType().getId())
                .orElseThrow(() -> new RuntimeException("MarkingRateType not found with ID: " + markingRate.getMarkingRateType().getId()));

        markingRate.setMarkingType(existingMarkingType);
        markingRate.setMarkingActivity(existingMarkingActivity);
        markingRate.setMarkingRateType(existingMarkingRateType);

        return markingRateRepository.save(markingRate);
    }

    // Save multiple marking rates at once with validation
    public List<MarkingRate> saveMarkingRates(List<MarkingRate> markingRates) {
        List<MarkingRate> validatedMarkingRates = markingRates.stream().map(markingRate -> {
            if (markingRate.getMarkingType() == null || markingRate.getMarkingType().getMarkingTypeId() == null) {
                throw new IllegalArgumentException("MarkingType ID cannot be null");
            }

            if (markingRate.getMarkingActivity() == null || markingRate.getMarkingActivity().getId() == null) {
                throw new IllegalArgumentException("MarkingActivity ID cannot be null");
            }

            if (markingRate.getMarkingRateType() == null || markingRate.getMarkingRateType().getId() == null) {
                throw new IllegalArgumentException("MarkingRateType ID cannot be null");
            }

            // Fetch and set existing references
            MarkingType existingMarkingType = markingTypeRepository.findById(markingRate.getMarkingType().getMarkingTypeId())
                    .orElseThrow(() -> new RuntimeException("MarkingType not found with ID: " + markingRate.getMarkingType().getMarkingTypeId()));

            MarkingActivity existingMarkingActivity = markingActivityRepository.findById(markingRate.getMarkingActivity().getId())
                    .orElseThrow(() -> new RuntimeException("MarkingActivity not found with ID: " + markingRate.getMarkingActivity().getId()));

            MarkingRateType existingMarkingRateType = markingRateTypeRepository.findById(markingRate.getMarkingRateType().getId())
                    .orElseThrow(() -> new RuntimeException("MarkingRateType not found with ID: " + markingRate.getMarkingRateType().getId()));

            markingRate.setMarkingType(existingMarkingType);
            markingRate.setMarkingActivity(existingMarkingActivity);
            markingRate.setMarkingRateType(existingMarkingRateType);

            return markingRate;
        }).collect(Collectors.toList());

        return markingRateRepository.saveAll(validatedMarkingRates);
    }

    // Get all marking rates
    public List<MarkingRate> getAllMarkingRates() {
        return markingRateRepository.findAll();
    }

    // Get a single marking rate by ID
    public MarkingRate getMarkingRateById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("MarkingRate ID cannot be null");
        }
        return markingRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MarkingRate not found with ID: " + id));
    }
}


