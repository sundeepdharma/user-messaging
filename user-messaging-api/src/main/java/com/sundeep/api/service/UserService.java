package com.sundeep.api.service;

import com.sundeep.api.exception.InternalServerException;
import com.sundeep.api.exception.ResourceNotFoundException;
import com.sundeep.api.model.User;
import com.sundeep.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers()
    {
        return userRepository.getUsers();
    }

    public User getUserByName(final String name) throws ResourceNotFoundException
    {
        return userRepository.getUserByName( name );
    }

    public boolean addOrUpdateUser(final User user) throws InternalServerException{
        return userRepository.addOrUpdateUser(user);
    }

    public boolean deleteUser(final String username){
        return userRepository.deleteUser(username);
    }

}
