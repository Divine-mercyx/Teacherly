package org.Teacherly.services.servicesImpl;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class OtpEmailConsumer {

    @Autowired
    private EmailServiceImpl emailService;

    @KafkaListener(topics = "otp-events", groupId = "otp-email-group")
    @Retryable(value = {RuntimeException.class}, maxAttempts = 5, backoff = @Backoff(delay = 5000, multiplier = 2))
    public void consume(String message) {
        JSONObject json = new JSONObject(message);
        String email = json.getString("email");
        String code = json.getString("code");
        try {
            emailService.sendSimpleMail(email, "OTP CODE", code);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send otp email " + e);
        }
    }
}
