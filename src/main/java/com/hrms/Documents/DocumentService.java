package com.hrms.Documents;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    void uploadDocument(MultipartFile file, DocumentDto documentDto);

    List<DocumentResponseDto> getDocuments(Long employeeId);

    void deleteDocument(Long id);

    DocumentDto getDocumentById(Long docId);
}
