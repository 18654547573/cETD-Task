package com.ectd.backend.service;

import com.ectd.backend.mapper.ApplicationMapper;
import com.ectd.backend.model.Application;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Application Service
 * Business logic for eCTD Application management
 */
@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Create a new eCTD Application
     * @param appNumber Application number
     * @param appType Application type
     * @return Created application
     * @throws JsonProcessingException If JSON processing fails
     */
    public Application createApplication(String appNumber, String appType) throws JsonProcessingException {
        // Check if application number already exists
        if (applicationMapper.findByAppNumber(appNumber) != null) {
            throw new IllegalArgumentException("Application number already exists: " + appNumber);
        }

        Application app = new Application();
        app.setAppNumber(appNumber);
        app.setAppType(appType);
        app.setStatus("DRAFT");

        // Create initial Root Section structure based on eCTD 4.0
        Map<String, Object> rootSection = createInitialRootSection(appNumber);
        app.setRootSection(objectMapper.writeValueAsString(rootSection));

        applicationMapper.insert(app);
        return app;
    }

    /**
     * Get application by ID
     * @param appId Application ID
     * @return Application entity
     */
    public Application getApplicationById(Long appId) {
        return applicationMapper.findById(appId);
    }

    /**
     * Get application by number
     * @param appNumber Application number
     * @return Application entity
     */
    public Application getApplicationByNumber(String appNumber) {
        return applicationMapper.findByAppNumber(appNumber);
    }

    /**
     * Get all applications
     * @return List of applications
     */
    public List<Application> getAllApplications() {
        return applicationMapper.findAll();
    }

    /**
     * Update application
     * @param application Application entity
     * @return Updated application
     */
    public Application updateApplication(Application application) {
        applicationMapper.update(application);
        return applicationMapper.findById(application.getAppId());
    }

    /**
     * Delete application by ID
     * @param appId Application ID
     */
    public void deleteApplication(Long appId) {
        applicationMapper.deleteById(appId);
    }

    /**
     * Update application root section
     * @param appId Application ID
     * @param rootSectionJson Root section JSON string
     * @return Updated application
     * @throws JsonProcessingException If JSON processing fails
     */
    public Application updateRootSection(Long appId, String rootSectionJson) throws JsonProcessingException {
        // Validate JSON format
        objectMapper.readTree(rootSectionJson);
        
        Application app = applicationMapper.findById(appId);
        if (app == null) {
            throw new IllegalArgumentException("Application not found: " + appId);
        }
        
        app.setRootSection(rootSectionJson);
        applicationMapper.update(app);
        return app;
    }

    /**
     * Create initial Root Section structure based on eCTD 4.0
     * @param appName Application name
     * @return Root section map
     */
    private Map<String, Object> createInitialRootSection(String appName) {
        return Map.of(
            "id", 9999990,
            "nodeType", "application",
            "name", appName,
            "children", List.of(
                Map.of(
                    "id", 8154,
                    "nodeType", "module",
                    "name", "1. Administrative information",
                    "children", List.of()
                ),
                Map.of(
                    "id", 8155,
                    "nodeType", "module",
                    "name", "2. Overview and Summaries",
                    "children", List.of()
                ),
                Map.of(
                    "id", 8156,
                    "nodeType", "module",
                    "name", "3. Quality",
                    "children", List.of()
                ),
                Map.of(
                    "id", 8157,
                    "nodeType", "module",
                    "name", "4. Nonclinical Study Reports",
                    "children", List.of()
                ),
                Map.of(
                    "id", 8158,
                    "nodeType", "module",
                    "name", "5. Clinical Study Reports",
                    "children", List.of()
                )
            )
        );
    }
}

