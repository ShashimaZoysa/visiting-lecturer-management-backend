package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.*;
import com.visitinglecturer.vlms_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecommendationDataService {

    private final ProgramOfStudyRepository programOfStudyRepository;
    private final CourseRepository courseRepository;
    private final CentreRepository centreRepository;
    private final AcademicYearRepository academicYearRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public RecommendationDataService(
            ProgramOfStudyRepository programOfStudyRepository,
            CourseRepository courseRepository,
            CentreRepository centreRepository,
            AcademicYearRepository academicYearRepository,
            ServiceTypeRepository serviceTypeRepository) {
        this.programOfStudyRepository = programOfStudyRepository;
        this.courseRepository = courseRepository;
        this.centreRepository = centreRepository;
        this.academicYearRepository = academicYearRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    // Fetch all programs of study
    public List<ProgramOfStudy> getAllPrograms() {
        return programOfStudyRepository.findAll();
    }

    // Fetch all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Fetch all centres
    public List<Centre> getAllCentres() {
        return centreRepository.findAll();
    }

    // Fetch all academic years
    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

    // Fetch all service types
    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }
}

