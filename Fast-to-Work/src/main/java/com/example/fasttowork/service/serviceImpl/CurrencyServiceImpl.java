package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.CurrencyFromNBRB;
import com.example.fasttowork.service.CurrencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;
    @Value("${nbrbUSD.api.url}")
    private String nbrbUSDApiUrl;

    @Override
    public CurrencyFromNBRB getCurrencyFromNBRB() {
        try {
            String jsonString = restTemplate.getForObject(nbrbUSDApiUrl, String.class);

            if (jsonString == null) {
                throw new RuntimeException("Received null response from the API");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, CurrencyFromNBRB.class);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception during deserialization: " + e.getMessage());
            return new CurrencyFromNBRB();
        }
    }

    @Override
    public Double convertFromBYNToUSD(Double currency) {
        Double curRate = getCurrencyFromNBRB().getCurOfficialRate();
        return currency / curRate;
    }
}