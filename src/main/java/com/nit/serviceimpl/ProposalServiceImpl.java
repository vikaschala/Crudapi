package com.nit.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nit.dto.NomineeDto;
import com.nit.dto.ProposalDto;
import com.nit.entity.Nominee;
import com.nit.entity.ProcessingQueue;
import com.nit.entity.Proposal;
import com.nit.entity.UserFilter;
import com.nit.entity.UserImportLog;
import com.nit.entity.UserListing;
import com.nit.enumeration.Gender;
import com.nit.enumeration.MaritalStatus;
import com.nit.enumeration.Nationality;
import com.nit.repository.NomineeRepo;
import com.nit.repository.ProcessingQueueRepo;
import com.nit.repository.ProposalRepo;
import com.nit.repository.UserImportLogRepo;
import com.nit.service.ProposalService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


@Service
public class ProposalServiceImpl implements ProposalService {

	@Autowired
	private ProposalRepo proposalRepo;
	@Autowired 
	private NomineeRepo nomineeRepo;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserImportLogRepo	userImportLogRepository;
	@Autowired
    private ProcessingQueueRepo   queueRepo;
	
    

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
	} 
	*/ // add proposal with annotation        
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
	    List<Proposal> activeProposals = proposalRepo.findByStatus('Y');
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

	        // Fetch nominees for the proposal
	        List<Nominee> nominees = proposal.getNominees(); // Assuming `getNominees()` fetches associated nominees
	        List<NomineeDto> nomineeDtos = new ArrayList<>();

	        // Map each nominee to NomineeDto
	        for (Nominee nominee : nominees) {
	            NomineeDto nomineeDto = new NomineeDto();
	            nomineeDto.setFirstName(nominee.getFirstName());
	            nomineeDto.setMiddleName(nominee.getMiddleName());
	            nomineeDto.setLastName(nominee.getLastName());
	            nomineeDto.setMobileNumber(nominee.getMobileNumber());
	            nomineeDto.setRelationship(nominee.getRealtionship());

	            nomineeDtos.add(nomineeDto);
	        }

	        // Add the nominee list to the proposalDto
	        dto.setNominee(nomineeDtos);

	        // Add the ProposalDto to the list
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

	//update
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

		List<Nominee> nomineeList = nomineeRepo.findByProposal_Id(proposal.getId());
		for (Nominee nominee : nomineeList) {
			nominee.setStatus('N'); // Set nominee status to 'N' (inactive)
		}
		nomineeRepo.saveAll(nomineeList);
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
	@Override
	public String exportDataIntoExcel(HttpServletResponse response) throws IOException {
		List<Proposal> proposals = proposalRepo.findAll();

		String[] headers = {
				"FirstName", "MiddleName", "LastName", "PANNumber", "AnnualIncome",
				"EmailID", "MobileNumber", "AlternateMobileNumber", "DateofBirth",
				"City", "Pincode", "Address", "Gender", "MaritalStatus", "Nationality"
		};

		int uId = new Random().nextInt(900000) + 100000; 
		String filePath = "C:/ExcelFile/Proposals_table" + uId + ".xlsx";

		try (Workbook workbook = new        
				XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Proposals");

			// Header row
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}


			int rowNum = 1;
			for (Proposal p : proposals) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(p.getFirstName());
				row.createCell(1).setCellValue(p.getMiddleName());
				row.createCell(2).setCellValue(p.getLastName());
				row.createCell(3).setCellValue(p.getPanNumber());
				row.createCell(4).setCellValue(p.getAnnualIncome() != null ? p.getAnnualIncome() : 0);
				row.createCell(5).setCellValue(p.getEmailId());
				row.createCell(6).setCellValue(p.getMobileNumber() != null ? p.getMobileNumber() : 0);
				row.createCell(7).setCellValue(p.getAlternateMobileNumber() != null ? p.getAlternateMobileNumber() : 0);
				row.createCell(8).setCellValue(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "");
				row.createCell(9).setCellValue(p.getCity());
				row.createCell(10).setCellValue(p.getPincode());
				row.createCell(11).setCellValue(p.getAddress());
				row.createCell(12).setCellValue(p.getGender() != null ? p.getGender().name() : "");
				row.createCell(13).setCellValue(p.getMaritalStatus() != null ? p.getMaritalStatus().name() : "");
				row.createCell(14).setCellValue(p.getNationality() != null ? p.getNationality().name() : "");
			}

			File file = new File(filePath);
			file.getParentFile().mkdirs(); // Ensure directory exists
			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}

			// Send file to client
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

			try (FileInputStream fileInputStream = new FileInputStream(file);
					OutputStream outStream = response.getOutputStream()) {

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = fileInputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while generating the Excel file");
		}

		return filePath;
	}

	
	@Override
	public String importExcel(MultipartFile file) throws IOException {
	    int totalRows = 0;
	    int validRows = 0;
	    List<Proposal> proposalList = new ArrayList<>();

	    String filePath = "C:/ExcelFile/Updated_Import_Result_" + System.currentTimeMillis() + ".xlsx"; // ✅ Set custom path
	    File outputFile = new File(filePath);
	    outputFile.getParentFile().mkdirs(); // ✅ Ensure directory exists

	    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
	        Sheet sheet = workbook.getSheetAt(0);

	        // Add headers for Status and ErrorField if not present
	        Row headerRow = sheet.getRow(0);
	        int statusColIndex = 0;
	        int errorFieldColIndex = 1;

	        if (headerRow != null) {
	            int lastHeaderCell = headerRow.getLastCellNum();
	            statusColIndex = lastHeaderCell;           // Determine the correct index for the Status column
	            errorFieldColIndex = lastHeaderCell + 1;  // ErrorField will be the next column after Status

	            // Add new headers only if they do not exist
	            if (headerRow.getCell(statusColIndex) == null) {
	                headerRow.createCell(statusColIndex).setCellValue("Status");
	            }
	            if (headerRow.getCell(errorFieldColIndex) == null) {
	                headerRow.createCell(errorFieldColIndex).setCellValue("ErrorField");
	            }
	        }

	        // Loop through the rows and process each row of data
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            totalRows++;

	            Proposal proposal = mapRowToProposal(row);

	            UserImportLog log = new UserImportLog();
	            log.setRowNumber(i + 1);
	            log.setTimestamp(LocalDateTime.now());
	            log.setStatus("FAILED");

	            boolean hasError = false;
	            List<String> failedFields = new ArrayList<>();
	            List<String> failedMessages = new ArrayList<>();

	            // Duplicate checks
	            if (proposal.getEmailId() != null && proposalRepo.existsByEmailId(proposal.getEmailId())) {
	                hasError = true;
	                failedFields.add("emailId");
	                failedMessages.add("Email ID already exists in database.");
	            }
	            if (proposal.getMobileNumber() != null && proposalRepo.existsByMobileNumber(proposal.getMobileNumber())) {
	                hasError = true;
	                failedFields.add("mobileNumber");
	                failedMessages.add("Mobile Number already exists in database.");
	            }
	            if (proposal.getPanNumber() != null && !proposal.getPanNumber().isEmpty()
	                    && proposalRepo.existsByPanNumber(proposal.getPanNumber())) {
	                hasError = true;
	                failedFields.add("panNumber");
	                failedMessages.add("PAN Number already exists in database.");
	            }

	            // Required field checks
	            if (proposal.getFirstName() == null || proposal.getFirstName().isEmpty()) {
	                hasError = true;
	                failedFields.add("firstName");
	                failedMessages.add("First Name is required.");
	            }
	            if (proposal.getLastName() == null || proposal.getLastName().isEmpty()) {
	                hasError = true;
	                failedFields.add("lastName");
	                failedMessages.add("Last Name is required.");
	            }
	            if (proposal.getEmailId() == null || proposal.getEmailId().isEmpty()) {
	                hasError = true;
	                failedFields.add("emailId");
	                failedMessages.add("Email ID is required.");
	            }
	            if (proposal.getPanNumber() == null || proposal.getPanNumber().isEmpty()) {
	                hasError = true;
	                failedFields.add("panNumber");
	                failedMessages.add("PAN Number is required.");
	            }

	            // Write Status and ErrorField to sheet
	            if (hasError) {
	                log.setErrorField(String.join(",", failedFields));
	                log.setErrorMessage(String.join("; ", failedMessages));
	                userImportLogRepository.save(log);

	                // Ensure correct cells are created and set
	                Cell statusCell = row.createCell(statusColIndex);
	                statusCell.setCellValue("FAILED");

	                Cell errorFieldCell = row.createCell(errorFieldColIndex);
	                errorFieldCell.setCellValue(String.join(",", failedMessages));
	            } else {
	                proposalList.add(proposal);
	                validRows++;
	                Cell statusCell = row.createCell(statusColIndex);
	                statusCell.setCellValue("SUCCESS");

	                Cell errorFieldCell = row.createCell(errorFieldColIndex);
	                errorFieldCell.setCellValue(""); // Clear ErrorField if no errors
	            }
	        }

	        // Save Excel with status to file
	        try (FileOutputStream out = new FileOutputStream(filePath)) {
	            workbook.write(out);
	        }
	    }

	    // Save valid proposals
	    if (!proposalList.isEmpty()) {
	        proposalRepo.saveAll(proposalList);
	    }

	    return String.format(
	            "Import complete. Total rows: %d, Valid: %d, Errors: %d. File saved at: %s",
	            totalRows, validRows, (totalRows - validRows), filePath
	    );
	}

	private Proposal mapRowToProposal(Row row) {
	    Proposal p = new Proposal();
	    System.out.println("Processing row " + row.getRowNum());
	    p.setFirstName(getStringValue(row.getCell(0)));
	    p.setMiddleName(getStringValue(row.getCell(1)));
	    p.setLastName(getStringValue(row.getCell(2)));
	    p.setPanNumber(getStringValue(row.getCell(3)));
	    p.setAnnualIncome(getLongValue(row.getCell(4)));
	    p.setEmailId(getStringValue(row.getCell(5)));
	    p.setMobileNumber(getLongValue(row.getCell(6)));
	    p.setAlternateMobileNumber(getLongValue(row.getCell(7)));
	    p.setDateOfBirth(getLocalDateValue(row.getCell(8)));
	    p.setCity(getStringValue(row.getCell(9)));
	    p.setPincode(getStringValue(row.getCell(10)));
	    p.setAddress(getStringValue(row.getCell(11)));
	    p.setGender(getEnumValue(row.getCell(12), Gender.class, "Gender", row.getRowNum(), new ArrayList<>())); 
	    p.setMaritalStatus(getEnumValue(row.getCell(13), MaritalStatus.class, "MaritalStatus", row.getRowNum(), new ArrayList<>())); 
	    p.setNationality(getEnumValue(row.getCell(14), Nationality.class, "Nationality", row.getRowNum(), new ArrayList<>()));
	    p.setStatus('Y');
	    
	    // Log the fields
	    System.out.println("First Name: " + p.getFirstName());
	    System.out.println("Last Name: " + p.getLastName());
	    System.out.println("Email ID: " + p.getEmailId());
	    // Add similar logs for other fields

	    return p;
	}

	private String getStringValue(Cell cell) {
	    if (cell == null) return null;
	    switch (cell.getCellType()) {
	        case STRING:  return cell.getStringCellValue();
	        case NUMERIC: return String.valueOf(cell.getNumericCellValue());
	        case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
	        default:      return null;
	    }
	}

	private Long getLongValue(Cell cell) {
	    if (cell == null) return null;
	    switch (cell.getCellType()) {
	        case NUMERIC:
	            return (long) cell.getNumericCellValue();
	        case STRING:
	            try { return Long.parseLong(cell.getStringCellValue()); }
	            catch (NumberFormatException e) { return null; }
	        default:
	            return null;
	    }
	}

	private LocalDate getLocalDateValue(Cell cell) {
	    if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
	    return cell.getDateCellValue()
	            .toInstant()
	            .atZone(ZoneId.systemDefault())
	            .toLocalDate();
	}

	private <T extends Enum<T>> T getEnumValue(Cell cell, Class<T> enumClass, String field, int rowNum, List<String> errors) {
	    if (cell == null) return null;
	    String val = getStringValue(cell);
	    if (val != null) {
	        try {
	            return Enum.valueOf(enumClass, val.toUpperCase());
	        } catch (IllegalArgumentException ex) {
	            errors.add("Row " + (rowNum + 1) + ": Invalid " + field + " -> " + val);
	        }
	    }
	    return null;
	}
   
	// Updated excelBatchProcessing method
	@Override
	public String excelBatchProcessing(MultipartFile file)  throws IOException {
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);

		int totalRows = sheet.getLastRowNum();
		int batchSize = 5;

		// If the file has more than 5 rows, queue the file for batch processing
		if (totalRows > batchSize) {
			String folderPath = "C:\\Users\\HP\\Downloads\\";
			String shortUUID = "xyz_" + UUID.randomUUID().toString().substring(0, 8);
			String filePath = folderPath + shortUUID + "Proposals.xlsx";

			// Save the workbook to a file
			try (FileOutputStream out = new FileOutputStream(filePath)) {
				workbook.write(out);
			}

			// Create and save a queue entry for batch processing
			ProcessingQueue queTable = new ProcessingQueue();
			queTable.setFilePath(filePath);
			queTable.setTotalCount(totalRows);
			queTable.setIsProcessed("N");
			queTable.setRowRead(0);
			queTable.setStatus("N");
			queueRepo.save(queTable);

			return "File queued for batch processing";
		}

		// Process each row of the file
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null) 
				continue;

			List<String> errors = new ArrayList<>();
			List<String> errorFields = new ArrayList<>();
			Proposal detailsEntity = new Proposal();

			// Validate the row and populate the Proposal entity
			validateProposalRow(row, errors, errorFields, detailsEntity);

			// If there are errors, save them; otherwise, save the successful record
			if (!errors.isEmpty()) {
				saveErrors(errors, errorFields, i);
			} else {
				proposalRepo.save(detailsEntity);
				saveSuccess(i);
			}
		}

		return "Processing completed";
	}
	private void validateProposalRow(Row row, List<String> errors, List<String> errorFields, Proposal proposal) {
		Cell firstNameCell = row.getCell(0);
		if (firstNameCell == null || firstNameCell.getCellType() != CellType.STRING || firstNameCell.getStringCellValue().isEmpty()) {
			errors.add("FirstName is empty");
			errorFields.add("FirstName");
		} else {
			proposal.setFirstName(firstNameCell.getStringCellValue());
		}

		Cell middleNameCell = row.getCell(1);
		if (middleNameCell == null || middleNameCell.getCellType() != CellType.STRING || middleNameCell.getStringCellValue().isEmpty()) {
			errors.add("MiddleName is empty");
			errorFields.add("MiddleName");
		} else {
			proposal.setMiddleName(middleNameCell.getStringCellValue());
		}

		Cell lastNameCell = row.getCell(2);
		if (lastNameCell == null || lastNameCell.getCellType() != CellType.STRING || lastNameCell.getStringCellValue().isEmpty()) {
			errors.add("LastName is empty");
			errorFields.add("LastName");
		} else {
			proposal.setLastName(lastNameCell.getStringCellValue());
		}

		Cell panCell = row.getCell(3);
		if (panCell == null || panCell.getCellType() != CellType.STRING || panCell.getStringCellValue().isEmpty() || !panCell.getStringCellValue().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
			errors.add("Invalid or empty PAN Number");
			errorFields.add("PAN Number");
		} else {
			proposal.setPanNumber(panCell.getStringCellValue());
		}

		Cell incomeCell = row.getCell(4);
		if (incomeCell == null || incomeCell.getCellType() != CellType.NUMERIC) {
			errors.add("Invalid or empty Annual Income");
			errorFields.add("Annual Income");
		} else {
			proposal.setAnnualIncome((long) incomeCell.getNumericCellValue());
		}

		Cell emailCell = row.getCell(5);
		if (emailCell == null || emailCell.getCellType() != CellType.STRING || emailCell.getStringCellValue().isEmpty() || !emailCell.getStringCellValue().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			errors.add("Invalid or empty Email ID");
			errorFields.add("Email ID");
		} else {
			proposal.setEmailId(emailCell.getStringCellValue());
		}

		Cell mobileCell = row.getCell(6);
		if (mobileCell == null || mobileCell.getCellType() != CellType.NUMERIC) {
			errors.add("Invalid Mobile Number");
			errorFields.add("Mobile Number");
		} else {
			long mobileNumber = (long) mobileCell.getNumericCellValue();
			if (String.valueOf(mobileNumber).length() != 10) {
				errors.add("Invalid Mobile Number");
				errorFields.add("Mobile Number");
			} else {
				proposal.setMobileNumber(mobileNumber);
			}
		}

		Cell altMobileCell = row.getCell(7);
		if (altMobileCell != null && altMobileCell.getCellType() == CellType.NUMERIC) {
			long altMobile = (long) altMobileCell.getNumericCellValue();
			if (String.valueOf(altMobile).length() != 10) {
				errors.add("Invalid Alternate Mobile Number");
				errorFields.add("Alternate Mobile Number");
			} else {
				proposal.setAlternateMobileNumber(altMobile);
			}
		}

		Cell dobCell = row.getCell(8);
		if (dobCell == null || dobCell.getCellType() != CellType.NUMERIC) {
			errors.add("Invalid or empty Date of Birth");
			errorFields.add("Date of Birth");
		} else {
			proposal.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
		}

		Cell cityCell = row.getCell(9);
		if (cityCell == null || cityCell.getCellType() != CellType.STRING || cityCell.getStringCellValue().isEmpty()) {
			errors.add("City is empty");
			errorFields.add("City");
		} else {
			proposal.setCity(cityCell.getStringCellValue());
		}

		Cell pinCell = row.getCell(10);
		if (pinCell == null || pinCell.getCellType() != CellType.STRING || pinCell.getStringCellValue().isEmpty() || !pinCell.getStringCellValue().matches("\\d{6}")) {
			errors.add("Invalid or empty Pincode");
			errorFields.add("Pincode");
		} else {
			proposal.setPincode(pinCell.getStringCellValue());
		}

		Cell addressCell = row.getCell(11);
		if (addressCell == null || addressCell.getCellType() != CellType.STRING || addressCell.getStringCellValue().isEmpty()) {
			errors.add("Address is empty");
			errorFields.add("Address");
		} else {
			proposal.setAddress(addressCell.getStringCellValue());
		}

		Cell genderCell = row.getCell(12);
		if (genderCell == null || genderCell.getCellType() != CellType.STRING || genderCell.getStringCellValue().isEmpty()) {
			errors.add("Invalid or empty Gender");
			errorFields.add("Gender");
		} else {
			try {
				proposal.setGender(Gender.valueOf(genderCell.getStringCellValue()));
			} catch (Exception e) {
				errors.add("Invalid Gender");
				errorFields.add("Gender");
			}
		}

		Cell maritalCell = row.getCell(13);
		if (maritalCell == null || maritalCell.getCellType() != CellType.STRING || maritalCell.getStringCellValue().isEmpty()) {
			errors.add("Invalid or empty Marital Status");
			errorFields.add("Marital Status");
		} else {
			try {
				proposal.setMaritalStatus(MaritalStatus.valueOf(maritalCell.getStringCellValue()));
			} catch (Exception e) {
				errors.add("Invalid Marital Status");
				errorFields.add("Marital Status");
			}
		}

		Cell nationalityCell = row.getCell(14);
		if (nationalityCell == null || nationalityCell.getCellType() != CellType.STRING || nationalityCell.getStringCellValue().isEmpty()) {
			errors.add("Invalid or empty Nationality");
			errorFields.add("Nationality");
		} else {
			try {
				proposal.setNationality(Nationality.valueOf(nationalityCell.getStringCellValue()));
			} catch (Exception e) {
				errors.add("Invalid Nationality");
				errorFields.add("Nationality");
			}
		}
	}

	private void saveErrors(List<String> errors, List<String> errorFields, int rowIndex) {
		for (int i = 0; i < errors.size(); i++) {
			String errorMessage = errors.get(i);
			String fieldName = errorFields.get(i);

			System.out.println("Error in Row " + rowIndex + " - Field: " + fieldName + ", Message: " + errorMessage);

			UserImportLog error = new UserImportLog ();
			error.setRowNumber(rowIndex);
			error.setErrorField(fieldName);
			error.setErrorMessage(errorMessage);

			userImportLogRepository.save(error);
		}
	}


	// Save success information for valid rows
	private void saveSuccess(int rowIndex) {
		// Log or save the success (could save to a database or log system)
		System.out.println("Row " + rowIndex + " processed successfully");

		// Create an instance of ProcessingQueue for success logging
		ProcessingQueue success = new ProcessingQueue();

		// Assuming you want to update the rowRead or any other field related to success
		success.setRowRead(rowIndex);  // Set the processed row index
		success.setStatus("Processed");  // Assuming 'Processed' is a status for successful rows
		success.setIsProcessed("Y");    // Set to 'Y' (Yes) for processed

		// Save success information to the database using the appropriate repository
		queueRepo.save(success); // Assuming successRepo is your repository for successes
	}

	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void processBatchFiles() {
		List<ProcessingQueue> batchQueues = queueRepo.findByIsProcessedAndTotalCountLessThan("N", 100);

		for (ProcessingQueue queue : batchQueues) {
			queueRepo.save(queue); // Save queue before processing

			try (FileInputStream fis = new FileInputStream(queue.getFilePath());
					Workbook workbook = new XSSFWorkbook(fis)) {

				Sheet sheet = workbook.getSheetAt(0);
				int rowStart = queue.getIsProcessed().equals("N") ? queue.getRowRead() + 1 : queue.getTotalCount();

				Row headerRow = sheet.getRow(0);
				if (rowStart == 1) {
					int lastCol = headerRow.getLastCellNum();
					headerRow.createCell(lastCol).setCellValue("Error Message");
					headerRow.createCell(lastCol + 1).setCellValue("Error Status");
				}

				for (int i = rowStart; i <= queue.getTotalCount(); i++) {
					if (i >= rowStart + 3) break; // Batch size of 3

					Row row = sheet.getRow(i);
					if (row == null) continue;

					List<String> errors = new ArrayList<>();
					List<String> errorFields = new ArrayList<>();

					Proposal proposal = new Proposal();

					// Validate First Name
					String firstName = getCellValueAsString(row.getCell(1));
					if (firstName.isEmpty()) {
						errors.add("FirstName is Empty");
						errorFields.add("FirstName");
					} else {
						proposal.setFirstName(firstName);
					}

					// Validate PAN Number
					String pan = getCellValueAsString(row.getCell(6));
					if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
						errors.add("Invalid PAN format");
						errorFields.add("PanNumber");
					} else if (proposalRepo.existsByPanNumber(pan)) {
						errors.add("PAN Already Exists");
						errorFields.add("PanNumber");
					} else {
						proposal.setPanNumber(pan);
					}

					// Validate Email
					String email = getCellValueAsString(row.getCell(9));
					if (email.isEmpty()) {
						errors.add("Email is Empty");
						errorFields.add("EmailId");
					} else if (proposalRepo.existsByEmailId(email)) {
						errors.add("Email Already Exists");
						errorFields.add("EmailId");
					} else {
						proposal.setEmailId(email);
					}

					// Validate Mobile Number
					Long mobile = getCellLongValue(row.getCell(17));
					if (mobile == null || mobile.toString().length() != 10) {
						errors.add("Invalid Mobile Number");
						errorFields.add("MobileNumber");
					} else if (proposalRepo.existsByMobileNumber(mobile)) {
						errors.add("Mobile Number Already Exists");
						errorFields.add("MobileNumber");
					} else {
						proposal.setMobileNumber(mobile);
					}

					// Handle errors
					if (!errors.isEmpty()) {
						for (int j = 0; j < errors.size(); j++) {
							logError(errors.get(j), errorFields.get(j), "Fail", i);
						}

						updateRowWithError(row, headerRow, errors);

					} else {
						Proposal saved = proposalRepo.save(proposal);
						logSuccess(saved.getId(), i);

						updateRowWithSuccess(row, headerRow, saved.getId());
					}

					queue.setRowRead(i);
				}

				// Mark as processed if all rows are done
				if (queue.getRowRead() >= queue.getTotalCount()) {
					queue.setIsProcessed("Y");
				}

				// Save workbook changes
				try (FileOutputStream out = new FileOutputStream(queue.getFilePath())) {
					workbook.write(out);
				}

				queueRepo.save(queue); // Save the updated queue status

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void logError(String errorMessage, String errorField, String status, int rowIndex) {
		UserImportLog log = new UserImportLog();
		log.setErrorMessage(errorMessage);
		log.setErrorField(errorField);
		log.setStatus(status);
		log.setRowNumber(rowIndex);
		userImportLogRepository.save(log);
	}

	private void logSuccess(Long proposalId, int rowIndex) {
		UserImportLog successLog = new UserImportLog();
		successLog.setErrorMessage("Saved Proposal ID: " + proposalId);
		successLog.setStatus("Success");
		successLog.setRowNumber( rowIndex);
		userImportLogRepository.save(successLog);
	}

	private void updateRowWithError(Row row, Row headerRow, List<String> errors) {
		int lastCol = headerRow.getLastCellNum();
		Cell errorMessage = row.createCell(lastCol - 2);
		Cell errorStatus = row.createCell(lastCol - 1);
		errorMessage.setCellValue(String.join(",", errors));
		errorStatus.setCellValue("Fail");
	}

	private void updateRowWithSuccess(Row row, Row headerRow, Long savedProposalId) {
		int lastCol = headerRow.getLastCellNum();
		Cell errorMessage = row.createCell(lastCol - 2);
		Cell errorStatus = row.createCell(lastCol - 1);
		errorMessage.setCellValue("Saved Proposal ID: " + savedProposalId);
		errorStatus.setCellValue("Success");
	}

	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue().trim();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue());
		}
		return "";
	}

	private Long getCellLongValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == CellType.NUMERIC) {
			return (long) cell.getNumericCellValue();
		} else if (cell.getCellType() == CellType.STRING) {
			try {
				return Long.parseLong(cell.getStringCellValue().trim());
			} catch (NumberFormatException e) {
				return null; // Handle gracefully
			}
		}
		return null;
	}
}