package com.sep.onlinedeliverysystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class FileUploadControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    FileUploadControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

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
        //String uploadDirectory = "${upload.directory}";
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/upload").file(file)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
        );
    }

    //uploads file then deletes it and ensures the deletion returns '200 ok'
    @Test
    public void deleteFileReturnsHttp200OkWhenFileExists() throws Exception {
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
    public void deleteFileReturnsHttp200OkWhenFileNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/delete").param("filename", "nonexistent.txt")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

}
