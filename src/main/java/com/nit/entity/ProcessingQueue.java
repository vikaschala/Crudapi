package com.nit.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "processing_queue")
public class ProcessingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCount;

    private String filePath;

    private String isProcessed;

    private String status;

    private int rowRead = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(String isProcessed) {
		this.isProcessed = isProcessed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRowRead() {
		return rowRead;
	}

	public void setRowRead(int rowRead) {
		this.rowRead = rowRead;
	}

    // Getters and setters
}
