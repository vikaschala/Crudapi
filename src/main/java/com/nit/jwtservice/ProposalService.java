package com.nit.jwtservice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nit.dto.ProposalDto;
import com.nit.entity.Proposal;
import com.nit.entity.UserFilter;
import com.nit.entity.UserListing;

import jakarta.servlet.http.HttpServletResponse;


public interface ProposalService {  
	public String addProposal(ProposalDto proposal); 

	public List<ProposalDto> getAllActiveProposals();

	public ProposalDto getByProposalId(Long id); 

	public Proposal updateProposal(Long id,ProposalDto proposal); 

	public void deleteById(Long id); 
	
    public List<Proposal> fetchAllProposerByListing(UserListing listing, UserFilter filter);

//    public void exportDataIntoExcel(HttpServletResponse response);
	public String exportDataIntoExcel(HttpServletResponse response) throws IOException;
     public String importExcel(MultipartFile file) throws IOException;
     public String excelBatchProcessing(MultipartFile file)throws IOException ;
     public void processBatchFiles() throws FileNotFoundException;

}
