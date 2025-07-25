package com.ectd.backend.service;

import com.ectd.backend.mapper.SubmissionUnitMapper;
import com.ectd.backend.model.SubmissionUnit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Submission Unit Service
 * Business logic for eCTD Submission Unit management
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
     * @param couDataJson CoU data JSON string
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

        // Validate CoU data JSON format if provided
        if (couDataJson != null && !couDataJson.trim().isEmpty()) {
            objectMapper.readTree(couDataJson);
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
     * Update CoU data for a submission unit
     * @param suId Submission unit ID
     * @param couDataJson CoU data JSON string
     * @return Updated submission unit
     * @throws JsonProcessingException If JSON processing fails
     */
    public SubmissionUnit updateCouData(Long suId, String couDataJson) throws JsonProcessingException {
        // Validate JSON format
        if (couDataJson != null && !couDataJson.trim().isEmpty()) {
            objectMapper.readTree(couDataJson);
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
     * Create sample CoU data for testing
     * @param operationType Operation type (add, replace, delete)
     * @param nodeId Node ID
     * @param documentPath Document path
     * @return CoU data JSON string
     * @throws JsonProcessingException If JSON processing fails
     */
    public String createSampleCouData(String operationType, Long nodeId, String documentPath) throws JsonProcessingException {
        Map<String, Object> couData = Map.of(
            "operations", List.of(
                Map.of(
                    "type", operationType,
                    "nodeId", nodeId,
                    "documentPath", documentPath
                )
            )
        );
        return objectMapper.writeValueAsString(couData);
    }
}

