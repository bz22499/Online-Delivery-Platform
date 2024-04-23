package com.sep.onlinedeliverysystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.request.MockMvcRequestMatchers;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //checks that the response status is '200 ok'
    @Test
    public void uploadFileReturnsHttp2000k() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/upload").file(file)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    //checks the response contains the content of the uploaded file
    @Test
    public void uploadFileReturnsPathToSavedFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello world".getBytes()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/upload").file(file)
        ).andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
        ).andExpect(
                MockMvcResultMatchers.content().string(Files.readAllLines(Paths.get(file.getOriginalFilename())).get(0))
        );
    }

    //uploads file then deletes it and ensures the deletion returns '200 ok'
    @Test
    public void deleteFileReturnsHttp2000kWhenFileExists() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello world".getBytes()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/upload").file(file)
        );
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/delete").param("filename", file.getOriginalFilename())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    //deletes a nonexistent file and ensures that it still returns '200 ok'
    @Test
    public void deleteFileReturnsHttp2000kWhenFileNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/delete").param("filename", "nonexistent.txt")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

}
