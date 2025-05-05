package com.example.demo.svc;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountSvc {
    @GetMapping("/hello")
    public Map<String, String> syHello() {
        return Map.of("message", "Hello World!");
    }

}
