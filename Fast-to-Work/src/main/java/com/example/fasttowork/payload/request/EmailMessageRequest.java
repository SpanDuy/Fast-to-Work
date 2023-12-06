package com.example.fasttowork.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageRequest {
    private String sender;
    private String emailAddress;
    private String subject;
    private String text;
}
