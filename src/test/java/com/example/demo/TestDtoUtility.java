package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

public class TestDtoUtility {
    private static final String PATH = "src/test/resources/persons.json";
    public static void assertSerialization(String file, Class serializationClass) throws IOException {
        String eventJson = new String(Files.readAllBytes(Paths.get(PATH)));
        ObjectMapper objectMapper = new ObjectMapper();
        Object object = objectMapper.readValue(eventJson,serializationClass);
        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        assertNotNull(jsonStr);
    }
}
