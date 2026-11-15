package com.saucedemo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class JsonDataReader {

    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonDataReader() {
    }

    public static JsonNode readJsonFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("JSON file does not exist at path: {}", filePath);
            throw new IllegalArgumentException("JSON file not found: " + filePath);
        }

        try {
            return objectMapper.readTree(file);
        } catch (IOException e) {
            logger.error("Failed to parse JSON file {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Error parsing JSON file: " + filePath, e);
        }
    }
}
