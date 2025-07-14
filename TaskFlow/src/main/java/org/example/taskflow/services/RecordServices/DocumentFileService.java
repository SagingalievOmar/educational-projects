package org.example.taskflow.services.RecordServices;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.taskflow.DTO.responces.DocumentFileResponse;
import org.example.taskflow.models.RecordModels.DocumentFile;
import org.example.taskflow.repositories.RecordRepositories.DocumentFileRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentFileService {
    private final DocumentFileRepository documentFileRepository;
    private final GridFSService gridFsService;

    @SneakyThrows
    @Transactional
    public ObjectId saveDocumentFile(Long taskId, MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];

        if (!(fileType.equals("pdf") || fileType.equals("png") || fileType.equals("jpg"))) {
            throw new BadRequestException("Unsupported file type");
        }

        DocumentFile documentFile = new DocumentFile();
        documentFile.setTaskId(taskId);
        documentFile.setFileName(file.getOriginalFilename());
        documentFile.setFileType(file.getContentType());
        documentFile.setUploadedAt(LocalDateTime.now());
        documentFile.setSize(file.getSize());

        ObjectId savedId = documentFileRepository.save(documentFile).getId();
        log.info("Saved documentFile id: {}", savedId);

        Document document = new Document();
        document.put("documentId", savedId);

        gridFsService.saveFile(document, file);
        log.info("Saved documentFile in gridFs: {}", savedId);
        return savedId;
    }

    public List<DocumentFileResponse> getDocumentFilesByTaskId(Long taskId) {
        List<DocumentFile> documentFiles = documentFileRepository.getDocumentFilesByTaskId(taskId);
        log.info("Found {} documentFiles from collections", documentFiles.size());
        return documentFiles
                .stream()
                .map(this::getFileFromGridFS).toList();
    }

    public DocumentFileResponse getDocumentFileById(ObjectId fileId) {
        DocumentFile documentFile = documentFileRepository.findById(fileId).orElseThrow();
        log.info("Found documentFile from collection: {}", documentFile.getId());

        return getFileFromGridFS(documentFile);
    }

    public DocumentFileResponse getFileFromGridFS(DocumentFile documentFile) {
        Query query = new Query(Criteria.where("metadata.documentId").is(documentFile.getId()));
        byte[] fileBytes = gridFsService.getFileBytes(query);
        log.info("Found file from gridFs: {}", fileBytes.length);

        DocumentFileResponse documentFileResponse = new DocumentFileResponse();
        documentFileResponse.setFileName(documentFile.getFileName());
        documentFileResponse.setFileType(documentFile.getFileType());
        documentFileResponse.setUploadedAt(documentFile.getUploadedAt());
        documentFileResponse.setSize(documentFile.getSize());
        documentFileResponse.setByteFile(fileBytes);

        return documentFileResponse;
    }

    @Transactional
    public void deleteDocumentFileById(ObjectId fileId) {
        documentFileRepository.deleteById(fileId);
        Query query = new Query(Criteria.where("metadata.documentId").is(fileId));
        gridFsService.deleteFileById(query);
        log.info("Deleted documentFile from collection and from gridFS: {}", fileId);
    }
}
