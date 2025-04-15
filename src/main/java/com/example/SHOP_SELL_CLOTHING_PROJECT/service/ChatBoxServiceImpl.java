package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/23
 * Time: 6:54 PM
 */


import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.ChatBoxService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.APIResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ChatBoxDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.ChatResponseDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.gemini.GeminiContent;
import com.example.SHOP_SELL_CLOTHING_PROJECT.gemini.GeminiPart;
import com.example.SHOP_SELL_CLOTHING_PROJECT.gemini.GeminiRequest;
import com.example.SHOP_SELL_CLOTHING_PROJECT.gemini.GeminiResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.repository.ChatBoxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * @ 2025. All rights reserved
 */

@Service
@RequiredArgsConstructor
public class ChatBoxServiceImpl implements ChatBoxService {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Autowired
    private ChatBoxRepository chatBoxRepository;

    @Autowired
    private APIResponseServiceImpl apiResponseServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private final WebClient webClient;

    @Override
    public APIResponse<String> chatConversationGetOrCreate(ChatBoxDTO chatBoxDTO) throws JsonProcessingException {
        Map<String, Object> result = chatBoxRepository.chatConversationGetOrCreate(chatBoxDTO.getUserId());

        Integer code = (Integer) result.get("CODE");

        if (code == 0 && result.get("CONVERSATION_ID") != null) {
            Integer conversationId = (Integer) result.get("CONVERSATION_ID");
            result.put("CONVERSATION_ID", conversationId);
            chatBoxDTO.setConversationId(conversationId);
            return new APIResponse<>(
                    code,
                    "Chat conversation retrieved/created successfully",
                    objectMapper.writeValueAsString(chatBoxDTO),
                    ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType()
            );
        }
    }

    @Override
    public APIResponse<String> chatMessageProcess(ChatBoxDTO chatBoxDTO) throws JsonProcessingException {
        Map<String, Object> result = chatBoxRepository.chatMessageProcess(
                chatBoxDTO.getUserId(),
                chatBoxDTO.getConversationId(),
                chatBoxDTO.getUserMessage());

        Integer code = (Integer) result.get("CODE");
        Integer userMessageId = (Integer) result.get("USER_MESSAGE_ID");

        if (code == 0 && userMessageId != null) {
            chatBoxDTO.setUserMessageId(userMessageId);
            return new APIResponse<>(code, "Message processed successfully", objectMapper.writeValueAsString(chatBoxDTO), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null, apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> chatBotResponseSave(ChatBoxDTO chatBoxDTO) throws JsonProcessingException {
        Map<String, Object> result = chatBoxRepository.chatBotResponseSave(
                chatBoxDTO.getConversationId(),
                chatBoxDTO.getUserId(),
                chatBoxDTO.getBotResponse(),
                chatBoxDTO.getUserMessageId()
        );

        Integer code = (Integer) result.get("CODE");
        Integer botMessageId = (Integer) result.get("BOT_MESSAGE_ID");

        if (code == 0 && botMessageId != null) {
            chatBoxDTO.setBotMessageId(botMessageId);
            return new APIResponse<>(code, "Bot response saved successfully",
                    objectMapper.writeValueAsString(chatBoxDTO), ResponseType.SUCCESS);
        } else {
            APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
            APIResponseDTO apiResponseDTO = apiResponse != null ?
                    objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                    new APIResponseDTO();

            return new APIResponse<>(code, apiResponseDTO.getMessage(), null,
                    apiResponseDTO.getResponseType());
        }
    }

    @Override
    public APIResponse<String> processUserMessage(ChatBoxDTO chatBoxDTO) throws JsonProcessingException {
//        Step 1: Get or create conversation
        Map<String, Object> conversationResult = chatBoxRepository.chatConversationGetOrCreate(chatBoxDTO.getUserId());

        Integer conversationCode = (Integer) conversationResult.get("CODE");
        if (conversationCode != 0 || conversationResult.get("CONVERSATION_ID") == null) {
            return getErrorResponse(conversationCode);
        }

        Integer conversationId = (Integer) conversationResult.get("CONVERSATION_ID");

//        Step 2: Save user message
        Map<String, Object> messageResult = chatBoxRepository.chatMessageProcess(chatBoxDTO.getUserId(), conversationId, chatBoxDTO.getUserMessage());

        Integer messageCode = (Integer) messageResult.get("CODE");
        if (messageCode != 0 || messageResult.get("USER_MESSAGE_ID") == null) {
            return getErrorResponse(messageCode);
        }

        Integer userMessageId = (Integer) messageResult.get("USER_MESSAGE_ID");

//        Step 3: Call Gemini API
        String botResponse = callGeminiAPI(chatBoxDTO.getUserMessage());

//        Step 4: Save bot response
        Map<String, Object> botResult = chatBoxRepository.chatBotResponseSave(conversationId, chatBoxDTO.getUserId(), botResponse, userMessageId);

        Integer botCode = (Integer) botResult.get("CODE");
        if (botCode != 0 || botResult.get("BOT_MESSAGE_ID") == null) {
            return getErrorResponse(botCode);
        }

        Integer botMessageId = (Integer) botResult.get("BOT_MESSAGE_ID");

//        Create response DTO
        ChatResponseDTO responseDTO = new ChatResponseDTO(
                conversationId,
                userMessageId,
                botMessageId,
                chatBoxDTO.getUserMessage(),
                botResponse
        );

        return new APIResponse<>(0, "Message processed successfully", objectMapper.writeValueAsString(responseDTO), ResponseType.SUCCESS);
    }

    private String callGeminiAPI(String userMessage) {
        try {
            GeminiRequest geminiRequest = new GeminiRequest(List.of(
                    new GeminiContent(List.of(new GeminiPart(userMessage)))
            ));

            GeminiResponse response = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .bodyValue(geminiRequest)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

            if (response != null && !response.getCandidates().isEmpty()
                    && response.getCandidates().get(0).getContent() != null) {
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }

            return "Sorry, I'm having trouble processing your question.";
        } catch (Exception e) {
            return "Sorry, there was an error connecting to the AI service.";
        }
    }

    private APIResponse<String> getErrorResponse(Integer code) throws JsonProcessingException {
        APIResponse<String> apiResponse = apiResponseServiceImpl.getAPIResponseByCode(code);
        APIResponseDTO apiResponseDTO = apiResponse != null ?
                objectMapper.readValue(apiResponse.getData(), APIResponseDTO.class) :
                new APIResponseDTO();

        return new APIResponse<>(code, apiResponseDTO.getMessage(), null,
                apiResponseDTO.getResponseType());
    }
}
