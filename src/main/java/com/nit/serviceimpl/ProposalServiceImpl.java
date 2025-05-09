package com.nit.serviceimpl;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.nit.repository.ProposalRepo;
import com.nit.repository.ProcessingQueueRepo;
import com.nit.repository.UserImportLogRepo;
import com.nit.service.ProposalService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletResponse;


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
	} */// add proposal with annotation        
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
	public String excelBatchProcessing(MultipartFile file) throws IOException {
	    Workbook workbook = new XSSFWorkbook(file.getInputStream());
	    Sheet sheet = workbook.getSheetAt(0);
	    int totalRow = sheet.getLastRowNum();

	    if (totalRow > 5) {
	        String folderPath = "C:\\Users\\HP\\Downloads\\";
	        String shortUUID = "xyz_" + UUID.randomUUID().toString().substring(0, 8);
	        String filePath = folderPath + shortUUID + "_Proposals.xlsx";

	        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
	            workbook.write(outputStream);
	        }
	        workbook.close();

	        ProcessingQueue queue = new ProcessingQueue();
	        queue.setFilePath(filePath);
	        queue.setTotalCount(0); // Initially zero
	        queue.setIsProcessed("N");
	        queue.setStatus("Y");
	        queueRepo.save(queue);

	        return "File goes for batch processing";
	    }

	    for (int i = 1; i <= totalRow; i++) {
	        Row row = sheet.getRow(i);
	        if (row == null) continue;

	        List<String> errors = new ArrayList<>();
	        List<String> errorFields = new ArrayList<>();

	        // Validation logic (same as below in batchProcessing)...

	        // Perform validations
	        String firstName = getStringValue(row.getCell(1));
	        if (firstName.isEmpty()) {
	            errors.add("FirstName is Empty");
	            errorFields.add("FirstName");
	        }

	        // ...continue other validations like you have in batchProcessing...

	        if (!errors.isEmpty()) {
	            for (int j = 0; j < errors.size(); j++) {
	                UserImportLog log = new UserImportLog();
	                log.setErrorMessage(errors.get(j));
	                log.setErrorField(errorFields.get(j));
	                log.setStatus("Fail");
	                log.setRowNumber(i);
	                log.setTimestamp(LocalDateTime.now());
	                userImportLogRepository.save(log);
	            }
	        } else {
	            Proposal p = new Proposal();
	            p.setFirstName(firstName);
	            p.setMiddleName(getStringValue(row.getCell(2)));
	            p.setLastName(getStringValue(row.getCell(3)));
	            p.setGender(Gender.valueOf(getStringValue(row.getCell(4))));
	            p.setDateOfBirth(getLocalDateValue(row.getCell(5)));
	            p.setPanNumber(getStringValue(row.getCell(6)));
	            p.setAlternateMobileNumber(getLongValue(row.getCell(10)));
	            p.setEmailId(getStringValue(row.getCell(9)));
	            p.setMobileNumber(getLongValue(row.getCell(17)));
	            p.setAddress(getStringValue(row.getCell(11)));
	            p.setCity(getStringValue(row.getCell(15)));
	            p.setPincode(getStringValue(row.getCell(14)));
	            p.setMaritalStatus(MaritalStatus.valueOf(getStringValue(row.getCell(8))));

	            proposalRepo.save(p);

	            UserImportLog log = new UserImportLog();
	            log.setErrorMessage("Row " + i + " Success");
	            log.setStatus("Success");
	            log.setRowNumber(i);
	            log.setTimestamp(LocalDateTime.now());
	            userImportLogRepository.save(log);
	        }
	    }

	    workbook.close();
	    return "Processing Completed";
	}
	@Scheduled(fixedDelay = 5000)
	@Override
	public void batchProcessing() throws FileNotFoundException {
		List<ProcessingQueue> pendingQueues = queueRepo.findByIsProcessedAndTotalCountLessThan("NO", 500);


	    for (ProcessingQueue queue : pendingQueues) {
	        try (FileInputStream fis = new FileInputStream(queue.getFilePath());
	             Workbook workbook = new XSSFWorkbook(fis)) {

	            Sheet sheet = workbook.getSheetAt(0);
	            int rowStart = queue.getTotalCount() + 1;
	            int totalRow = sheet.getLastRowNum();
	            int batchSize = 3;

	            for (int i = rowStart; i <= totalRow && i < rowStart + batchSize; i++) {
	                Row row = sheet.getRow(i);
	                if (row == null) continue;

	                List<String> errors = new ArrayList<>();
	                List<String> errorFields = new ArrayList<>();

	                String firstName = getStringValue(row.getCell(1));
	                if (firstName.isEmpty()) {
	                    errors.add("FirstName is Empty");
	                    errorFields.add("FirstName");
	                }

	                // Continue all your validations same as in excelBatchProcessing...

	                if (!errors.isEmpty()) {
	                    for (int j = 0; j < errors.size(); j++) {
	                        UserImportLog log = new UserImportLog();
	                        log.setErrorMessage(errors.get(j));
	                        log.setErrorField(errorFields.get(j));
	                        log.setStatus("Fail");
	                        log.setRowNumber(i);
	                        log.setTimestamp(LocalDateTime.now());
	                        userImportLogRepository.save(log);
	                    }
	                } else {
	                    Proposal p = new Proposal();
	                    p.setFirstName(firstName);
	                    p.setMiddleName(getStringValue(row.getCell(2)));
	                    p.setLastName(getStringValue(row.getCell(3)));
	                    String genderValue = getStringValue(row.getCell(4));
	                    try {
	                        p.setGender(Gender.valueOf(genderValue.toUpperCase())); // Make it case-insensitive
	                    } catch (IllegalArgumentException e) {
	                        // Handle invalid gender value
	                        System.out.println("Invalid gender value: " + genderValue);
	                    }

	                    p.setDateOfBirth(getLocalDateValue(row.getCell(5)));
	                    p.setPanNumber(getStringValue(row.getCell(6)));
	                    p.setAlternateMobileNumber(getLongValue(row.getCell(10)));
	                    p.setEmailId(getStringValue(row.getCell(9)));
	                    p.setMobileNumber(getLongValue(row.getCell(17)));
	                    p.setAddress(getStringValue(row.getCell(11)));
	                    p.setCity(getStringValue(row.getCell(15)));
	                    p.setPincode(getStringValue(row.getCell(14)));
	                    String maritalStatusValue = getStringValue(row.getCell(8));
	                    try {
	                        p.setMaritalStatus(MaritalStatus.valueOf(maritalStatusValue.toUpperCase())); // Making it case-insensitive
	                    } catch (IllegalArgumentException e) {
	                        // Handle invalid marital status value
	                        System.out.println("Invalid marital status value: " + maritalStatusValue);
	                    }


	                    proposalRepo.save(p);

	                    UserImportLog log = new UserImportLog();
	                    log.setErrorMessage("Row " + i + " Success");
	                    log.setStatus("Success");
	                    log.setRowNumber(i);
	                    log.setTimestamp(LocalDateTime.now());
	                    userImportLogRepository.save(log);
	                }

	                queue.setTotalCount(i);
	            }

	            if (queue.getTotalCount() >= totalRow) {
	                queue.setIsProcessed("Y");
	                queue.setStatus("N");
	            }

	            queueRepo.save(queue);
	        } catch (IOException e) {
	            // Log or rethrow
	            e.printStackTrace();
	        }
	    }
	}
}