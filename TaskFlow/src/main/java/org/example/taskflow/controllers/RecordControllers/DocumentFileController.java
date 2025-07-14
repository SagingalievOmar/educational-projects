package org.example.taskflow.controllers.RecordControllers;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.taskflow.DTO.responces.DocumentFileResponse;
import org.example.taskflow.services.RecordServices.DocumentFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentFileController {
    private final DocumentFileService documentFileService;

    @GetMapping("/{id}")
    public ResponseEntity<DocumentFileResponse> createDocumentFile(@PathVariable String id) {
        return ResponseEntity.ok(documentFileService.getDocumentFileById(new ObjectId(id)));
    }

    @DeleteMapping("/{id}")
    public void deleteDocumentFile(@PathVariable String id) {
        documentFileService.deleteDocumentFileById(new ObjectId(id));
    }
}
