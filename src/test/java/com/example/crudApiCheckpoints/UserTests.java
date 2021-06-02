package com.example.crudApiCheckpoints;

import com.example.crudApiCheckpoints.domain.User;
import com.example.crudApiCheckpoints.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mvc;

    @Test
    @Transactional
    @Rollback
    public void getAllUsersTest() throws Exception{
        User user=new User();
        user.setEmail("User@abc.com");
        user.setPassword("xbex");
        this.userRepository.save(user);
        User user1=new User();
        user1.setEmail("User1@abc.com");
        user1.setPassword("xbex");
        this.userRepository.save(user1);
        MockHttpServletRequestBuilder request=get("/Users")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("User@abc.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].email", is("User1@abc.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void postUserTest() throws Exception{
        String json=getJSON("src/test/resources/postUser.json");
        MockHttpServletRequestBuilder request=post("/Users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("User1@abc.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void getByIdTest() throws Exception{
        User user=new User();
        user.setEmail("User@abc.com");
        user.setPassword("xbex");
        this.userRepository.save(user);
        MockHttpServletRequestBuilder request=get("/Users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("User1@abc.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void updateByIdTest() throws Exception{
        User user=new User();
        user.setEmail("User@abc.com");
        user.setPassword("xbex");
        this.userRepository.save(user);

        String json=getJSON("src/test/resources/updateUser.json");
        System.out.println(json);
        MockHttpServletRequestBuilder request=patch("/Users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("User1@abc.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteByIdTest() throws Exception{
        User user=new User();
        user.setEmail("User@abc.com");
        user.setPassword("xbex");
        this.userRepository.save(user);
        User user1=new User();
        user1.setEmail("User1@abc.com");
        user1.setPassword("xbex1");
        this.userRepository.save(user1);
                MockHttpServletRequestBuilder request=delete("/Users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mvc.perform(request)
                .andExpect(status().isOk()).andReturn();
        int actual = Integer.parseInt((result.getResponse().getContentAsString()));
        assertEquals(1,actual);
    }

    @Test
    @Transactional
    @Rollback
    public void authenticateUserTest() throws Exception{
        User user=new User();
        user.setEmail("User@abc.com");
        user.setPassword("123");
        this.userRepository.save(user);
        //String json=getJSON("src/test/resources/authorizeUser.json");

        MockHttpServletRequestBuilder request=post("/Users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email","User@abc.com")
                .param("password", "123");
                //.content(json);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(true)));
    }

    public String getJSON(String path) throws  Exception{
        Path paths = Paths.get(path);
        return new String(Files.readAllBytes(paths));
    }
}
