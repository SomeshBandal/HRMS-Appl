package com.hrms.Documents;

import lombok.Data;

@Data
public class DocumentResponseDto {
    private String fileName;
    private byte[] fileData;
    private String documentType;
    private String downloadUrl;
}