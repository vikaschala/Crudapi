package com.nit.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.nit.dto.UserListing;
import com.nit.entity.Proposal;
import com.nit.entity.UserFilter;
import com.nit.repository.ProposalRepo;
import com.nit.responsehandler.ResponseHandler;
import com.nit.service.ProposalService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

	@Autowired      
	private ProposalService proposalService;

	@Autowired
	private ProposalRepo proposalRepo;

	@Autowired
	private JwtUtil jwtUtil;
	
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
	public ResponseHandler getAllProposals(HttpServletRequest request) {
	    ResponseHandler response = new ResponseHandler();

	    try {
	        String authHeader = request.getHeader("Authorization");

	        if (authHeader != null && authHeader.startsWith("Bearer")) {
	            String token = authHeader.substring(7); // Remove "Bearer " prefix
	            String emailId = jwtUtil.extractEmailId(token);
	            String fullName = jwtUtil.extractFullName(token);
	            String role = jwtUtil.extractUserRole(token);
	            String username=jwtUtil.extractUsername(token);
	        
	            List<Proposal> proposals = proposalRepo.findAll();

	            Map<String, Object> responseData = new HashMap<>();
	            Map<String, Object> userInfo = new HashMap<>();
	            userInfo.put("username", username);
	            userInfo.put("fullName", fullName);
	            userInfo.put("emailId", emailId);
	            userInfo.put("userRole", role);

	            responseData.put("userInfo", userInfo);
	            responseData.put("proposals", proposals);

	            response.setData(responseData);
	            response.setStatus(true);
	            response.setMessage("Data extracted and proposals retrieved successfully.");
	            response.setTotalRecord(proposals.size());
	        } else {
	            response.setStatus(false);
	            response.setMessage("Invalid Authorization header.");
	            response.setData(null);
	            response.setTotalRecord(0);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus(false);
	        response.setMessage("Unexpected error: " + e.getMessage());
	        response.setData(null);
	        response.setTotalRecord(0);
	    }

	    return response;
	}

	@GetMapping("/list/{id}")
	public ResponseHandler getProposalById(@PathVariable Long id) {
		ResponseHandler response = new ResponseHandler();
		try {
			ProposalDto proposalDto = proposalService.getByProposalId(id);
			if (proposalDto == null) {
				response.setStatus(false);
				response.setMessage("Proposal not found");
			} else {
				response.setStatus(true);
				response.setData(proposalDto);
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

	@PutMapping("/update/proposal_info/{id}")
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

	@DeleteMapping("/list/{id}")
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
	@PostMapping("/listing")
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



	@GetMapping("/export_excel")
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
	@PostMapping(value = "/import_excel", consumes = "multipart/form-data")
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


	@PostMapping(value = "/import_excel_batch", consumes = "multipart/form-data")
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