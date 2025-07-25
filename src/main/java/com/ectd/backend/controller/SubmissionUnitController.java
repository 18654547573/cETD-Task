package com.ectd.backend.controller;

import com.ectd.backend.model.SubmissionUnit;
import com.ectd.backend.service.SubmissionUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Submission Unit Controller
 * REST API endpoints for eCTD Submission Unit management
 */
@RestController
@RequestMapping("/submission-units")
@CrossOrigin(origins = "*")
public class SubmissionUnitController {

    @Autowired
    private SubmissionUnitService submissionUnitService;

    /**
     * Create a new submission unit
     * @param payload Request payload
     * @return Created submission unit
     */
    @PostMapping
    public ResponseEntity<?> createSubmissionUnit(@RequestBody Map<String, Object> payload) {
        try {
            Long appId = Long.valueOf(payload.get("appId").toString());
            String effectiveDateStr = (String) payload.get("effectiveDate");
            String suType = (String) payload.get("suType");
            String suUnitType = (String) payload.get("suUnitType");
            String couDataJson = (String) payload.get("couData");
            
            if (appId == null) {
                return ResponseEntity.badRequest().body("Application ID is required");
            }
            if (effectiveDateStr == null || effectiveDateStr.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Effective date is required");
            }
            if (suType == null || suType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Submission type is required");
            }
            if (suUnitType == null || suUnitType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Submission unit type is required");
            }
            
            LocalDate effectiveDate = LocalDate.parse(effectiveDateStr);
            
            SubmissionUnit su = submissionUnitService.createSubmissionUnit(
                appId, effectiveDate, suType.trim(), suUnitType.trim(), couDataJson);
            return ResponseEntity.ok(su);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get submission unit by ID
     * @param suId Submission unit ID
     * @return SubmissionUnit entity
     */
    @GetMapping("/{suId}")
    public ResponseEntity<?> getSubmissionUnitById(@PathVariable Long suId) {
        try {
            SubmissionUnit su = submissionUnitService.getSubmissionUnitById(suId);
            if (su == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(su);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get all submission units for an application
     * @param appId Application ID
     * @return List of submission units
     */
    @GetMapping("/by-app/{appId}")
    public ResponseEntity<?> getSubmissionUnitsByAppId(@PathVariable Long appId) {
        try {
            List<SubmissionUnit> sus = submissionUnitService.getSubmissionUnitsByAppId(appId);
            return ResponseEntity.ok(sus);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get submission unit by application ID and sequence number
     * @param appId Application ID
     * @param sequenceNum Sequence number
     * @return SubmissionUnit entity
     */
    @GetMapping("/by-app/{appId}/sequence/{sequenceNum}")
    public ResponseEntity<?> getSubmissionUnitByAppIdAndSequence(@PathVariable Long appId, @PathVariable Integer sequenceNum) {
        try {
            SubmissionUnit su = submissionUnitService.getSubmissionUnitByAppIdAndSequence(appId, sequenceNum);
            if (su == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(su);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get all submission units
     * @return List of submission units
     */
    @GetMapping
    public ResponseEntity<?> getAllSubmissionUnits() {
        try {
            List<SubmissionUnit> sus = submissionUnitService.getAllSubmissionUnits();
            return ResponseEntity.ok(sus);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Update submission unit
     * @param suId Submission unit ID
     * @param payload Request payload
     * @return Updated submission unit
     */
    @PutMapping("/{suId}")
    public ResponseEntity<?> updateSubmissionUnit(@PathVariable Long suId, @RequestBody Map<String, Object> payload) {
        try {
            SubmissionUnit su = submissionUnitService.getSubmissionUnitById(suId);
            if (su == null) {
                return ResponseEntity.notFound().build();
            }

            // Update fields if provided
            if (payload.containsKey("effectiveDate")) {
                String effectiveDateStr = (String) payload.get("effectiveDate");
                su.setEffectiveDate(LocalDate.parse(effectiveDateStr));
            }
            if (payload.containsKey("suType")) {
                su.setSuType((String) payload.get("suType"));
            }
            if (payload.containsKey("suUnitType")) {
                su.setSuUnitType((String) payload.get("suUnitType"));
            }
            if (payload.containsKey("couData")) {
                su.setCouData((String) payload.get("couData"));
            }
            if (payload.containsKey("status")) {
                su.setStatus((String) payload.get("status"));
            }

            SubmissionUnit updatedSu = submissionUnitService.updateSubmissionUnit(su);
            return ResponseEntity.ok(updatedSu);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Update CoU data for a submission unit
     * @param suId Submission unit ID
     * @param payload Request payload containing couData
     * @return Updated submission unit
     */
    @PutMapping("/{suId}/cou-data")
    public ResponseEntity<?> updateCouData(@PathVariable Long suId, @RequestBody Map<String, String> payload) {
        try {
            String couDataJson = payload.get("couData");
            
            SubmissionUnit updatedSu = submissionUnitService.updateCouData(suId, couDataJson);
            return ResponseEntity.ok(updatedSu);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Delete submission unit
     * @param suId Submission unit ID
     * @return Success message
     */
    @DeleteMapping("/{suId}")
    public ResponseEntity<?> deleteSubmissionUnit(@PathVariable Long suId) {
        try {
            SubmissionUnit su = submissionUnitService.getSubmissionUnitById(suId);
            if (su == null) {
                return ResponseEntity.notFound().build();
            }
            
            submissionUnitService.deleteSubmissionUnit(suId);
            return ResponseEntity.ok("Submission Unit deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Create sample CoU data for testing
     * @param payload Request payload
     * @return Sample CoU data JSON
     */
    @PostMapping("/sample-cou-data")
    public ResponseEntity<?> createSampleCouData(@RequestBody Map<String, Object> payload) {
        try {
            String operationType = (String) payload.get("operationType");
            Long nodeId = Long.valueOf(payload.get("nodeId").toString());
            String documentPath = (String) payload.get("documentPath");
            
            if (operationType == null || operationType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Operation type is required");
            }
            if (nodeId == null) {
                return ResponseEntity.badRequest().body("Node ID is required");
            }
            if (documentPath == null || documentPath.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Document path is required");
            }
            
            String couDataJson = submissionUnitService.createSampleCouData(
                operationType.trim(), nodeId, documentPath.trim());
            return ResponseEntity.ok(Map.of("couData", couDataJson));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }
}

