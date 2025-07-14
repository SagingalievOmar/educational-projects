package org.example.taskflow.Aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.taskflow.DTO.messages.LogEntryMessage;
import org.example.taskflow.enums.RecordEnums.LogEntryLevel;
import org.example.taskflow.messaging.KafkaProducer;
import org.example.taskflow.models.Comment;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.RecordModels.LogEntry;
import org.example.taskflow.models.Task;
import org.example.taskflow.repositories.RecordRepositories.LogEntryRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    private final KafkaProducer kafkaProducer;
    private final LogEntryRepository logEntryRepository;

    @AfterReturning(
            pointcut = "execution(* org.example.taskflow.services..*.create(..)) || " +
                    "execution(* org.example.taskflow.services..*.update(..)) || " +
                    "execution(* org.example.taskflow.services..*.delete(..))",
            returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> context = new HashMap<>();
        if (!methodName.equals("delete")) {
            switch (result) {
                case Task task -> {
                    context.put("id", task.getId());
                    context.put("title", task.getTitle());
                    context.put("status", task.getStatus().toString());
                }
                case Comment comment -> {
                    context.put("id", comment.getId());
                    context.put("taskId", comment.getTask().getId());
                    context.put("usrId", comment.getUser().getId());
                }
                case Project project -> {
                    context.put("id", project.getId());
                    context.put("name", project.getName());
                    context.put("status", project.getStatus().toString());
                }
                default -> {
                    return;
                }
            }
        } else context.put("id", joinPoint.getArgs()[0]);

        String className = result.getClass().getSimpleName();
        String message = className + " " + methodName + "ed";

        LogEntry logEntry = new LogEntry();
        logEntry.setLevel(LogEntryLevel.INFO);
        logEntry.setMessage(message);
        logEntry.setContext(context);
        logEntry.setTimestamp(LocalDateTime.now());

        kafkaProducer.send("taskFlow.logs", LogEntryMessage.toMessage(logEntry));
        logEntryRepository.save(logEntry);
    }
}
