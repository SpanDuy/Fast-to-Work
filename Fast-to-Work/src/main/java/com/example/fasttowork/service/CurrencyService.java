package com.example.fasttowork.service;

import com.example.fasttowork.entity.CurrencyFromNBRB;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CurrencyService {
    CurrencyFromNBRB getCurrencyFromNBRB();
    Double convertFromBYNToUSD(Double currency);
}
