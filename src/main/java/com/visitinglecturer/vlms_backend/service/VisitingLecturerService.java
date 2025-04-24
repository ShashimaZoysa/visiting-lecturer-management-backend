package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.payload.response.VisitingLecturerDetailsResponse;

public interface VisitingLecturerService {
    VisitingLecturerDetailsResponse getVisitingLecturerDetails(String nicNumber);
}

