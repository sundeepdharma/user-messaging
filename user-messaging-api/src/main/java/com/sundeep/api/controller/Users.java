package com.sundeep.api.controller;

import com.sundeep.api.exception.InternalServerException;
import com.sundeep.api.exception.ResourceNotFoundException;
import com.sundeep.api.model.User;
import com.sundeep.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users/v1")
public class Users {

    @Autowired
    UserService userService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    List<User> getUsers()
    {
        return userService.getUsers();
    }

    @GetMapping(path = "/{username}", produces = {MediaType.APPLICATION_JSON_VALUE})
    User getUser(@PathVariable final String username, final HttpServletResponse response) throws ResourceNotFoundException
    {
        final User user = userService.getUserByName(username);
        user.setPassword(null);
        return user;
    }

    @PostMapping
    public boolean addOrUpdateUser(@RequestBody @Valid final User user, final HttpServletResponse response) throws InternalServerException {
        final boolean result = userService.addOrUpdateUser(user);
        if(result){
            response.setStatus(HttpStatus.CREATED.value());
        }
        return result;
    }

    @DeleteMapping("/{username}")
    public boolean deleteUser(@PathVariable final String username){
        return userService.deleteUser(username);
    }

}
