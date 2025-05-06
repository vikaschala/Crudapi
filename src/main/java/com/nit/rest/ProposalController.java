package com.nit.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import com.nit.dto.ProposalDto;
import com.nit.entity.Proposal;
import com.nit.entity.UserFilter;
import com.nit.entity.UserListing;
import com.nit.repository.ProposalRepo;
import com.nit.responsehandler.ResponseHandler;
import com.nit.service.ProposalService;

import lombok.experimental.PackagePrivate;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;
    
    @Autowired
    private ProposalRepo proposalRepo;

 
    @PostMapping("/add")
    public ResponseHandler createProposal(@RequestBody ProposalDto dto) {
        ResponseHandler response = new ResponseHandler();
        try {
            String data = proposalService.addProposal(dto);
            response.setStatus(true);
            response.setData(data);
            response.setMessage("Data saved successfully");
        }catch (IllegalArgumentException e) {
        	
        	response.setData(new ArrayList<>());
            response.setStatus(false);
            response.setMessage(e.getMessage());
         
        } catch (Exception e) {
        	
        	response.setData(new ArrayList<>());
            response.setStatus(false);
            response.setMessage(e.getMessage());
         
        }
        return response;
    }
  

    @GetMapping("/report")
    public ResponseHandler getAllProposals() {
        ResponseHandler response = new ResponseHandler();
        try {
            List<ProposalDto> proposals = proposalService.getAllActiveProposals();
            response.setStatus(true);
            response.setData(proposals);
            System.out.println(proposals);
            response.setMessage("All proposals fetched successfully");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

  
    @GetMapping("/list_by_id/{id}")
    public ResponseHandler getProposalById(@PathVariable Long id) {
        ResponseHandler response = new ResponseHandler();
        try {
        	ProposalDto proposal = proposalService.getByProposalId(id);
            if (proposal == null) {
                response.setStatus(false);
                response.setMessage("Proposal not found");
            } else {
                response.setStatus(true);
                response.setData(proposal);
                response.setMessage("Proposal fetched successfully");
            }
        } catch (Exception e) {
        	response.setData(new ArrayList<>());
            response.setStatus(false);
            response.setMessage(e.getMessage());
            e.printStackTrace(); 
        }
        return response;
    }
    
    @PutMapping("/update_by_id/{id}")
    public ResponseHandler updateProposal(@PathVariable Long id, @RequestBody ProposalDto dto) {
        ResponseHandler response = new ResponseHandler();
        try {
            Proposal updatedProposal = proposalService.updateProposal(id, dto);
            if (updatedProposal == null) {
                response.setStatus(false);
                response.setMessage("Proposal not found");
            } else {
                response.setStatus(true);
                response.setData(updatedProposal);
                response.setMessage("Proposal updated successfully");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
    
    @DeleteMapping("/delete_by_id/{id}")
    public ResponseHandler deleteById(@PathVariable Long id) {
        ResponseHandler response = new ResponseHandler();
        try {
            proposalService.deleteById(id); // soft delete all active proposals
            response.setStatus(true);
               response.setMessage("proposals"
            		+ " deleted successfully.");
        } catch (Exception e) {
            response.setStatus(false);
            response.setData(new ArrayList<>());
            response.setMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
    @PostMapping("/pagination")
    public ResponseHandler getProposers(@RequestBody UserListing listing) {
        ResponseHandler response = new ResponseHandler();
        try {
            // Extract UserFilter from the UserListing object
            UserFilter filter = listing.getUserFilter();

            // Fetch proposers based on listing and filter
          List<Proposal> proposers = proposalService.fetchAllProposerByListing(listing, filter);
          List<Proposal> proposer=proposalRepo.findAll();
     System.out.println(proposers);
            // Set response properties
            response.setData(proposers);
            response.setStatus(true);
            response.setMessage("Proposers fetched successfully.");
            response.setTotalRecord(proposer.size());

        } catch (IllegalArgumentException ex) {
            // Handle invalid arguments (like invalid input fields)
            response.setStatus(false);
            response.setMessage("Invalid input provided: " + ex.getMessage());
            response.setData(null);
            response.setTotalRecord(0);
        } catch (Exception ex) {
            // Log the error and set a general error message
            response.setStatus(false);
            response.setMessage("An error occurred while fetching proposers.");
            response.setData(null);
            response.setTotalRecord(0);
            
            // Log the exception for debugging purposes (optional but highly recommended)
            ex.printStackTrace();
        }
        return response;
    }

}