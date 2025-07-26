package com.ectd.backend.service;

import com.ectd.backend.mapper.SubmissionUnitMapper;
import com.ectd.backend.model.SubmissionUnit;
import com.ectd.backend.model.CoUOperation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Submission Unit Service
 * Business logic for eCTD Submission Unit management with CoU operations array support
 */
@Service
@Transactional
public class SubmissionUnitService {

    @Autowired
    private SubmissionUnitMapper submissionUnitMapper;

    @Autowired
    private ApplicationService applicationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Create a new Submission Unit
     * @param appId Application ID
     * @param effectiveDate Effective date
     * @param suType Submission type
     * @param suUnitType Submission unit type
     * @param couDataJson CoU data JSON string (optional)
     * @return Created submission unit
     * @throws JsonProcessingException If JSON processing fails
     */
    public SubmissionUnit createSubmissionUnit(Long appId, LocalDate effectiveDate, 
                                             String suType, String suUnitType, 
                                             String couDataJson) throws JsonProcessingException {
        // Validate that application exists
        if (applicationService.getApplicationById(appId) == null) {
            throw new IllegalArgumentException("Application not found: " + appId);
        }

        // Initialize empty CoU operations array if not provided
        if (couDataJson == null || couDataJson.trim().isEmpty()) {
            couDataJson = "[]";
        } else {
            // Validate CoU data JSON format
            parseCouData(couDataJson);
        }

        // Get next sequence number
        Integer nextSequenceNum = submissionUnitMapper.getNextSequenceNum(appId);

        SubmissionUnit su = new SubmissionUnit();
        su.setAppId(appId);
        su.setSequenceNum(nextSequenceNum);
        su.setEffectiveDate(effectiveDate);
        su.setSuType(suType);
        su.setSuUnitType(suUnitType);
        su.setCouData(couDataJson);
        su.setStatus("DRAFT");

        submissionUnitMapper.insert(su);
        return su;
    }

    /**
     * Get submission unit by ID
     * @param suId Submission unit ID
     * @return SubmissionUnit entity
     */
    public SubmissionUnit getSubmissionUnitById(Long suId) {
        return submissionUnitMapper.findById(suId);
    }

    /**
     * Get all submission units for an application
     * @param appId Application ID
     * @return List of submission units
     */
    public List<SubmissionUnit> getSubmissionUnitsByAppId(Long appId) {
        return submissionUnitMapper.findByAppId(appId);
    }

    /**
     * Get submission unit by application ID and sequence number
     * @param appId Application ID
     * @param sequenceNum Sequence number
     * @return SubmissionUnit entity
     */
    public SubmissionUnit getSubmissionUnitByAppIdAndSequence(Long appId, Integer sequenceNum) {
        return submissionUnitMapper.findByAppIdAndSequence(appId, sequenceNum);
    }

    /**
     * Get all submission units
     * @return List of submission units
     */
    public List<SubmissionUnit> getAllSubmissionUnits() {
        return submissionUnitMapper.findAll();
    }

    /**
     * Update submission unit
     * @param submissionUnit SubmissionUnit entity
     * @return Updated submission unit
     */
    public SubmissionUnit updateSubmissionUnit(SubmissionUnit submissionUnit) {
        submissionUnitMapper.update(submissionUnit);
        return submissionUnitMapper.findById(submissionUnit.getSuId());
    }

    /**
     * Delete submission unit by ID
     * @param suId Submission unit ID
     */
    public void deleteSubmissionUnit(Long suId) {
        submissionUnitMapper.deleteById(suId);
    }

    /**
     * Update CoU data for a submission unit (replace entire array)
     * @param suId Submission unit ID
     * @param couDataJson CoU data JSON string
     * @return Updated submission unit
     * @throws JsonProcessingException If JSON processing fails
     */
    public SubmissionUnit updateCouData(Long suId, String couDataJson) throws JsonProcessingException {
        // Validate JSON format
        if (couDataJson != null && !couDataJson.trim().isEmpty()) {
            parseCouData(couDataJson);
        } else {
            couDataJson = "[]";
        }
        
        SubmissionUnit su = submissionUnitMapper.findById(suId);
        if (su == null) {
            throw new IllegalArgumentException("Submission Unit not found: " + suId);
        }
        
        su.setCouData(couDataJson);
        submissionUnitMapper.update(su);
        return su;
    }

    /**
     * Add a single CoU operation to the specified Submission Unit
     * @param suId Submission Unit ID
     * @param couOperation CoU operation object
     * @return Updated Submission Unit
     */
    public SubmissionUnit addCouOperation(Long suId, CoUOperation couOperation) {
        SubmissionUnit submissionUnit = submissionUnitMapper.findById(suId);
        if (submissionUnit == null) {
            throw new IllegalArgumentException("Submission Unit not found with id: " + suId);
        }

        try {
            // Parse existing CoU data
            List<CoUOperation> couOperations = parseCouData(submissionUnit.getCouData());
            
            // Set operation metadata
            couOperation.setSuId(String.valueOf(suId));
            couOperation.setTimestamp(LocalDateTime.now());
            
            // Generate CoU ID if not provided
            if (couOperation.getCouId() == null || couOperation.getCouId().trim().isEmpty()) {
                couOperation.setCouId("COU_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000));
            }
            
            // Add new operation to array
            couOperations.add(couOperation);
            
            // Serialize back to JSON string
            String updatedCouData = objectMapper.writeValueAsString(couOperations);
            
            // Update database
            submissionUnit.setCouData(updatedCouData);
            submissionUnitMapper.update(submissionUnit);
            
            return submissionUnit;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to add CoU operation: " + e.getMessage(), e);
        }
    }

    /**
     * Get all CoU operations for the specified Submission Unit
     * @param suId Submission Unit ID
     * @return List of CoU operations
     */
    public List<CoUOperation> getCouOperations(Long suId) {
        SubmissionUnit submissionUnit = submissionUnitMapper.findById(suId);
        if (submissionUnit == null) {
            return new ArrayList<>();
        }
        
        return parseCouData(submissionUnit.getCouData());
    }

    /**
     * Remove a specific CoU operation
     * @param suId Submission Unit ID
     * @param couId CoU operation ID
     * @return Updated Submission Unit
     */
    public SubmissionUnit removeCouOperation(Long suId, String couId) {
        SubmissionUnit submissionUnit = submissionUnitMapper.findById(suId);
        if (submissionUnit == null) {
            throw new IllegalArgumentException("Submission Unit not found with id: " + suId);
        }

        try {
            // Parse existing CoU data
            List<CoUOperation> couOperations = parseCouData(submissionUnit.getCouData());
            
            // Remove the specified operation
            couOperations.removeIf(op -> couId.equals(op.getCouId()));
            
            // Serialize back to JSON string
            String updatedCouData = objectMapper.writeValueAsString(couOperations);
            
            // Update database
            submissionUnit.setCouData(updatedCouData);
            submissionUnitMapper.update(submissionUnit);
            
            return submissionUnit;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove CoU operation: " + e.getMessage(), e);
        }
    }

    /**
     * Parse CoU data string to CoU operations list
     * @param couDataJson CoU data JSON string
     * @return List of CoU operations
     */
    private List<CoUOperation> parseCouData(String couDataJson) {
        if (couDataJson == null || couDataJson.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(couDataJson, new TypeReference<List<CoUOperation>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Invalid CoU data format: " + e.getMessage(), e);
        }
    }

    /**
     * Create sample CoU data for testing
     * @param operationType Operation type (add, replace, delete)
     * @param nodeId Node ID
     * @param documentPath Document path
     * @return CoU data JSON string
     * @throws JsonProcessingException If JSON processing fails
     */
    public String createSampleCouData(String operationType, Long nodeId, String documentPath) throws JsonProcessingException {
        CoUOperation.DocumentInfo document = new CoUOperation.DocumentInfo(
            "doc-" + System.currentTimeMillis(),
            "Sample Document",
            "PDF",
            documentPath
        );
        
        CoUOperation operation = new CoUOperation(operationType, null, nodeId, document);
        List<CoUOperation> operations = List.of(operation);
        
        return objectMapper.writeValueAsString(operations);
    }
}

