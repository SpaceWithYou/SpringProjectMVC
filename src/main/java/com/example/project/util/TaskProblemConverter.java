package com.example.project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class TaskProblemConverter implements AttributeConverter<TaskProblem, String> {

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TaskProblem attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return "error";
        }
    }

    @Override
    public TaskProblem convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, TaskProblem.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
