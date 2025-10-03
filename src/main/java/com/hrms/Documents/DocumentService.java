package com.hrms.Documents;

import com.hrms.Entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    void uploadDocument(MultipartFile file, DocumentDto documentDto) throws IOException;

    List<DocumentResponseDto> getDocuments(Long employeeId);

    void deleteDocument(Long id);

    Document downloadDocumentById(Long docId);


}
