package com.devteria.profile.service;

import com.devteria.profile.dto.request.EmailRequest;
import com.devteria.profile.dto.request.SendEmailRequest;
import com.devteria.profile.dto.request.Sender;
import com.devteria.profile.dto.response.EmailResponse;
import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;
    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey;


    public EmailResponse sendEmail(SendEmailRequest request){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Sam")
                        .email("truongsam272@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey,emailRequest);
        }catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }

    }
}
