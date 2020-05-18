package com.sundeep.api.repository;

import com.sundeep.api.model.MessageCount;
import com.sundeep.api.model.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserMessagingRepository {

    private final static String INSERT_USER_MESSAGE = "insert into messages (sender, receiver, message) " +
            "values (:sender, :receiver, :message)";
    private final static String GET_UNREAD_MESSAGES = "select u.username sender, u.first_name firstName, u.last_name lastname, " +
            "SUM(case when m.is_read is null then 0 else 1 end) as unReadMessageCount, max(m.message_time) as lastMessageTime from users u left join messages m " +
            "on u.username=m.sender and m.is_read=0 and m.receiver=:receiver " +
            "where u.username<>:receiver group by u.username, u.first_name, u.last_name order by lastMessageTime desc, u.last_name asc";
    private final static String GET_USER_MESSAGES = "select sender, receiver, message, message_time messageTime, is_read read " +
            "from messages where (sender=:sender and receiver=:receiver) or (sender=:receiver and receiver=:sender) " +
            "order by message_time desc";

    private final static String UPDATE_READ_MESSAGES = "update messages set is_read=1 where sender=:sender and receiver=:receiver " +
            "and is_read=0";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean addMessage(UserMessage message){
        final Map<String,Object> params = new HashMap<>();
        params.put("sender", message.getSender());
        params.put("receiver", message.getReceiver());
        params.put("message", message.getMessage());
        final int result = namedParameterJdbcTemplate.update( INSERT_USER_MESSAGE, params );
        return (result == 1)? true : false;
    }

    public List<MessageCount> getMessageCount(final String receiver )  {
        final Map<String,Object> params = new HashMap<>();
        params.put("receiver", receiver);
        return namedParameterJdbcTemplate.query(GET_UNREAD_MESSAGES, params, BeanPropertyRowMapper.newInstance(MessageCount.class));
    }

    public List<UserMessage> getUserMessages(final String sender, final String receiver){
        final Map<String,Object> params = new HashMap<>();
        params.put("sender", sender);
        params.put("receiver", receiver);
        return namedParameterJdbcTemplate.query(GET_USER_MESSAGES, params, BeanPropertyRowMapper.newInstance(UserMessage.class));
    }

    public boolean updateReadMessages(final String sender, final String receiver) {
        final Map<String, Object> params = new HashMap<>();
        params.put("sender", sender);
        params.put("receiver", receiver);
        final int result = namedParameterJdbcTemplate.update( UPDATE_READ_MESSAGES, params );
        return (result == 1)? true : false;
    }

}
