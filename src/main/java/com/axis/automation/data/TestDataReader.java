package com.axis.automation.data;

import com.axis.automation.utils.LoggerUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

/**
 * Utility class for reading test data from JSON files.
 * Uses Jackson ObjectMapper to deserialize JSON into POJOs.
 */
public class TestDataReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TestDataReader() {}

    // ─── Core Read Method ────────────────────────────────────────

    public static <T> T getData(String filePath, String nodeName, Class<T> clazz) {
        try (InputStream is = TestDataReader.class
                .getClassLoader()
                .getResourceAsStream(filePath)) {

            if (is == null) {
                throw new RuntimeException("Test data file not found: " + filePath);
            }

            JsonNode rootNode = objectMapper.readTree(is);
            JsonNode targetNode = rootNode.get(nodeName);

            if (targetNode == null) {
                throw new RuntimeException("Node '" + nodeName + "' not found in: " + filePath);
            }

            T data = objectMapper.treeToValue(targetNode, clazz);

            if (data instanceof User user) {
                if (user.getEmail() != null && user.getEmail().contains("{{timestamp}}")) {
                    user.setEmail(user.getEmail()
                            .replace("{{timestamp}}", String.valueOf(System.currentTimeMillis())));
                }
            }

            LoggerUtils.info("Test data loaded: [" + nodeName + "] from [" + filePath + "]");
            return data;

        } catch (Exception e) {
            LoggerUtils.error("Failed to load test data from: " + filePath, e);
            throw new RuntimeException("Test data loading failed", e);
        }
    }

    // ─── User Convenience Methods ────────────────────────────────

    public static User getValidUser() {
        return getData("testdata/users.json", "validUser", User.class);
    }

    public static User getInvalidUser() {
        return getData("testdata/users.json", "invalidUser", User.class);
    }

    public static User getMissingFieldsUser() {
        return getData("testdata/users.json", "missingFieldsUser", User.class);
    }

    public static User getExistingUser() {
        return getData("testdata/users.json", "existingUser", User.class);
    }

    public static User getInvalidLoginUser() {
        return getData("testdata/users.json", "invalidLoginUser", User.class);
    }
}