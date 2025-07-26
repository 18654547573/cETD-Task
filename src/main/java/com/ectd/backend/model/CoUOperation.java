package com.ectd.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * CoU (Context of Use) 操作实体类
 * 表示对 eCTD 结构的单个操作记录
 */
public class CoUOperation {
    
    @JsonProperty("cou_id")
    private String couId;
    
    @JsonProperty("operation")
    private String operation; // add, replace, delete
    
    @JsonProperty("su_id")
    private String suId;
    
    @JsonProperty("target_node_id")
    private Long targetNodeId; // 目标节点ID
    
    @JsonProperty("target_xpath")
    private String targetXpath; // 目标节点XPath
    
    @JsonProperty("document")
    private DocumentInfo document;
    
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    @JsonProperty("operator")
    private String operator; // 操作人员
    
    @JsonProperty("description")
    private String description; // 操作描述
    
    // 内部类：文档信息
    public static class DocumentInfo {
        @JsonProperty("file_id")
        private String fileId;
        
        @JsonProperty("title")
        private String title;
        
        @JsonProperty("format")
        private String format;
        
        @JsonProperty("path")
        private String path;
        
        @JsonProperty("size")
        private Long size;
        
        // 构造函数
        public DocumentInfo() {}
        
        public DocumentInfo(String fileId, String title, String format, String path) {
            this.fileId = fileId;
            this.title = title;
            this.format = format;
            this.path = path;
        }
        
        // Getters and Setters
        public String getFileId() { return fileId; }
        public void setFileId(String fileId) { this.fileId = fileId; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        
        public Long getSize() { return size; }
        public void setSize(Long size) { this.size = size; }
    }
    
    // 构造函数
    public CoUOperation() {
        this.timestamp = LocalDateTime.now();
    }
    
    public CoUOperation(String operation, String suId, Long targetNodeId, DocumentInfo document) {
        this();
        this.operation = operation;
        this.suId = suId;
        this.targetNodeId = targetNodeId;
        this.document = document;
        this.couId = generateCouId();
    }
    
    // 生成CoU ID
    private String generateCouId() {
        return "COU_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getCouId() { return couId; }
    public void setCouId(String couId) { this.couId = couId; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    
    public String getSuId() { return suId; }
    public void setSuId(String suId) { this.suId = suId; }
    
    public Long getTargetNodeId() { return targetNodeId; }
    public void setTargetNodeId(Long targetNodeId) { this.targetNodeId = targetNodeId; }
    
    public String getTargetXpath() { return targetXpath; }
    public void setTargetXpath(String targetXpath) { this.targetXpath = targetXpath; }
    
    public DocumentInfo getDocument() { return document; }
    public void setDocument(DocumentInfo document) { this.document = document; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return "CoUOperation{" +
                "couId='" + couId + '\'' +
                ", operation='" + operation + '\'' +
                ", suId='" + suId + '\'' +
                ", targetNodeId=" + targetNodeId +
                ", targetXpath='" + targetXpath + '\'' +
                ", document=" + document +
                ", timestamp=" + timestamp +
                ", operator='" + operator + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

