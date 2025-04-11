package com.studywith.api.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/connection")
public class ConnectionTestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("Successfully connected to frontend.");
        return "Successfully connected to backend.";
    }

}
