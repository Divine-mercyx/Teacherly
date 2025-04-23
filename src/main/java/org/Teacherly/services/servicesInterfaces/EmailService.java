package org.Teacherly.services.servicesInterfaces;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);
}
