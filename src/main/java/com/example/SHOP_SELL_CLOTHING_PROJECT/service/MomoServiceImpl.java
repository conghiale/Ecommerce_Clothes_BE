package com.example.SHOP_SELL_CLOTHING_PROJECT.service;

import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.PaymentMethod;
import com.example.SHOP_SELL_CLOTHING_PROJECT.ENUM.ResponseType;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.MomoService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.IService.PaymentService;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentDTO;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentRequest;
import com.example.SHOP_SELL_CLOTHING_PROJECT.dto.PaymentResponse;
import com.example.SHOP_SELL_CLOTHING_PROJECT.model.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoServiceImpl implements MomoService {

    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    private static final String REDIRECT_URL = "http://localhost:3000/whatever"; // URL redirect
    private static final String IPN_URL = "http://localhost:8080/IPN"; // handle Callback
    // private static final String REQUEST_TYPE = "captureWallet";
    private static final String REQUEST_TYPE = "payWithMethod";

    private ObjectMapper objectMapper = new ObjectMapper();

    private Integer orderID; // Vi orderID sinh ngau nhien trong db nen bị trung lap voi orderID trong db của MOMO
    // => nên sẽ lưu orderID khi truyền lên ở đây và giả lặp orderID ngẫu nhiên để test chức năng thanh toán MOMO
    // => Sau khi callback thì dùng orderID ở đây để câ nhật trong db

    @Autowired
    private PaymentService paymentService;

    @Override
    public APIResponse<String> createPaymentRequest(PaymentRequest paymentRequest) {
        try {
//             Generate requestId and orderId
            String requestId = PARTNER_CODE + new Date().getTime();
//            Integer orderId = paymentRequest.getOrderId();
            orderID = Integer.parseInt(paymentRequest.getOrderId());
            String orderId = requestId;
            String orderInfo = "SN Mobile";
            String extraData = "";

//             Generate raw signature
            String rawSignature = String.format(
                    "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                    ACCESS_KEY, paymentRequest.getAmount(), extraData, IPN_URL, orderId, orderInfo, PARTNER_CODE, REDIRECT_URL,
                    requestId, REQUEST_TYPE);

//             Sign with HMAC SHA256
            String signature = signHmacSHA256(rawSignature, SECRET_KEY);
            System.out.println("Generated Signature: " + signature);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", PARTNER_CODE);
            requestBody.put("accessKey", ACCESS_KEY);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", paymentRequest.getAmount());
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", REDIRECT_URL);
            requestBody.put("ipnUrl", IPN_URL);
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", REQUEST_TYPE);
            requestBody.put("signature", signature);
            requestBody.put("lang", "en");

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://test-payment.momo.vn/v2/gateway/api/create");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody), StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                System.out.println("Response from MoMo: " + result);

                PaymentResponse paymentResponse = objectMapper.readValue(result.toString(), PaymentResponse.class);
                paymentResponse.setOrderId(String.valueOf(orderID));
                //                return result.toString();
                return new APIResponse<>(
                        paymentResponse.getResultCode(),
                        paymentResponse.getMessage(),
                        objectMapper.writeValueAsString(paymentResponse),
                        paymentResponse.getResultCode() == 0 ? ResponseType.SUCCESS : ResponseType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //                return result.toString();
            return new APIResponse<>(1, e.getMessage(), null, ResponseType.ERROR);
//            return "{\"error\": \"Failed to create payment request: " + e.getMessage() + "\"}";
        }
    }

    // HMAC SHA256 signing method
    private static String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public APIResponse<String> checkPaymentStatus(PaymentResponse paymentResponse) {
        try {
//            Generate requestId
            String requestId = PARTNER_CODE + new Date().getTime();

//            Generate raw signature for the status check
            String rawSignature = String.format(
                    "accessKey=%s&orderId=%s&partnerCode=%s&requestId=%s",
                    ACCESS_KEY, paymentResponse.getOrderId(), PARTNER_CODE, requestId);

//            Sign with HMAC SHA256
            String signature = signHmacSHA256(rawSignature, SECRET_KEY);
            System.out.println("Generated Signature for Status Check: " + signature);

//            Prepare request body
//            JSONObject requestBody = new JSONObject();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", PARTNER_CODE);
            requestBody.put("accessKey", ACCESS_KEY);
            requestBody.put("requestId", requestId);
            requestBody.put("orderId", paymentResponse.getOrderId());
            requestBody.put("signature", signature);
            requestBody.put("lang", "en");

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://test-payment.momo.vn/v2/gateway/api/query");
            httpPost.setHeader("Content-Type", "application/json");
//            httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
            httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                System.out.println("Response from MoMo (Status Check): " + result);
                PaymentResponse paymentResponseCheckStatus = objectMapper.readValue(result.toString(), PaymentResponse.class);
                paymentResponseCheckStatus.setOrderId(String.valueOf(orderID));

                PaymentDTO paymentDTO = new PaymentDTO(orderID, PaymentMethod.MOMO.toString(), paymentResponseCheckStatus.getRequestId());
                APIResponse<String> resultData = paymentService.paymentProcess(paymentDTO.getOrderId(), paymentDTO);

                resultData.setData(objectMapper.writeValueAsString(paymentResponseCheckStatus));
                return resultData;

//                return result.toString();
//                return new APIResponse<>(
//                        paymentResponseCheckStatus.getResultCode(),
//                        paymentResponseCheckStatus.getMessage(),
//                        result.toString(),
//                        paymentResponseCheckStatus.getResultCode() == 0 ? ResponseType.SUCCESS : ResponseType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse<>(1, e.getMessage(), null, ResponseType.ERROR);
//            return "{\"error\": \"Failed to check payment status: " + e.getMessage() + "\"}";
        }
    }

}
