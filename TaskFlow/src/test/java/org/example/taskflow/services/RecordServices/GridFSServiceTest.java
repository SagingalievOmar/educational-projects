package org.example.taskflow.services.RecordServices;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class GridFSServiceTest {

    @Mock
    private GridFsTemplate gridFsTemplate;

    @InjectMocks
    private GridFSService gridFSService;

    private MultipartFile multipartFile;
    private final ObjectId objectId = new ObjectId();
    private final File file = new File("C:/Users/lab7/Desktop/papka/file.txt");

    @BeforeEach
    public void setUp() {
        try {
            file.createNewFile();
            FileInputStream inputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(
                    "file",
                    file.getName(),
                    "text/plain",
                    inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUploadFile() {
        Document document = new Document();
        document.put("documentId", objectId);

        Mockito.when(gridFsTemplate.store(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(objectId);
        Assertions.assertDoesNotThrow(() -> gridFSService.saveFile(document, multipartFile));
    }

    @Test
    public void TestDeleteFile() {
        Mockito.doNothing().when(gridFsTemplate).delete(Mockito.any());
        Query query = new Query(Criteria.where("metadata.documentId").is(objectId));
        Assertions.assertDoesNotThrow(() -> gridFSService.deleteFileById(query));
    }
}
