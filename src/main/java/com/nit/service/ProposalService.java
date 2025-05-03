package com.nit.service;

import java.util.List;

import com.nit.dto.ProposalDto;
import com.nit.entity.Proposal;

public interface ProposalService {  
	public String addProposal(ProposalDto proposal); 

	public List<ProposalDto> getAllActiveProposals();

	public ProposalDto getByProposalId(Long id); 

	public Proposal updateProposal(Long id,ProposalDto proposal); 

	public void deleteById(Long id); 

}
