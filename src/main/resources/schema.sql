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
INSERT INTO `ectd_db`.`ectd_application` (`app_id`, `app_number`, `app_type`, `created_at`, `updated_at`, `root_section`, `status`) VALUES (1, 'NDA-202501', 'NDA', '2025-07-25 14:13:42', '2025-07-26 16:47:14', '[{\"id\": 9999991, \"name\": \"1.BLA-202502\", \"children\": [{\"id\": 8159, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.1 行政信息（Administrative Information）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8164, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.1.1 申请表（Application Form）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 1753438778595, \"lev\": \"层级1\", \"pid\": 8164, \"name\": \"测试文档\", \"path\": \"123\", \"format\": \"PDF\", \"nodeType\": \"document\"}], \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8160, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.2. 标签和说明书（Labelling）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8165, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.2.1 包装说明书（Package Insert/SPC/SmPC）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8161, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.3. 专利与专营权信息（Patent & Exclusivity Data）*\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8166, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.3.1 专利声明（Patent Declaration）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8162, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.4. 环境风险评估（Environmental Risk Assessment, ERA）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8167, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.4.1 Administrative information\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8163, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.5. 地区特殊要求\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8168, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.5.1 Administrative information\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"application\"}]', 'DRAFT');
INSERT INTO `ectd_db`.`ectd_application` (`app_id`, `app_number`, `app_type`, `created_at`, `updated_at`, `root_section`, `status`) VALUES (2, 'BLA-202502', 'BLA', '2025-07-25 14:13:42', '2025-07-26 16:47:09', '[{\"id\": 9999991, \"name\": \"1.BLA-202502\", \"children\": [{\"id\": 8159, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.1 行政信息（Administrative Information）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8164, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.1.1 申请表（Application Form）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 1753438778595, \"lev\": \"层级1\", \"pid\": 8164, \"name\": \"测试文档\", \"path\": \"123\", \"format\": \"PDF\", \"nodeType\": \"document\"}], \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8160, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.2. 标签和说明书（Labelling）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8165, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.2.1 包装说明书（Package Insert/SPC/SmPC）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8161, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.3. 专利与专营权信息（Patent & Exclusivity Data）*\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8166, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.3.1 专利声明（Patent Declaration）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8162, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.4. 环境风险评估（Environmental Risk Assessment, ERA）\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8167, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.4.1 Administrative information\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}, {\"id\": 8163, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.5. 地区特殊要求\", \"type\": \"类型\", \"xpath\": \"XPath\", \"children\": [{\"id\": 8168, \"lev\": \"层级\", \"code\": \"代码\", \"name\": \"1.5.1 Administrative information\", \"type\": \"类型\", \"xpath\": \"XPath\", \"nodeType\": \"section\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"module\", \"ectdElement\": \"eCTD元素\"}], \"nodeType\": \"application\"}]', 'DRAFT');


INSERT INTO `ectd_db`.`ectd_submission_unit` (`su_id`, `app_id`, `sequence_num`, `effective_date`, `su_type`, `su_unit_type`, `cou_data`, `created_at`, `updated_at`, `status`) VALUES (1, 1, 1, '2025-01-15', 'original', 'ectd-4-0', '{\"operations\": [{\"type\": \"add\", \"nodeId\": 8154, \"documentPath\": \"/m1/admin-info.pdf\"}]}', '2025-07-25 14:13:42', '2025-07-25 14:13:42', 'DRAFT');
INSERT INTO `ectd_db`.`ectd_submission_unit` (`su_id`, `app_id`, `sequence_num`, `effective_date`, `su_type`, `su_unit_type`, `cou_data`, `created_at`, `updated_at`, `status`) VALUES (2, 1, 2, '2025-02-15', 'supplement', 'ectd-4-0', '{\"operations\": [{\"type\": \"replace\", \"nodeId\": 8155, \"documentPath\": \"/m2/updated-summary.pdf\"}]}', '2025-07-25 14:13:42', '2025-07-25 14:13:42', 'DRAFT');
INSERT INTO `ectd_db`.`ectd_submission_unit` (`su_id`, `app_id`, `sequence_num`, `effective_date`, `su_type`, `su_unit_type`, `cou_data`, `created_at`, `updated_at`, `status`) VALUES (3, 2, 1, '2025-01-20', 'original', 'ectd-4-0', '[{\"su_id\": \"214214\", \"cou_id\": \"COU_1234567890_123\", \"document\": {\"path\": \"/m1/bla-admin.pdf\", \"title\": \"临床研究报告\", \"format\": \"PDF\", \"file_id\": \"doc-98765\"}, \"operator\": \"张三\", \"operation\": \"add\", \"description\": \"在节点1.1下添加新的临床研究报告1122\", \"target_xpath\": \"us_1/us_1.1\", \"target_node_id\": 8159}]', '2025-07-25 14:13:42', '2025-07-26 16:36:47', 'DRAFT');


