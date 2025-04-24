package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendationFormService {

    @Autowired
    private RecommendationFormRepository recommendationFormRepository;

    @Autowired
    private VisitingLecturerRepository visitingLecturerRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ProgramOfStudyRepository programOfStudyRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Transactional
    public RecommendationForm saveRecommendationForm(RecommendationForm recommendationForm) {
        return recommendationFormRepository.save(recommendationForm);
    }
}

