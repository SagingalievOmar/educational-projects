package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentFileResponse {

    private String fileName;
    private String fileType;
    private LocalDateTime uploadedAt;
    private Long size;
    private byte[] byteFile;

}
