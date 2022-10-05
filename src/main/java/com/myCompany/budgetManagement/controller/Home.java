package com.myCompany.budgetManagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class Home {

    @GetMapping
    public Map<String, Object> root(){
        return Map.of(
                "version", 1,
                "paths", List.of("/users", "/transactions", "/household")
                );
    }

}
