package com.nit.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nit.JwtUtil.JwtUtil;
import com.nit.dto.ProposalDto;
import com.nit.entity.Proposal;
import com.nit.entity.UserFilter;
import com.nit.entity.UserListing;
import com.nit.model.AuthenticationRequest;
import com.nit.model.AuthenticationResponse;
import com.nit.repository.ProposalRepo;
import com.nit.responsehandler.ResponseHandler;
import com.nit.service.ProposalService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

	@Autowired      
	private ProposalService proposalService;

	@Autowired
	private ProposalRepo proposalRepo;
	  @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private UserDetailsService userDetailsService; 

	    @Autowired
	    private JwtUtil jwtTokenUtil; 

	    @PostMapping("/authenticate")
	    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
	        try {
	         
	            authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                	
	                    authenticationRequest.getUsername(), 
	                    authenticationRequest.getPassword()
	                 
	                )
	               
	            );
	        } catch (BadCredentialsException e) {
	          
	          throw new IllegalArgumentException("Incorrect username or password\", HttpStatus.UNAUTHORIZED");
	        }

	        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

	        final String jwt = jwtTokenUtil.generateToken(userDetails);

	        return ResponseHandler.generateResponse("Authentication successful", HttpStatus.OK, new AuthenticationResponse(jwt));
	    }

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
	
	
	
	@GetMapping("/fileExport")
	public ResponseHandler exportProposersToExcel(HttpServletResponse response) throws IOException,ServletException{
		ResponseHandler handler = new ResponseHandler();
		try {
			
			String filepath = proposalService.exportDataIntoExcel(response);
			handler.setMessage("File exported successfully.");
			handler.setStatus(true);
			handler.setData(filepath); // No data to return, just the success message
		} catch (Exception e) {
			// In case of failure, return a failure message
			handler.setMessage("Failed: " + e.getMessage());
			handler.setStatus(false);
		}

		return handler;
	}
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseHandler importProposalsFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
	    ResponseHandler handler = new ResponseHandler();

	    if (file.isEmpty()) {
	        handler.setStatus(false);
	        handler.setMessage("Uploaded file is empty.");
	        return handler;
	    }
	    try {
	        String result = proposalService.importExcel(file);
	        handler.setStatus(true);
	        handler.setMessage("File imported successfully.");
	        handler.setData(result); // Assuming you have a setData method
	    } catch (Exception e) {
	        handler.setStatus(false);
	        handler.setMessage("Error occurred while importing file: " + e.getMessage());
	    }

	    return handler;
	}
	
	
	 @PostMapping(value = "/upload1", consumes = "multipart/form-data")
	    public ResponseHandler uploadExcel(@RequestParam("file") MultipartFile file) {
	        ResponseHandler response = new ResponseHandler();

	        if (file.isEmpty()) {
	            response.setStatus(false);
	            response.setMessage("Uploaded file is empty.");
	            return response;
	        }

	        try {
	            String result = proposalService.excelBatchProcessing(file);
	            response.setStatus(true);
	            response.setMessage("File queued for batch processing.");
	            response.setData(result);
	        } catch (IOException e) {
	            response.setStatus(false);
	            response.setMessage("Error processing file: " + e.getMessage());
	        }

	        return response;
	    }
}