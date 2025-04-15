package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:07 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.UserService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.UserDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.UserRepository;

import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> createUser(UserDTO userDTO) {
        return userRepository.createUser(
                userDTO.getUserName(),
                userDTO.getPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPhoneNumber(),
                userDTO.getAuthProvider().toString()
        );
    }

    @Override
    public Boolean authenticateUser(String userName, String passwordHash) {
        return userRepository.authenticateUser(userName, passwordHash);
    }

    @Override
    public APIResponse<User> loginAccount(String userName, String passwordHash) {
        try {
//            Check if user exists
            User user = userRepository.findByUsername(userName);
            if (user == null) {
                return new APIResponse<>(2, "User not found", null);
            }

//            Verify password
            if (!passwordHash.equals(user.getPasswordHash())) {
                return new APIResponse<>(2,"Invalid password", null);
            }

            return new APIResponse<>(0, "Login successful", user);
        } catch (Exception e) {
            return new APIResponse<>(1, e.getMessage(), null);
        }
    }
}
