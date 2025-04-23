package org.Teacherly.services.servicesImpl;

import org.Teacherly.data.models.User;
import org.Teacherly.dtos.request.ChangePasswordRequest;
import org.Teacherly.dtos.request.UserLoginRequest;
import org.Teacherly.dtos.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplTest {
    @Autowired
    private AuthServiceImpl authServiceImpl;

    private User user;
    private UserLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        authServiceImpl.deleteAllUsers();
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");

        loginRequest = new UserLoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword());
    }

    @AfterEach
    void tearDown() {
        authServiceImpl.deleteAllUsers();
    }

    @Test
    public void saveUser_checkToken_authServiceTest() {
        UserResponse savedUser = authServiceImpl.register(user);
        assertNotNull(savedUser);
        assertEquals(savedUser.getEmail(), user.getEmail());
        assertNotNull(savedUser.getToken());
        System.out.println(savedUser.getToken());
    }

    @Test
    public void saveUser_loginUser_authServiceTest() {
        assertThrows(IllegalArgumentException.class, () -> {authServiceImpl.login(loginRequest);});
        UserResponse savedUser = authServiceImpl.register(user);
        assertNotNull(savedUser);
        UserResponse savedUser2 = authServiceImpl.login(loginRequest);
        assertNotNull(savedUser2);
        assertEquals(savedUser2.getEmail(), user.getEmail());
        assertEquals(savedUser2.getId(), savedUser.getId());
    }

    @Test
    public void saveUser_ChangePassword_authServiceTest() {
        UserResponse savedUser = authServiceImpl.register(user);
        assertNotNull(savedUser);
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail(user.getEmail());
        request.setNewPassword("1234567");
        authServiceImpl.changePassword(request);
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.login(loginRequest));
        loginRequest.setPassword("1234567");
        UserResponse savedUser2 = authServiceImpl.login(loginRequest);
        assertNotNull(savedUser2);
    }
}