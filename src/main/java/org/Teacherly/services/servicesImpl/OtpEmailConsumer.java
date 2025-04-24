package org.Teacherly.services.servicesImpl;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class OtpEmailConsumer {

    @Autowired
    private EmailServiceImpl emailService;

    @KafkaListener(topics = "otp-events", groupId = "otp-email-group")
    public void consume(String message) {
        JSONObject json = new JSONObject(message);
        String email = json.getString("email");
        String code = json.getString("code");
        emailService.sendSimpleMail(email, "Your OTP code", code);
    }
}
