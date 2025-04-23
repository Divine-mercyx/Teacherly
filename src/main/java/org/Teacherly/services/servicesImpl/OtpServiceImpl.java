package org.Teacherly.services.servicesImpl;

import org.Teacherly.data.models.Otp;
import org.Teacherly.data.repositories.OtpRepo;
import org.Teacherly.services.servicesInterfaces.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private OtpRepo otpRepository;

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String sendOtp(String email) {
        int otp = 1000 + random.nextInt(9000);
        Otp otpObj = new Otp();
        otpObj.setCode(String.valueOf(otp));
        otpRepository.save(otpObj);
        emailServiceImpl.sendSimpleMail(email, "OTP Code", otpObj.getCode());
        return otpObj.getCode();
    }
}
