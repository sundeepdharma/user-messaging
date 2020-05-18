package com.sundeep.api.service;

import com.sundeep.api.model.MessageCount;
import com.sundeep.api.model.UserMessage;
import com.sundeep.api.repository.UserMessagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMessagingService {

    @Autowired
    UserMessagingRepository userMessagingRepository;

    public boolean addMessage(UserMessage message){
        return userMessagingRepository.addMessage(message);
    }

    public List<MessageCount> getMessageCount(final String receiver )  {
        return userMessagingRepository.getMessageCount(receiver);
    }

    public List<UserMessage> getUserMessages(final String sender, final String receiver){
        return userMessagingRepository.getUserMessages(sender, receiver);
    }

    public boolean updateReadMessages(final String sender, final String receiver) {
        return userMessagingRepository.updateReadMessages(sender, receiver);
    }

}
