package com.sundeep.api.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserMessage {

    @NotEmpty
    @Size(min =4, max = 100)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String sender;

    @NotEmpty
    @Size(min =4, max = 100)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String receiver;

    @Size(max = 255)
    private String message;

    private Date messageTime;
    private boolean read;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
