package com.estudochat.ws.services;


import com.estudochat.ws.data.User;
import com.estudochat.ws.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findChatUsers(){
        return userRepository.findAll();
    }

}
