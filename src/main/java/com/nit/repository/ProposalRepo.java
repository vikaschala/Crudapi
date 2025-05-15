package com.nit.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nit.entity.Proposal;
@Repository
public interface ProposalRepo extends JpaRepository<Proposal, Long> {
  
    List<Proposal> findByStatus(char status);
    boolean existsByEmailId(String emailId);
    boolean existsByMobileNumber(Long number);
    boolean existsByPanNumber(String panNumber);

}
