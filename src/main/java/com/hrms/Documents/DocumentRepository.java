package com.hrms.Documents;

import com.hrms.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByEmployeeId(Long employeeId);
}
