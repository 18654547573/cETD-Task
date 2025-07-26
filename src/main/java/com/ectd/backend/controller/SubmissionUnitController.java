package com.ectd.backend.controller;

import com.ectd.backend.model.SubmissionUnit;
import com.ectd.backend.model.CoUOperation;
import com.ectd.backend.service.SubmissionUnitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Submission Unit REST Controller
 * Handles HTTP requests for eCTD Submission Unit management with CoU operations support
 */
@RestController
@RequestMapping("/api/submission-units")
@CrossOrigin(origins = "*")
public class SubmissionUnitController {

    @Autowired
    private SubmissionUnitService submissionUnitService;

    /**
     * Get all submission units
     * @return List of submission units
     */
    @GetMapping
    public ResponseEntity<List<SubmissionUnit>> getAllSubmissionUnits() {
        try {
            List<SubmissionUnit> submissionUnits = submissionUnitService.getAllSubmissionUnits();
            return ResponseEntity.ok(submissionUnits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get submission units by application ID
     * @param appId Application ID
     * @return List of submission units
     */
    @GetMapping("/by-app/{appId}")
    public ResponseEntity<List<SubmissionUnit>> getSubmissionUnitsByAppId(@PathVariable Long appId) {
        try {
            List<SubmissionUnit> submissionUnits = submissionUnitService.getSubmissionUnitsByAppId(appId);
            return ResponseEntity.ok(submissionUnits);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get submission unit by ID
     * @param suId Submission unit ID
     * @return SubmissionUnit entity
     */
    @GetMapping("/{suId}")
    public ResponseEntity<SubmissionUnit> getSubmissionUnitById(@PathVariable Long suId) {
        try {
            SubmissionUnit submissionUnit = submissionUnitService.getSubmissionUnitById(suId);
            if (submissionUnit != null) {
                return ResponseEntity.ok(submissionUnit);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create a new submission unit
     * @param requestBody Request body containing submission unit data
     * @return Created submission unit
     */
    @PostMapping
    public ResponseEntity<SubmissionUnit> createSubmissionUnit(@RequestBody Map<String, Object> requestBody) {
        try {
            Long appId = Long.valueOf(requestBody.get("appId").toString());
            LocalDate effectiveDate = LocalDate.parse(requestBody.get("effectiveDate").toString());
            String suType = requestBody.get("suType").toString();
            String suUnitType = requestBody.get("suUnitType").toString();
            String couData = requestBody.get("couData") != null ? requestBody.get("couData").toString() : null;

            SubmissionUnit submissionUnit = submissionUnitService.createSubmissionUnit(
                appId, effectiveDate, suType, suUnitType, couData);
            return ResponseEntity.status(HttpStatus.CREATED).body(submissionUnit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update submission unit
     * @param suId Submission unit ID
     * @param submissionUnit Updated submission unit data
     * @return Updated submission unit
     */
    @PutMapping("/{suId}")
    public ResponseEntity<SubmissionUnit> updateSubmissionUnit(
            @PathVariable Long suId, @RequestBody SubmissionUnit submissionUnit) {
        try {
            submissionUnit.setSuId(suId);
            SubmissionUnit updated = submissionUnitService.updateSubmissionUnit(submissionUnit);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete submission unit
     * @param suId Submission unit ID
     * @return Success response
     */
    @DeleteMapping("/{suId}")
    public ResponseEntity<Void> deleteSubmissionUnit(@PathVariable Long suId) {
        try {
            submissionUnitService.deleteSubmissionUnit(suId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update CoU data for a submission unit (replace entire array)
     * @param suId Submission unit ID
     * @param requestBody Request body containing CoU data
     * @return Updated submission unit
     */
    @PutMapping("/{suId}/cou-data")
    public ResponseEntity<SubmissionUnit> updateCouData(
            @PathVariable Long suId, @RequestBody Map<String, String> requestBody) {
        try {
            String couData = requestBody.get("couData");
            SubmissionUnit updated = submissionUnitService.updateCouData(suId, couData);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add a single CoU operation to the submission unit
     * @param suId Submission unit ID
     * @param couOperation CoU operation to add
     * @return Updated submission unit
     */
    @PostMapping("/{suId}/cou-operations")
    public ResponseEntity<SubmissionUnit> addCouOperation(
            @PathVariable Long suId, @RequestBody CoUOperation couOperation) {
        try {
            SubmissionUnit updated = submissionUnitService.addCouOperation(suId, couOperation);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all CoU operations for a submission unit
     * @param suId Submission unit ID
     * @return List of CoU operations
     */
    @GetMapping("/{suId}/cou-operations")
    public ResponseEntity<List<CoUOperation>> getCouOperations(@PathVariable Long suId) {
        try {
            List<CoUOperation> operations = submissionUnitService.getCouOperations(suId);
            return ResponseEntity.ok(operations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Remove a specific CoU operation from the submission unit
     * @param suId Submission unit ID
     * @param couId CoU operation ID
     * @return Updated submission unit
     */
    @DeleteMapping("/{suId}/cou-operations/{couId}")
    public ResponseEntity<SubmissionUnit> removeCouOperation(
            @PathVariable Long suId, @PathVariable String couId) {
        try {
            SubmissionUnit updated = submissionUnitService.removeCouOperation(suId, couId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create sample CoU data for testing
     * @param operationType Operation type
     * @param nodeId Node ID
     * @param documentPath Document path
     * @return Sample CoU data JSON
     */
    @GetMapping("/sample-cou-data")
    public ResponseEntity<String> createSampleCouData(
            @RequestParam String operationType,
            @RequestParam Long nodeId,
            @RequestParam String documentPath) {
        try {
            String sampleData = submissionUnitService.createSampleCouData(operationType, nodeId, documentPath);
            return ResponseEntity.ok(sampleData);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

