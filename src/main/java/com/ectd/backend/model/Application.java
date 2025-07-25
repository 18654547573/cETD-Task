package com.ectd.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * eCTD Application Entity
 * Represents a complete drug registration application
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    
    /**
     * Application global unique ID
     */
    private Long appId;
    
    /**
     * Application number (e.g., NDA-202501, BLA-202502)
     */
    private String appNumber;
    
    /**
     * Application type (e.g., NDA, BLA, ANDA)
     */
    private String appType;
    
    /**
     * Creation timestamp
     */
    private LocalDateTime createdAt;
    
    /**
     * Update timestamp
     */
    private LocalDateTime updatedAt;
    
    /**
     * Root Section tree structure, based on eCTD 4.0
     * Stored as JSON string in database
     */
    private String rootSection;
    
    /**
     * Application status (DRAFT, SUBMITTED, APPROVED, REJECTED)
     */
    private String status;
}

