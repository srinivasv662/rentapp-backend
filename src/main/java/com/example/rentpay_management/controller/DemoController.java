package com.example.rentpay_management.controller;

import com.example.rentpay_management.dto.DemoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GetMapping
    public ResponseEntity<?> getDetails() {
        DemoDto demoDto = new DemoDto();
        demoDto.setName("Sri");
        demoDto.setAge("25");
        System.out.println("Name: " + demoDto.getName() + " Age: " + demoDto.getAge());
        return ResponseEntity.status(200).body("You are allowed");
    }
}
