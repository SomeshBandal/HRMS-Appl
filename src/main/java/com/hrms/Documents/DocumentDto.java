package com.hrms.Documents;

import com.hrms.Entity.Employee;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private Long id;
    private String fileName;
    private String documentType;
    @Lob
    private byte[] fileData;
    private Long employeeId;
}
