package com.example.NotificationService.repositories;

import com.example.NotificationService.models.TaskNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskNotificationRepository extends JpaRepository<TaskNotification, Long> {
}
