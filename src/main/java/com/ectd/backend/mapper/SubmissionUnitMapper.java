package com.ectd.backend.mapper;

import com.ectd.backend.model.SubmissionUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Submission Unit Mapper Interface
 * Provides database operations for eCTD Submission Units
 */
@Mapper
public interface SubmissionUnitMapper {
    
    /**
     * Find submission unit by ID
     * @param suId Submission Unit ID
     * @return SubmissionUnit entity
     */
    SubmissionUnit findById(@Param("suId") Long suId);
    
    /**
     * Find all submission units by application ID
     * @param appId Application ID
     * @return List of submission units
     */
    List<SubmissionUnit> findByAppId(@Param("appId") Long appId);
    
    /**
     * Find submission unit by application ID and sequence number
     * @param appId Application ID
     * @param sequenceNum Sequence number
     * @return SubmissionUnit entity
     */
    SubmissionUnit findByAppIdAndSequence(@Param("appId") Long appId, @Param("sequenceNum") Integer sequenceNum);
    
    /**
     * Find all submission units
     * @return List of submission units
     */
    List<SubmissionUnit> findAll();
    
    /**
     * Insert new submission unit
     * @param submissionUnit SubmissionUnit entity
     * @return Number of affected rows
     */
    int insert(SubmissionUnit submissionUnit);
    
    /**
     * Update submission unit
     * @param submissionUnit SubmissionUnit entity
     * @return Number of affected rows
     */
    int update(SubmissionUnit submissionUnit);
    
    /**
     * Delete submission unit by ID
     * @param suId Submission Unit ID
     * @return Number of affected rows
     */
    int deleteById(@Param("suId") Long suId);
    
    /**
     * Get next sequence number for an application
     * @param appId Application ID
     * @return Next sequence number
     */
    Integer getNextSequenceNum(@Param("appId") Long appId);
}

