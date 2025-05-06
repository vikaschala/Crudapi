package com.nit.serviceimpl;

import java.beans.PropertyChangeListenerProxy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nit.dto.NomineeDto;
import com.nit.dto.ProposalDto;
import com.nit.entity.Nominee;
import com.nit.entity.Proposal;
import com.nit.entity.UserFilter;
import com.nit.entity.UserListing;
import com.nit.repository.NomineeRepo;
import com.nit.repository.ProposalRepo;
import com.nit.service.ProposalService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


@Service
public class ProposalServiceImpl implements ProposalService {

	@Autowired
	private ProposalRepo proposalRepo;
	@Autowired 
	private NomineeRepo nomineeRepo;
	@Autowired
	private EntityManager entityManager;


	//add Proposal Without annotation
/*	@Override
	public String addProposal(ProposalDto dto) {
		List<String> errors = new ArrayList<>();
		if (dto.getFirstName() == null || dto.getFirstName().isBlank())
			errors.add("First name is required");

		if (dto.getMiddleName() == null || dto.getMiddleName().isEmpty())
			errors.add("Middle name is required");

		if (dto.getLastName() == null || dto.getLastName().isEmpty())
			errors.add("Last name is required");

		if (dto.getPanNumber() == null || dto.getPanNumber().isEmpty() || 
				!dto.getPanNumber().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}"))
			errors.add("Invalid PAN number format");

		if (dto.getAnnualIncome() == null || dto.getAnnualIncome() < 10000)
			errors.add("Annual income must be at least 10,000");

		if (dto.getEmailId() == null || dto.getEmailId().isBlank() || 
				!dto.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
			errors.add("Invalid email format");

		if (dto.getMobileNumber() == null || 
				!dto.getMobileNumber().toString().matches("^91[789]\\d{9}$"))
			errors.add("Invalid mobile number");

		if (dto.getAlternateMobileNumber() == null || 
				!dto.getAlternateMobileNumber().toString().matches("^91[789]\\d{9}$"))
			errors.add("Invalid alternate mobile number");

		if (dto.getDateOfBirth() == null || dto.getDateOfBirth().isAfter(LocalDate.now()))
			errors.add("Date of Birth cannot be in the future");

		if (dto.getCity() == null || dto.getCity().isEmpty())
			errors.add("City cannot be empty");

		if (dto.getPincode() == null || !dto.getPincode().matches("^[1-9][0-9]{5}$"))
			errors.add("Invalid pincode");

		if (dto.getAddress() == null || dto.getAddress().isEmpty())
			errors.add("Address cannot be empty");

		if (dto.getGender() == null)
			errors.add("Gender cannot be null");

		if (dto.getMaritalStatus() == null)
			errors.add("Marital status cannot be null");

		if (dto.getNationality() == null)
			errors.add("Nationality cannot be null");

		if (!errors.isEmpty()) {
			throw new IllegalArgumentException(String.join(", ", errors));
		}
		//Duplicate Check
		if (proposalRepo.existsByEmailId(dto.getEmailId())) {
			throw new IllegalArgumentException("Email ID " + dto.getEmailId() + " is already in use.");
		}

		if (proposalRepo.existsByMobileNumber(dto.getMobileNumber())) {
			throw new IllegalArgumentException("Mobile number " + dto.getMobileNumber() + " is already in use.");
		}

		if (proposalRepo.existsByPanNumber(dto.getPanNumber())) {
			throw new IllegalArgumentException("PAN number " + dto.getPanNumber() + " is already in use.");
		}

		// === SETTING VALUES ===
		Proposal proposal = new Proposal();
		proposal.setFirstName(dto.getFirstName());
		proposal.setMiddleName(dto.getMiddleName());
		proposal.setLastName(dto.getLastName());
		proposal.setPanNumber(dto.getPanNumber());
		proposal.setAnnualIncome(dto.getAnnualIncome());
		proposal.setEmailId(dto.getEmailId());
		proposal.setMobileNumber(dto.getMobileNumber());
		proposal.setAlternateMobileNumber(dto.getAlternateMobileNumber());
		proposal.setDateOfBirth(dto.getDateOfBirth());
		proposal.setCity(dto.getCity());
		proposal.setPincode(dto.getPincode());
		proposal.setAddress(dto.getAddress());
		proposal.setGender(dto.getGender());
		proposal.setMaritalStatus(dto.getMaritalStatus());
		proposal.setNationality(dto.getNationality());
		proposal.setStatus('Y');

		Proposal savedProposal = proposalRepo.save(proposal);

		List<NomineeDto> listOfData = dto.getNominee(); // Assuming this is a List<NomineeDto>
		if (listOfData != null && !listOfData.isEmpty()) {
			for (NomineeDto nomineeDto : listOfData) {
				if (nomineeDto != null) {
					Nominee nominee = new Nominee();
					nominee.setFirstName(nomineeDto.getFirstName());
					nominee.setMiddleName(nomineeDto.getMiddleName());
					nominee.setLastName(nomineeDto.getLastName());
					nominee.setMobileNumber(nomineeDto.getMobileNumber());

					nominee.setUserId(savedProposal.getId()); // Foreign key
					nominee.setRealtionship(nomineeDto.getRelationship());
					nominee.setStatus('Y');
					nomineeRepo.save(nominee);
				}
			}
		}
		return "Proposal and nominee(s) saved successfully!";
	} */
	public String addProposal(ProposalDto dto) {
		List<String> errors = new ArrayList<>();
		if (dto.getFirstName() == null || dto.getFirstName().isBlank())
			errors.add("First name is required");

		if (dto.getMiddleName() == null || dto.getMiddleName().isEmpty())
			errors.add("Middle name is required");

		if (dto.getLastName() == null || dto.getLastName().isEmpty())
			errors.add("Last name is required");

		if (dto.getPanNumber() == null || dto.getPanNumber().isEmpty() || 
				!dto.getPanNumber().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}"))
			errors.add("Invalid PAN number format");

		if (dto.getAnnualIncome() == null || dto.getAnnualIncome() < 10000)
			errors.add("Annual income must be at least 10,000");

		if (dto.getEmailId() == null || dto.getEmailId().isBlank() || 
				!dto.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
			errors.add("Invalid email format");

		if (dto.getMobileNumber() == null || 
				!dto.getMobileNumber().toString().matches("^91[789]\\d{9}$"))
			errors.add("Invalid mobile number");

		if (dto.getAlternateMobileNumber() == null || 
				!dto.getAlternateMobileNumber().toString().matches("^91[789]\\d{9}$"))
			errors.add("Invalid alternate mobile number");

		if (dto.getDateOfBirth() == null || dto.getDateOfBirth().isAfter(LocalDate.now()))
			errors.add("Date of Birth cannot be in the future");

		if (dto.getCity() == null || dto.getCity().isEmpty())
			errors.add("City cannot be empty");

		if (dto.getPincode() == null || !dto.getPincode().matches("^[1-9][0-9]{5}$"))
			errors.add("Invalid pincode");

		if (dto.getAddress() == null || dto.getAddress().isEmpty())
			errors.add("Address cannot be empty");

		if (dto.getGender() == null)
			errors.add("Gender cannot be null");

		if (dto.getMaritalStatus() == null)
			errors.add("Marital status cannot be null");

		if (dto.getNationality() == null)
			errors.add("Nationality cannot be null");

		if (!errors.isEmpty()) {
			throw new IllegalArgumentException(String.join(", ", errors));
		}
		//Duplicate Check
		if (proposalRepo.existsByEmailId(dto.getEmailId())) {
			throw new IllegalArgumentException("Email ID " + dto.getEmailId() + " is already in use.");
		}

		if (proposalRepo.existsByMobileNumber(dto.getMobileNumber())) {
			throw new IllegalArgumentException("Mobile number " + dto.getMobileNumber() + " is already in use.");
		}

		if (proposalRepo.existsByPanNumber(dto.getPanNumber())) {
			throw new IllegalArgumentException("PAN number " + dto.getPanNumber() + " is already in use.");
		}

		// === SETTING VALUES ===
		Proposal proposal = new Proposal();
		proposal.setFirstName(dto.getFirstName());
		proposal.setMiddleName(dto.getMiddleName());
		proposal.setLastName(dto.getLastName());
		proposal.setPanNumber(dto.getPanNumber());
		proposal.setAnnualIncome(dto.getAnnualIncome());
		proposal.setEmailId(dto.getEmailId());
		proposal.setMobileNumber(dto.getMobileNumber());
		proposal.setAlternateMobileNumber(dto.getAlternateMobileNumber());
		proposal.setDateOfBirth(dto.getDateOfBirth());
		proposal.setCity(dto.getCity());
		proposal.setPincode(dto.getPincode());
		proposal.setAddress(dto.getAddress());
		proposal.setGender(dto.getGender());
		proposal.setMaritalStatus(dto.getMaritalStatus());
		proposal.setNationality(dto.getNationality());
		proposal.setStatus('Y');

		Proposal savedProposal = proposalRepo.save(proposal);

		List<NomineeDto> listOfData = dto.getNominee(); // Assuming this is a List<NomineeDto>
		if (listOfData != null && !listOfData.isEmpty()) {
			for (NomineeDto nomineeDto : listOfData) {
				if (nomineeDto != null) {
					Nominee nominee = new Nominee();
					nominee.setFirstName(nomineeDto.getFirstName());
					nominee.setMiddleName(nomineeDto.getMiddleName());
					nominee.setLastName(nomineeDto.getLastName());
					nominee.setMobileNumber(nomineeDto.getMobileNumber());
					nominee.setRealtionship(nomineeDto.getRelationship());
					nominee.setProposal(savedProposal); // Foreign key
			
					nominee.setStatus('Y');
					nomineeRepo.save(nominee);
				}
			}
		}
		return "Proposal and nominee(s) saved successfully!";
	} 

	@Override
	public List<ProposalDto> getAllActiveProposals() {

		// Fetch all active proposals from the repository
		List<Proposal> activeProposals = proposalRepo.findByStatus('N');
		List<ProposalDto> proposalDtoList = new ArrayList<>();

		// Map each active proposal to ProposalDto
		for (Proposal proposal : activeProposals) {

			ProposalDto dto = new ProposalDto();
			dto.setFirstName(proposal.getFirstName());
			dto.setMiddleName(proposal.getMiddleName());
			dto.setLastName(proposal.getLastName());
			dto.setPanNumber(proposal.getPanNumber());
			dto.setAnnualIncome(proposal.getAnnualIncome());
			dto.setEmailId(proposal.getEmailId());
			dto.setMobileNumber(proposal.getMobileNumber());
			dto.setAlternateMobileNumber(proposal.getAlternateMobileNumber());
			dto.setDateOfBirth(proposal.getDateOfBirth());
			dto.setCity(proposal.getCity());
			dto.setPincode(proposal.getPincode());
			dto.setAddress(proposal.getAddress());
			dto.setGender(proposal.getGender());
			dto.setMaritalStatus(proposal.getMaritalStatus());
			dto.setNationality(proposal.getNationality());

			proposalDtoList.add(dto);
		}

		return proposalDtoList;
	}

	@Override
	public ProposalDto getByProposalId(Long id) {
		Proposal p = proposalRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Proposal with ID " + id + " not found"));

		ProposalDto dto = new ProposalDto();
		dto.setFirstName(p.getFirstName());
		dto.setMiddleName(p.getMiddleName());
		dto.setLastName(p.getLastName());
		dto.setPanNumber(p.getPanNumber());
		dto.setAnnualIncome(p.getAnnualIncome());
		dto.setEmailId(p.getEmailId());
		dto.setMobileNumber(p.getMobileNumber());
		dto.setAlternateMobileNumber(p.getAlternateMobileNumber());
		dto.setDateOfBirth(p.getDateOfBirth());
		dto.setCity(p.getCity());
		dto.setPincode(p.getPincode());
		dto.setAddress(p.getAddress());
		dto.setGender(p.getGender());
		dto.setMaritalStatus(p.getMaritalStatus());
		dto.setNationality(p.getNationality());

		return dto;
	}

	@Override
	public Proposal updateProposal(Long id, ProposalDto dto) {
		Proposal proposal = proposalRepo.findById(id).orElse(null);
		if (proposal == null) {
			return null;
		}

		// Update only if data is not null or not blank
		if (dto.getFirstName() != null && !dto.getFirstName().trim().isEmpty()) {
			proposal.setFirstName(dto.getFirstName());
		}
		if (dto.getLastName() != null && !dto.getLastName().trim().isEmpty()) {
			proposal.setLastName(dto.getLastName());
		}
		if (dto.getMiddleName() != null && !dto.getMiddleName().trim().isEmpty()) {
			proposal.setMiddleName(dto.getMiddleName());
		}
		if (dto.getPanNumber() != null && !dto.getPanNumber().trim().isEmpty()) {
			proposal.setPanNumber(dto.getPanNumber());
		}
		if (dto.getAnnualIncome() != null) {
			proposal.setAnnualIncome(dto.getAnnualIncome());
		}
		if (dto.getEmailId() != null && !dto.getEmailId().trim().isEmpty()) {
			proposal.setEmailId(dto.getEmailId());
		}
		if (dto.getMobileNumber() != null && !dto.getMobileNumber().toString().trim().isEmpty()) {
			proposal.setMobileNumber(dto.getMobileNumber());
		}
		if (dto.getAlternateMobileNumber() != null && !dto.getAlternateMobileNumber().toString().trim().isEmpty()) {
			proposal.setAlternateMobileNumber(dto.getAlternateMobileNumber());
		}
		if (dto.getDateOfBirth() != null) {
			proposal.setDateOfBirth(dto.getDateOfBirth());
		}
		if (dto.getCity() != null && !dto.getCity().trim().isEmpty()) {
			proposal.setCity(dto.getCity());
		}
		if (dto.getPincode() != null && !dto.getPincode().trim().isEmpty()) {
			proposal.setPincode(dto.getPincode());
		}
		if (dto.getAddress() != null && !dto.getAddress().trim().isEmpty()) {
			proposal.setAddress(dto.getAddress());
		}
		if (dto.getGender() != null && !dto.getGender().toString().trim().isEmpty()) {
			proposal.setGender(dto.getGender());
		}
		if (dto.getMaritalStatus() != null && !dto.getMaritalStatus().toString().trim().isEmpty()) {
			proposal.setMaritalStatus(dto.getMaritalStatus());
		}
		if (dto.getNationality() != null && !dto.getNationality().toString().trim().isEmpty()) {
			proposal.setNationality(dto.getNationality());
		}

		return proposalRepo.save(proposal);
	}

	@Override
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("Invalid ID. ID must be a positive number.");
		}
		Proposal proposal = proposalRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Proposal with ID " + id + " not found."));
		if (proposal.getStatus() != null && proposal.getStatus() == 'N') {
			throw new RuntimeException("Proposal with ID " + id + " is already inactive.");
		}
		proposal.setStatus('N');
		proposalRepo.save(proposal);
	
//		List<Nominee> nomineeList = nomineeRepo.findByUserId(proposal.getId());
//		for (Nominee nominee : nomineeList) {
//			nominee.setStatus('N'); // Set nominee status to 'N' (inactive)
//		}
//		nomineeRepo.saveAll(nomineeList);
	
		System.out.println("Proposal with ID " + id + " marked as inactive successfully.");
	}

	//Using row Query
	/*
	 * 	  @Override
		public List<Proposal> fetchAllProposerByListing(UserListing listing, UserFilter filter) {
	    // Validate sort field
	    List<String> allowedSortFields = List.of("first_name", "middle_name", "last_name", "email_id", "mobile_number");
	    String sortBy = listing.getSortBy();


	    // Base query and filter conditions
	    String sql = "SELECT * FROM proposal_table";  // Adjust for your table name
	    Map<String, Object> params = new HashMap<>();
	    List<String> conditions = new ArrayList<>();

	    if (filter != null) {
	        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
	            conditions.add("LOWER(first_name) LIKE :firstName");
	            params.put("firstName", "%" + filter.getFirstName().toLowerCase() + "%");
	        }
	        if (filter.getMiddleName() != null && !filter.getMiddleName().isBlank()) {
	            conditions.add("LOWER(middle_name) LIKE :middleName");
	            params.put("middleName", "%" + filter.getMiddleName().toLowerCase() + "%");
	        }
	        if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
	            conditions.add("LOWER(last_name) LIKE :lastName");
	            params.put("lastName", "%" + filter.getLastName().toLowerCase() + "%");
	        }
	        if (filter.getEmailId() != null && !filter.getEmailId().isBlank()) {
	            conditions.add("LOWER(email_id) LIKE :emailId");
	            params.put("emailId", "%" + filter.getEmailId().toLowerCase() + "%");
	        }
	        if (filter.getMobileNumber() != null && !filter.getMobileNumber().isBlank()) {
	        	Long mobileNumber = Long.parseLong(filter.getMobileNumber());
	            conditions.add("mobile_number = :mobileNumber");
	            params.put("mobileNumber", mobileNumber);
	        }
	    }

	    // Join all conditions to SQL
	    if (!conditions.isEmpty()) {
	        sql += " WHERE " + String.join(" AND ", conditions);
	    }

	    if (sortBy == null || !allowedSortFields.contains(sortBy)) {
	        throw new IllegalArgumentException("Invalid sort field: " + sortBy);
	    }

	    // Validate sort order
	    String sortOrder = listing.getSortOrder();
	    if (sortOrder == null || !(sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
	        sortOrder = "desc"; // default
	    }

	    // Validate pagination
	    int pageNo = listing.getPageNo();
	    int pageSize = listing.getPageSize();
	    if (pageNo <= 0 || pageSize <= 0) {
	        throw new IllegalArgumentException("Page number and page size must be greater than 0");
	    }

	    // Add sorting
	    sql += " ORDER BY " + sortBy + " " + sortOrder;


	    Query query = entityManager.createNativeQuery(sql, Proposal.class);

	    // Set parameters
	    for (Map.Entry<String, Object> entry : params.entrySet()) {
	        query.setParameter(entry.getKey(), entry.getValue());
	    }

	    // Set pagination
	    query.setFirstResult((pageNo - 1) * pageSize);
	    query.setMaxResults(pageSize);

	    return query.getResultList();
	}*/
	//Using String Builder
	/*@Override
	public List<Proposal> fetchAllProposerByListing(UserListing listing, UserFilter filter) {

	    StringBuilder sql = new StringBuilder("SELECT * FROM proposal_table");
	    Map<String, Object> params = new HashMap<>();
	    List<String> conditions = new ArrayList<>();

	    if (filter != null) {
	        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
	            conditions.add("LOWER(first_name) LIKE :firstName");
	            params.put("firstName", "%" + filter.getFirstName().toLowerCase() + "%");
	        }
	        if (filter.getMiddleName() != null && !filter.getMiddleName().isBlank()) {
	            conditions.add("LOWER(middle_name) LIKE :middleName");
	            params.put("middleName", "%" + filter.getMiddleName().toLowerCase() + "%");
	        }
	        if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
	            conditions.add("LOWER(last_name) LIKE :lastName");
	            params.put("lastName", "%" + filter.getLastName().toLowerCase() + "%");
	        }
	        if (filter.getEmailId() != null && !filter.getEmailId().isBlank()) {
	            conditions.add("LOWER(email_id) LIKE :emailId");
	            params.put("emailId", "%" + filter.getEmailId().toLowerCase() + "%");
	        }
	        if (filter.getMobileNumber() != null && !filter.getMobileNumber().isBlank()) {
	            try {
	                Long mobileNumber = Long.parseLong(filter.getMobileNumber());
	                conditions.add("mobile_number = :mobileNumber");
	                params.put("mobileNumber", mobileNumber);
	            } catch (NumberFormatException e) {
	                throw new IllegalArgumentException("Invalid mobile number format");
	            }
	        }
	    }

	    // Append WHERE clause if there are conditions
	    if (!conditions.isEmpty()) {
	        sql.append(" WHERE ").append(String.join(" AND ", conditions));
	    }

	    // Validate and append ORDER BY clause
	    List<String> allowedSortFields = List.of("first_name", "middle_name", "last_name", "email_id", "mobile_number");
	    String sortBy = listing.getSortBy();
	    if (sortBy == null || !allowedSortFields.contains(sortBy)) {
	        throw new IllegalArgumentException("Invalid sort field: " + sortBy);
	    }

	    String sortOrder = listing.getSortOrder();
	    if (sortOrder == null || !(sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
	        sortOrder = "desc";
	    }

	    sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder);

	    // Pagination validation
	    int pageNo = listing.getPageNo();
	    int pageSize = listing.getPageSize();
	    if (pageNo <= 0 || pageSize <= 0) {
	        throw new IllegalArgumentException("Page number and page size must be greater than 0");
	    }

	    // Create native query
	    Query query = entityManager.createNativeQuery(sql.toString(), Proposal.class);

	    // Set parameters
	    params.forEach(query::setParameter);

	    // Apply pagination
	    query.setFirstResult((pageNo - 1) * pageSize);
	    query.setMaxResults(pageSize);

	    return query.getResultList();
	}*/
	// Using Criteria Query
	@Override
	public List<Proposal> fetchAllProposerByListing(UserListing listing, UserFilter filter) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Proposal> cq = cb.createQuery(Proposal.class);
		Root<Proposal> root = cq.from(Proposal.class);

		if (listing.getSortBy() == null || listing.getSortBy().trim().isEmpty()) {
			throw new IllegalArgumentException("Please enter a valid string for sortBy");
		}

		List<Predicate> predicates = new ArrayList<>();
		boolean fetchAll = 
				("00".equals(String.valueOf(listing.getPageNo())) || listing.getPageNo() == 0) &&
				("00".equals(String.valueOf(listing.getPageSize())) || listing.getPageSize() == 0);
		// predicates.add(cb.equal(root.get("status"), 'Y'));

		// Filtering
		if (filter != null) {
			if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
			}
			if (filter.getMiddleName() != null && !filter.getMiddleName().isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("middleName")), "%" + filter.getMiddleName().toLowerCase() + "%"));
			}
			if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
			}
			if (filter.getEmailId() != null && !filter.getEmailId().isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("emailId")), "%" + filter.getEmailId().toLowerCase() + "%"));
			}
			if (filter.getMobileNumber() != null && !filter.getMobileNumber().isBlank()) {
				try {
					Long mobileNo = Long.parseLong(filter.getMobileNumber());
					predicates.add(cb.equal(root.get("mobileNumber"), mobileNo));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid mobile number format");
				}
			}
		}

		cq.where(predicates.toArray(new Predicate[0]));

		// Check for special "00" condition


		// Apply sorting only if not fetching all
		if (!fetchAll) {
			String sortBy = listing.getSortBy();
			if (sortBy == null || sortBy.isBlank() || "00".equals(sortBy)) {
				sortBy = "id";
			}
			String sortOrder = listing.getSortOrder();
			if (sortOrder == null || !sortOrder.equalsIgnoreCase("asc")) {
				cq.orderBy(cb.desc(root.get(sortBy)));
			} else {
				cq.orderBy(cb.asc(root.get(sortBy)));
			}
		}

		TypedQuery<Proposal> query = entityManager.createQuery(cq);

		// Apply pagination only if not fetching all
		if (!fetchAll) {
			int pageNo = Math.max(1, listing.getPageNo());
			int pageSize = Math.max(1, listing.getPageSize());
			query.setFirstResult((pageNo - 1) * pageSize);
			query.setMaxResults(pageSize);
		}

		return query.getResultList();
	}


}