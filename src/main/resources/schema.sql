-- eCTD Database Schema
-- Create database
CREATE DATABASE IF NOT EXISTS ectd_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ectd_db;

-- eCTD 4.0 Application Table
-- This table stores the main application information.
CREATE TABLE IF NOT EXISTS `ectd_application` (
  `app_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Application global unique ID',
  `app_number` VARCHAR(50) NOT NULL COMMENT 'Application number (e.g., EMA/123456)',
  `app_type` VARCHAR(50) NOT NULL COMMENT 'Application type (e.g., NDA, BLA)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
  `root_section` JSON COMMENT 'Root Section tree structure, based on eCTD 4.0',
  `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT 'Application status',
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `uk_app_number` (`app_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='eCTD Application master table';

-- eCTD Submission Unit (SU) Table
-- This table stores each submission sequence for an application.
CREATE TABLE IF NOT EXISTS `ectd_submission_unit` (
  `su_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Submission Unit unique ID',
  `app_id` BIGINT NOT NULL COMMENT 'Foreign key to the parent application',
  `sequence_num` INT NOT NULL COMMENT 'Submission sequence number (e.g., 0001, 0002)',
  `effective_date` DATE NOT NULL COMMENT 'Effective date of the submission',
  `su_type` VARCHAR(32) NOT NULL COMMENT 'Submission Type (e.g., original, supplement)',
  `su_unit_type` VARCHAR(32) NOT NULL COMMENT 'Submission Unit Type (e.g., ectd-4-0)',
  `cou_data` JSON COMMENT 'Context of Use (CoU) data, describing the changes in this SU',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
  `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT 'Submission Unit status',
  PRIMARY KEY (`su_id`),
  KEY `idx_app_id` (`app_id`),
  UNIQUE KEY `uk_app_seq` (`app_id`, `sequence_num`),
  CONSTRAINT `fk_su_app` FOREIGN KEY (`app_id`) REFERENCES `ectd_application` (`app_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='eCTD Submission Unit table';

-- Insert sample data for testing
INSERT INTO `ectd_application` (`app_number`, `app_type`, `root_section`, `status`) VALUES
('NDA-202501', 'NDA', '{"id": 9999990, "nodeType": "application", "name": "NDA-202501", "children": [{"id": 8154, "nodeType": "module", "name": "1. Administrative information"}, {"id": 8155, "nodeType": "module", "name": "2. Overview and Summaries"}, {"id": 8156, "nodeType": "module", "name": "3. Quality"}, {"id": 8157, "nodeType": "module", "name": "4. Nonclinical Study Reports"}, {"id": 8158, "nodeType": "module", "name": "5. Clinical Study Reports"}]}', 'DRAFT'),
('BLA-202502', 'BLA', '{"id": 9999991, "nodeType": "application", "name": "BLA-202502", "children": [{"id": 8159, "nodeType": "module", "name": "1. Administrative information"}, {"id": 8160, "nodeType": "module", "name": "2. Overview and Summaries"}, {"id": 8161, "nodeType": "module", "name": "3. Quality"}, {"id": 8162, "nodeType": "module", "name": "4. Nonclinical Study Reports"}, {"id": 8163, "nodeType": "module", "name": "5. Clinical Study Reports"}]}', 'DRAFT');

INSERT INTO `ectd_submission_unit` (`app_id`, `sequence_num`, `effective_date`, `su_type`, `su_unit_type`, `cou_data`, `status`) VALUES
(1, 1, '2025-01-15', 'original', 'ectd-4-0', '{"operations": [{"type": "add", "nodeId": 8154, "documentPath": "/m1/admin-info.pdf"}]}', 'DRAFT'),
(1, 2, '2025-02-15', 'supplement', 'ectd-4-0', '{"operations": [{"type": "replace", "nodeId": 8155, "documentPath": "/m2/updated-summary.pdf"}]}', 'DRAFT'),
(2, 1, '2025-01-20', 'original', 'ectd-4-0', '{"operations": [{"type": "add", "nodeId": 8159, "documentPath": "/m1/bla-admin.pdf"}]}', 'DRAFT');

