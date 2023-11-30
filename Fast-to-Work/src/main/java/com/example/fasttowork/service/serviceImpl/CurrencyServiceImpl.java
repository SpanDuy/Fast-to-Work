package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.service.CurrencyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public String getCurrencyFromNBRB() {
        StringBuilder response = new StringBuilder();

        try {
            String currencyCode = "USD"; // Код валюты, для которой вы хотите получить курс

            // Формирование URL для запроса к API НБРБ
            URL url = new URL("https://www.nbrb.by/api/exrates/rates/" + currencyCode + "?parammode=2");

            // Открытие соединения
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса
            connection.setRequestMethod("GET");

            // Получение ответа от сервера
            int responseCode = connection.getResponseCode();

            // Если ответ успешный (HTTP 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Чтение данных из потока
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

            } else {
                response.append("Failed to get data. Response code: " + responseCode);
            }

            // Закрытие соединения
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public Double getCurrentOfficialRate() {
        try {
            // Ваша JSON-строка
            String jsonString = getCurrencyFromNBRB();

            // Используем ObjectMapper из библиотеки Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Преобразование JSON-строки в объект JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Получение значения курса валюты
            double officialRate = jsonNode.get("Cur_OfficialRate").asDouble();

            // Вывод значения курса валюты
            return officialRate;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

}