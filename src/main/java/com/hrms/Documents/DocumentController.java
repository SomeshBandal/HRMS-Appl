package com.hrms.Documents;

import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Entity.Document;
import com.hrms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Document Management", description = "APIs for managing document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/hr/document/{empId}")
    @Operation(summary = "Uploading Employee Documents",
            description = "Uploading document file with id, name, documentType and Employee")

    public ResponseEntity<ApiResponse<?>> uploadDocument(
            @RequestParam MultipartFile file,
            @ModelAttribute DocumentDto documentDto) {

        try {
            documentService.uploadDocument(file, documentDto);
            return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Invalid file", e.getMessage()));

        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, "Employee not found", e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed", e.getMessage()));
        }
    }

        @PreAuthorize("hasRole('EMPLOYEE')")
        @GetMapping("/employee/{employeeId}")
        @Operation(summary = "Fetching Employee Documents",
                description = "Fetching document file using employee id")

        public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocuments (
                @PathVariable Long employeeId){

            try {
                List<DocumentResponseDto> documents = documentService.getDocuments(employeeId);
                return ResponseEntity.ok().body(ApiResponse.success("Documents fetched successfully", documents));
            } catch (DocumentNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND, "Documents not found", e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch documents", e.getMessage()));
            }
        }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/download/{docId}")
    @Operation(summary = "Downloading Employee Documents",
            description = "Downloading document file using document id")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long docId) {

            Document document = documentService.downloadDocumentById(docId);
            byte[] fileData = document.getFileData();

            ByteArrayResource resource = new ByteArrayResource(fileData);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                    .body(resource);
        }



    @PreAuthorize("hasRole('HR')")
    @DeleteMapping("/employee/document/{id}")
    @Operation(summary = "Deleting document", description = "deleting document using id")

    public ResponseEntity<String>deleteDocument(
            @PathVariable Long id){

        documentService.deleteDocument(id);
        return ResponseEntity.ok("Document deleted successfully");
    }
}
