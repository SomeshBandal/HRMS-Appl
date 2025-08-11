package com.hrms.Documents;

import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Employee.EmployeeRepository;
import com.hrms.Entity.Document;
import com.hrms.Entity.Employee;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public void uploadDocument(MultipartFile file, DocumentDto documentDto) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(500, 500)
                    .outputFormat("jpg")
                    .outputQuality(0.7)
                    .toOutputStream(outputStream);

            byte[] compressedImage = outputStream.toByteArray();

            Employee employee = employeeRepository.findById(documentDto.getEmployeeId())
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

            Document document = new Document();
            document.setId(documentDto.getId());
            document.setFileName(documentDto.getFileName());
            document.setDocumentType(documentDto.getDocumentType());
            document.setEmployee(employee);
            document.setFileData(compressedImage);

            documentRepository.save(document);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload Document", e);
        }

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
    public DocumentDto getDocumentById(Long documentId) {
        return documentRepository.findById(documentId).map(documentMapper::toDto)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + documentId));
    }


    @Override
    public void deleteDocument(Long id) {
        DocumentDto dto = documentRepository.findById(id).map(documentMapper::toDto).orElseThrow(() -> new DocumentNotFoundException("Document not found"));
        Document entity = documentMapper.toEntity(dto);
        documentRepository.delete(entity);
    }
}
