package com.nit.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.ProcessingQueue;

public interface ProcessingQueueRepo extends JpaRepository<ProcessingQueue, Long> {
    List<ProcessingQueue> findByIsProcessedAndTotalCountLessThan(String isProcessed, int totalCount);
}
