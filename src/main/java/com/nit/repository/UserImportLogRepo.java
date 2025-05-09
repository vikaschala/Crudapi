package com.nit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.UserImportLog;

public interface UserImportLogRepo extends JpaRepository<UserImportLog, Long>{

}
