package org.example.eventconsumerservice.services;

import lombok.RequiredArgsConstructor;
import org.example.eventconsumerservice.entities.DeadRecord;
import org.example.eventconsumerservice.repositories.DeadRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeadRecordService {
    private final DeadRecordRepository deadRecordRepository;

    @Transactional
    public void save(String record) {
        DeadRecord deadRecord = new DeadRecord();
        deadRecord.setData(record);
        deadRecordRepository.save(deadRecord);
    }

    public List<DeadRecord> getDeadRecords() {
        return deadRecordRepository.findAll();
    }

}
