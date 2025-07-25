package com.ectd.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * eCTD Submission Unit Entity
 * Represents an independent regulatory sequence unit that composes an Application
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionUnit {
    
    /**
     * Submission Unit unique ID
     */
    private Long suId;
    
    /**
     * Foreign key to the parent application
     */
    private Long appId;
    
    /**
     * Submission sequence number (e.g., 1, 2, 3...)
     */
    private Integer sequenceNum;
    
    /**
     * Effective date of the submission
     */
    private LocalDate effectiveDate;
    
    /**
     * Submission Type (e.g., original, supplement, amendment)
     */
    private String suType;
    
    /**
     * Submission Unit Type (e.g., ectd-4-0)
     */
    private String suUnitType;
    
    /**
     * Context of Use (CoU) data, describing the changes in this SU
     * Stored as JSON string in database
     */
    private String couData;
    
    /**
     * Creation timestamp
     */
    private LocalDateTime createdAt;
    
    /**
     * Update timestamp
     */
    private LocalDateTime updatedAt;
    
    /**
     * Submission Unit status (DRAFT, SUBMITTED, APPROVED, REJECTED)
     */
    private String status;
}

