package com.sundeep.api.controller;

import java.util.List;
import com.sundeep.api.exception.InternalServerException;
import com.sundeep.api.model.MessageCount;
import com.sundeep.api.model.UserMessage;
import com.sundeep.api.service.UserMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user-messages/v1")
public class UserMessaging {

    @Autowired
    UserMessagingService userMessagingService;

    @PostMapping
    public boolean addMessage(@RequestBody @Valid final UserMessage message, final HttpServletResponse response) throws InternalServerException {
        boolean result = userMessagingService.addMessage(message);
        if(result){
            response.setStatus(HttpStatus.CREATED.value());
        }
        return result;
    }

    @PutMapping
    public boolean updateReadMessages(@RequestBody @Valid final UserMessage message, final HttpServletResponse response) throws InternalServerException {
        boolean result = userMessagingService.updateReadMessages(message.getSender(), message.getReceiver());
        return result;
    }

    @GetMapping("/{receiver}")
    public List<MessageCount> getMessageCount(@PathVariable final String receiver){
        return userMessagingService.getMessageCount(receiver);
    }

    @GetMapping("/{receiver}/{sender}")
    public List<UserMessage> getUserMessages(@PathVariable final String receiver, @PathVariable final String sender){
        return userMessagingService.getUserMessages(sender, receiver);
    }

}
