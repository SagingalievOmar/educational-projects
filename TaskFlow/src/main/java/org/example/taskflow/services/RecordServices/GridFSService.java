package org.example.taskflow.services.RecordServices;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GridFSService {
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;

    public void saveFile(Document document, MultipartFile file) {
        try {
            gridFsTemplate.store(
                    file.getInputStream(),
                    file.getName(),
                    String.valueOf(file.getSize()),
                    document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getFileBytes(Query query) {
        GridFSFile file = gridFsTemplate.findOne(query);
        if (file.getChunkSize() == 0) return null;
        try {
            return operations.getResource(file).getContentAsByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteFileById(Query query) {
        gridFsTemplate.delete(query);
    }
}
