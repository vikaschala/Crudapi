package com.nit.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.dto.ProposalDto;
import com.nit.entity.Proposal;
import com.nit.responsehandler.ResponseHandler;
import com.nit.service.ProposalService;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

 
    @PostMapping("/proposals")
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
  

    @GetMapping("/proposals/report")
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

  
    @GetMapping("/proposals/{id}")
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
    
    @PutMapping("/proposals/{id}")
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
    
    @DeleteMapping("/proposals/{id}")
    public ResponseHandler deleteById(@PathVariable Long id) {
        ResponseHandler response = new ResponseHandler();
        try {
            proposalService.deleteById(id); // soft delete all active proposals
            response.setStatus(true);
            response.setData("active proposals deleted.");
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
}
