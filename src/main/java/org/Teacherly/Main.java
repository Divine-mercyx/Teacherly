package org.Teacherly;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        System.setProperty("spring.data.mongodb.uri", dotenv.get("MONGO_URI"));
        System.setProperty("server.port", dotenv.get("PORT"));
        System.setProperty("spring.kafka.bootstrap-servers", dotenv.get("KAFKA_BOOTSTRAP"));

        System.setProperty("spring.mail.host", dotenv.get("MAIL_HOST"));
        System.setProperty("spring.mail.port", dotenv.get("MAIL_PORT"));
        System.setProperty("spring.mail.username", dotenv.get("MAIL_USERNAME"));
        System.setProperty("spring.mail.password", dotenv.get("MAIL_PASSWORD"));

        SpringApplication.run(Main.class, args);
    }
}