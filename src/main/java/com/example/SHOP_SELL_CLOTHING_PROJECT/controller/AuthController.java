package com.example.SHOP_SELL_CLOTHING_PROJECT.controller;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/16
 * Time: 11:36 AM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.LoginDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.UserDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@RestController
@RequestMapping("/api/auth")
@Validated
@Slf4j
public class AuthController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@Valid @RequestBody UserDTO userDTO) {
//        log.debug("Register user: {}", userDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> data = userServiceImpl.createUser(userDTO);
            int code = (Integer) data.get("CODE");

            if (code == 3)
                return ResponseEntity.ok(new APIResponse<>(code, "Username already exists", "", ResponseType.SUCCESS));

            int userID = (Integer) data.get("USER_ID");
            return ResponseEntity.ok(new APIResponse<>(code, "User registered successfully", objectMapper.writeValueAsString(userID), ResponseType.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new APIResponse<>(1, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<User>> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            APIResponse<User> response = userServiceImpl.loginAccount(loginDTO.getUserName(), loginDTO.getPassword());

            return ResponseEntity.ok()
                .body(new APIResponse<>(response.getCode(), response.getMessage(), response.getData(), response.getResponseType()));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new APIResponse<>(1, e.getMessage(), null));
        }
    }
}
