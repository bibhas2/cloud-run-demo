package com.example.demo.svc;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/account")
public class AccountSvc {
    private static final Logger logger = LoggerFactory.getLogger(AccountSvc.class);

    @GetMapping("/hello")
    public Map<String, String> syHello() {
        logger.info("Logging INFO with Logback");
        logger.error("Logging ERROR with Logback");
        logger.debug("Logging DEBUG with Logback");
        logger.warn("Logging WARN with Logback");
        logger.trace("Logging TRACE with Logback");
        
        return Map.of("message", "Hello World!");
    }

}
