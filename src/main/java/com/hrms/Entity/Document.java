package com.hrms.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue
    private Long id;
    private String fileName;
    private String documentType;

    @Lob
    private byte[] fileData;

    @ManyToOne
    private Employee employee;

}