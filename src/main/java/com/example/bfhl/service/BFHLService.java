package com.example.bfhl.service;

import com.example.bfhl.dto.RequestDTO;
import com.example.bfhl.dto.ResponseDTO;

public interface BFHLService {
    ResponseDTO process(RequestDTO request);
}
