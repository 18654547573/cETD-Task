package com.ectd.backend.controller;

import com.ectd.backend.model.Application;
import com.ectd.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Application Controller
 * REST API endpoints for eCTD Application management
 */
@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * Create a new application
     * @param payload Request payload containing appNumber and appType
     * @return Created application
     */
    @PostMapping
    public ResponseEntity<?> createApplication(@RequestBody Map<String, String> payload) {
        try {
            String appNumber = payload.get("appNumber");
            String appType = payload.get("appType");
            
            if (appNumber == null || appNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Application number is required");
            }
            if (appType == null || appType.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Application type is required");
            }
            
            Application app = applicationService.createApplication(appNumber.trim(), appType.trim());
            return ResponseEntity.ok(app);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get application by ID
     * @param appId Application ID
     * @return Application entity
     */
    @GetMapping("/{appId}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long appId) {
        try {
            Application app = applicationService.getApplicationById(appId);
            if (app == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(app);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get application by number
     * @param appNumber Application number
     * @return Application entity
     */
    @GetMapping("/number/{appNumber}")
    public ResponseEntity<?> getApplicationByNumber(@PathVariable String appNumber) {
        try {
            Application app = applicationService.getApplicationByNumber(appNumber);
            if (app == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(app);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Get all applications
     * @return List of applications
     */
    @GetMapping
    public ResponseEntity<?> getAllApplications() {
        try {
            List<Application> apps = applicationService.getAllApplications();
            return ResponseEntity.ok(apps);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Update application
     * @param appId Application ID
     * @param payload Request payload
     * @return Updated application
     */
    @PutMapping("/{appId}")
    public ResponseEntity<?> updateApplication(@PathVariable Long appId, @RequestBody Map<String, Object> payload) {
        try {
            Application app = applicationService.getApplicationById(appId);
            if (app == null) {
                return ResponseEntity.notFound().build();
            }

            // Update fields if provided
            if (payload.containsKey("appNumber")) {
                app.setAppNumber((String) payload.get("appNumber"));
            }
            if (payload.containsKey("appType")) {
                app.setAppType((String) payload.get("appType"));
            }
            if (payload.containsKey("status")) {
                app.setStatus((String) payload.get("status"));
            }
            if (payload.containsKey("rootSection")) {
                app.setRootSection((String) payload.get("rootSection"));
            }

            Application updatedApp = applicationService.updateApplication(app);
            return ResponseEntity.ok(updatedApp);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Update application root section
     * @param appId Application ID
     * @param payload Request payload containing rootSection
     * @return Updated application
     */
    @PutMapping("/{appId}/root-section")
    public ResponseEntity<?> updateRootSection(@PathVariable Long appId, @RequestBody Map<String, String> payload) {
        try {
            String rootSectionJson = payload.get("rootSection");
            if (rootSectionJson == null || rootSectionJson.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Root section JSON is required");
            }
            
            Application updatedApp = applicationService.updateRootSection(appId, rootSectionJson);
            return ResponseEntity.ok(updatedApp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    /**
     * Delete application
     * @param appId Application ID
     * @return Success message
     */
    @DeleteMapping("/{appId}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long appId) {
        try {
            Application app = applicationService.getApplicationById(appId);
            if (app == null) {
                return ResponseEntity.notFound().build();
            }
            
            applicationService.deleteApplication(appId);
            return ResponseEntity.ok("Application deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }
}

