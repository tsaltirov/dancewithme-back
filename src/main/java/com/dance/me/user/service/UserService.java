package com.dance.me.user.service;

import org.springframework.stereotype.Service;

import com.dance.me.common.exception.ResourceNotFoundException;
import com.dance.me.user.dto.UserResponse;
import com.dance.me.user.mapper.UserMapper;
import com.dance.me.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
    }
}
