package com.example.demo.demoController;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final UserRepository userRepository;

    public List<User> findAllUser() {
        List<User> list = userRepository.findAll();
        return list;
    }

}
