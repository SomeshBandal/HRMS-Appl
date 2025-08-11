package com.hrms.Documents;

import com.hrms.Entity.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.print.Doc;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    public DocumentDto toDto(Document document){
        if(document==null){
            return null;
        }

        DocumentDto dto = new DocumentDto();

        dto.setId(document.getId());
        dto.setEmployeeId(document.getEmployee().getId());
        dto.setFileData(document.getFileData());
        dto.setFileName(document.getFileName());
        dto.setDocumentType(document.getDocumentType());

        return dto;
    }

    public Document toEntity(DocumentDto dto){
        if(dto==null){
            return null;
        }

        Document document = new Document();
        if(dto.getId()!=null){
            document.setId(dto.getId());
        }

        document.getEmployee().setId(dto.getEmployeeId());
        document.setFileName(dto.getFileName());
        document.setFileData(dto.getFileData());
        document.setDocumentType(dto.getDocumentType());

        return document;
    }
}
