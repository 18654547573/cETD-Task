package com.ectd.backend.mapper;

import com.ectd.backend.model.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Application Mapper Interface
 * Provides database operations for eCTD Applications
 */
@Mapper
public interface ApplicationMapper {
    
    /**
     * Find application by application number
     * @param appNumber Application number
     * @return Application entity
     */
    Application findByAppNumber(@Param("appNumber") String appNumber);
    
    /**
     * Find application by ID
     * @param appId Application ID
     * @return Application entity
     */
    Application findById(@Param("appId") Long appId);
    
    /**
     * Find all applications
     * @return List of applications
     */
    List<Application> findAll();
    
    /**
     * Insert new application
     * @param application Application entity
     * @return Number of affected rows
     */
    int insert(Application application);
    
    /**
     * Update application
     * @param application Application entity
     * @return Number of affected rows
     */
    int update(Application application);
    
    /**
     * Delete application by ID
     * @param appId Application ID
     * @return Number of affected rows
     */
    int deleteById(@Param("appId") Long appId);
    
    /**
     * Count total applications
     * @return Total count
     */
    int count();
}

