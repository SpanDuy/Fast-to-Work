package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.CurrencyFromNBRB;
import com.example.fasttowork.service.CurrencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CurrencyFromNBRB getCurrencyFromNBRB() {
        try {
            String currencyCode = "USD";
            String url = "https://www.nbrb.by/api/exrates/rates/" + currencyCode + "?parammode=2";
            String jsonString = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            CurrencyFromNBRB currency = objectMapper.readValue(jsonString, CurrencyFromNBRB.class);

            return currency;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception during deserialization: " + e.getMessage());
            return new CurrencyFromNBRB();
        }
    }

    @Override
    public Double convertFromBYNToUSD(Double currency) {
        return currency / getCurrencyFromNBRB().getCurOfficialRate();
    }
}