package com.example.demo.demoController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import com.example.demo.user.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/v1/demo")
@RequiredArgsConstructor
public class DemoController {
    
    private final DemoService demoService;

    @GetMapping("/hello")
    public ResponseEntity<List<User>> sayHello() {
        return ResponseEntity.ok(demoService.findAllUser());
    }
    
}
