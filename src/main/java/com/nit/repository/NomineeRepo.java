package com.nit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nit.entity.Nominee;

@Repository
public interface NomineeRepo extends JpaRepository<Nominee, Long> {
    List<Nominee> findByStatus(char status);  
    List<Nominee> findByProposal_Id(Long proposalId);
}
 