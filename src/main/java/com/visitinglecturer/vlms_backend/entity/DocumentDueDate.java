package com.visitinglecturer.vlms_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"visiting_lecturer_id", "document_type_id"}))
public class DocumentDueDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visiting_lecturer_id")
    private VisitingLecturer visitingLecturer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    private LocalDate dueDate; // NULL allowed if there's no specific deadline
}
