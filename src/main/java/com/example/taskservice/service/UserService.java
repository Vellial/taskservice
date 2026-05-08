package com.example.taskservice.service;

import com.example.taskservice.entity.User;
import com.example.taskservice.exception.UserNotFoundException;
import com.example.taskservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
    }
}
