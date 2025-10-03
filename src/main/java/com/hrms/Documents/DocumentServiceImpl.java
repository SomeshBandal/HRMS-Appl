package com.hrms.Documents;

import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Document;
import com.hrms.Entity.Employee;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {


    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final EmployeeRepository employeeRepository;


    @Override
    public void uploadDocument(MultipartFile file, DocumentDto documentDto) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files allowed");
        }

        byte[] fileData = file.getBytes();

        Employee employee = employeeRepository.findById(documentDto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        Document document = new Document();
        document.setId(documentDto.getId());
        document.setFileName(documentDto.getFileName());
        document.setDocumentType(documentDto.getDocumentType());
        document.setEmployee(employee);
        document.setFileData(fileData);

        documentRepository.save(document);
    }

    @Override
    public List<DocumentResponseDto> getDocuments(Long employeeId) {
        List<DocumentDto> documents = documentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(documentMapper::toDto)
                .toList();

        if (documents.isEmpty()) {
            throw new DocumentNotFoundException("No documents found for employeeId: " + employeeId);
        }

        List<DocumentResponseDto> dtos = documents.stream().map(doc -> {
            DocumentResponseDto dto = new DocumentResponseDto();

            dto.setFileName(doc.getFileName());
            dto.setDocumentType(doc.getDocumentType());
            dto.setDownloadUrl("/api/documents/download/" + doc.getId());
            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public Document downloadDocumentById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() -> new DocumentNotFoundException("Document not found"));
    }


    @Override
    public void deleteDocument(Long id) {
        DocumentDto dto = documentRepository.findById(id).map(documentMapper::toDto).orElseThrow(() -> new DocumentNotFoundException("Document not found"));
        documentRepository.delete(documentMapper.toEntity(dto));

    }
}
