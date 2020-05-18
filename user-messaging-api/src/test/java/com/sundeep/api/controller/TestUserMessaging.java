package com.sundeep.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundeep.api.context.DataSourceContext;
import com.sundeep.api.model.User;
import com.sundeep.api.model.UserMessage;
import com.sundeep.api.repository.UserMessagingRepository;
import com.sundeep.api.service.UserMessagingService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserMessaging.class, UserMessagingService.class, UserMessagingRepository.class, DataSourceContext.class})
@WebMvcTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestUserMessaging {
    private final static String TEST_USER_ID = "dummy-id";

    private User user;
    private UserMessage userMessage;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    final ObjectMapper mapper = new ObjectMapper();
    private final static String INSERT_USER_QUERY = "INSERT INTO USERS ( username, first_name, last_name, password, enabled ) VALUES ( :username, :firstName, :lastName, :password, :enabled )";

    @BeforeAll
    public void createUsers() throws Exception{
        createUser();
        addUser(user);
        user.setUsername("usertest1");
        addUser(user);
    }

    @AfterAll
    public void clearUsers() throws Exception{
        jdbcTemplate.update("truncate table users");
        clearUserMessages();

    }

    @Test
    public void testAddMessage() throws Exception{
        setUserMessage("usertest", "usertest1", "hello");
        testAddMessage(userMessage);
        testGetMessageCount("usertest1", 1, 0);
        testUserMessages("usertest1", "usertest", 1);
        clearUserMessages();

    }

    @Test
    public void testUpdateReadMessages() throws Exception{
        setUserMessage("usertest", "usertest1", "hello");
        testAddMessage(userMessage);
        testGetMessageCount("usertest1", 1, 0);
        testUserMessages("usertest1", "usertest", 1);
        testUpdateUserMessages(userMessage);
        testGetMessageCount("usertest1", 1, 0);
        clearUserMessages();
    }

    private void testAddMessage( final UserMessage userMessage) throws Exception {
        final String content = mapper.writeValueAsString(userMessage);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user-messages/v1")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    private void testGetMessageCount(final String receiver, final int size, final int unReadMessageCount) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user-messages/v1/"+receiver)
                .with(user(TEST_USER_ID))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(size)))
                .andExpect(jsonPath("$[0].unReadMessageCount", Matchers.is(unReadMessageCount)))
                .andReturn();
    }
    private void testUserMessages(final String receiver, final String sender, final int size) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user-messages/v1/"+receiver+"/"+sender)
                .with(user(TEST_USER_ID))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(size)))
                .andReturn();
    }

    private void testUpdateUserMessages(final UserMessage userMessage) throws Exception {
        final String content = mapper.writeValueAsString(userMessage);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user-messages/v1")
                .with(user(TEST_USER_ID))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    private void setUserMessage( final String sender, final String receiver, final String message){
        if( userMessage == null ){
            userMessage = new UserMessage();
        }
        userMessage.setSender(sender);
        userMessage.setReceiver(receiver);
        userMessage.setMessage(message);

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

    private void addUser(final User user){
        final Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("password", user.getPassword());
        params.put("enabled", user.isEnabled());

        namedParameterJdbcTemplate.update(INSERT_USER_QUERY, params);

    }

    private void clearUserMessages(){
        jdbcTemplate.update("truncate table messages");
    }

}
