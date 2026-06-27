package com.example.bfhl.controller;

import com.example.bfhl.dto.RequestDTO;
import com.example.bfhl.dto.ResponseDTO;
import com.example.bfhl.service.BFHLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class BFHLController {

    @Autowired
    private BFHLService service;

    @PostMapping("/bfhl")
    public ResponseEntity<ResponseDTO> process(@RequestBody RequestDTO request) {
        try {
            ResponseDTO response = service.process(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder()
                    .isSuccess(false)
                    .userId("")
                    .email("")
                    .rollNumber("")
                    .oddNumbers(Collections.emptyList())
                    .evenNumbers(Collections.emptyList())
                    .alphabets(Collections.emptyList())
                    .specialCharacters(Collections.emptyList())
                    .sum("0")
                    .concatString("")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/bfhl")
    public ResponseEntity<Map<String, String>> getStatus() {
        return ResponseEntity.ok(Map.of("operation_code", "1"));
    }
}
