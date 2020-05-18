package com.sundeep.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundeep.api.context.DataSourceContext;
import com.sundeep.api.model.User;
import com.sundeep.api.repository.UserRepository;
import com.sundeep.api.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {Users.class, UserService.class, UserRepository.class, DataSourceContext.class})
@WebMvcTest
@ActiveProfiles("test")
public class TestUsers {

    private final static String TEST_USER_ID = "dummy-id";

    private User user;

    @InjectMocks
    private Users users;

    final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserSizeZero() throws Exception{
        testGetUsers(0);
    }

    @Test
    public void testUserComplete() throws Exception{
        createUser();
        testGetUsers(0);
        testAddOrUpdateUser(user);
        testGetUsers(1);
        testAddOrUpdateUser(user);
        testGetUsers(1);
        testGetUser(user.getUsername());
        testDeleteUser(user.getUsername());
        testGetUsers(0);
    }

    @Test
    public void testManyUsers() throws Exception{
        createUser();
        testGetUsers(0);
        testAddOrUpdateUser(user);
        testGetUsers(1);
        setUsername("testuser1");
        testAddOrUpdateUser(user);
        testAddOrUpdateUser(user);
        testGetUser(user.getUsername());
        testGetUsers(2);
        testDeleteUser(user.getUsername());
        testGetUsers(1);
        testGetUser("testuser");
        testDeleteUser("testuser");
        testGetUsers(0);
    }

    private void testGetUsers( final int size) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/v1")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(size)))
                .andReturn();
    }

    private void testGetUser( final String userName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/v1/"+userName)
                .with(user(TEST_USER_ID))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is(userName)))
                .andReturn();
    }

    public void testDeleteUser( final String userName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/v1/"+userName)
                .with(user(TEST_USER_ID))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    public void testAddOrUpdateUser( final User user) throws Exception {
        final String content = mapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/v1")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    public void createUser()
    {
        if( user == null ){
            user = new User();
        }
        user.setUsername("testuser");
        user.setPassword("dummypassword");
        user.setFirstName("firstname");
        user.setLastName("lastName");
        user.setEnabled(true);
        user.setRoles("ADMIN,USER");
    }

    public void setUsername(final String username){
        user.setUsername(username);
    }

    public User getUser(){
        return user;
    }

    public void checkMockMvc(){
        if(mockMvc == null){
            mockMvc = MockMvcBuilders.standaloneSetup(users).build();
        }
    }

}
