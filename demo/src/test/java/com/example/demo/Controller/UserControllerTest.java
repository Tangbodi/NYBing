package com.example.demo.Controller;

import com.example.demo.DTO.UserDTO;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        System.out.println("---------------start---------------");

        System.out.println("================end================");
    }
    @Test
    @Transactional
    @Rollback()
    void updateUserById() throws Exception{
        String json="{\n" +
                "    \"firstName\": \"bodi\",\n" +
                "    \"middleName\": null,\n" +
                "    \"lastName\": \"tang\",\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("http://192.168.1.10:8080/api/user/update/51")
                .content(json.getBytes())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
    }
}