package com.example.SHOP_SELL_CLOTHING_PROJECT.IService;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/15
 * Time: 11:08 PM
 */

import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.UserDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.User;

import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

public interface UserService {
    Map<String, Object> createUser(UserDTO userDTO);
    Boolean authenticateUser(String userName, String passwordHash);
    APIResponse<User> loginAccount(String userName, String passwordHash);
}
