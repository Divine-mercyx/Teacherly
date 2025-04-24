package org.Teacherly.services.servicesImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.Teacherly.data.models.User;
import org.Teacherly.data.repositories.UserRepo;
import org.Teacherly.dtos.request.ChangePasswordRequest;
import org.Teacherly.dtos.request.UserLoginRequest;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.services.servicesInterfaces.AuthService;
import org.Teacherly.utils.JwtUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Validated
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OtpServiceImpl otpService;

    @Autowired
    private Validator validator;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";





    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public UserResponse register(@Valid User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateUserEmail(user);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User savedUser = userRepo.save(user);
        String token = jwtUtils.generateToken(user.getId());
        return mapUserToUserResponse(token, savedUser);
    }

    private static UserResponse mapUserToUserResponse(String token, User savedUser) {
        return new UserResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getProfile());
    }

    private void validateUserEmail(User user) {
        if (userRepo.existsByEmail(user.getEmail())) throw new IllegalArgumentException("email already in use");
        if (!isValidEmail(user.getEmail())) throw new IllegalArgumentException("email is invalid");
    }

    @Override
    public UserResponse login(@Valid UserLoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("user not found with email " + request.getEmail()));
        hashPassword(!BCrypt.checkpw(request.getPassword(), user.getPassword()));
        return mapUserToUserResponse(jwtUtils.generateToken(user.getId()), user);
    }

    private static void hashPassword(boolean request) {
        if (request) throw new IllegalArgumentException("Password not match");
    }

    @Override
    public String sendOtp(String email) {
        return otpService.sendOtp(email);
    }

    @Override
    public void changePassword(@Valid ChangePasswordRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("user not found with email " + request.getEmail()));
        user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        userRepo.save(user);
    }

    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
}
