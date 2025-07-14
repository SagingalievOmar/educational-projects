package org.example.eventconsumerservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter

@Document
public class DeadRecord {

    @Id
    private ObjectId id;
    private String data;

}
