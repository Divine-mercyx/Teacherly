package org.Teacherly.services.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.Teacherly.data.models.Profile;
import org.Teacherly.data.models.Role;
import org.Teacherly.data.models.Subscription;
import org.Teacherly.data.models.User;
import org.Teacherly.data.repositories.UserRepo;
import org.Teacherly.dtos.request.GetSubscribersRequest;
import org.Teacherly.dtos.request.ProfileUpdateRequest;
import org.Teacherly.dtos.response.SubscriberResponse;
import org.Teacherly.dtos.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
class SubscriptionServiceImplTest {

    @Autowired
    private SubscriptionServiceImpl subscriptionService;
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepo userRepo;
    private User user1;
    private User user2;
    private Profile profile1;
    private Profile profile2;

    @BeforeEach
    void setUp() {
        subscriptionService.deleteAll();
        profile1 = new Profile();
        profile1.setFirstName("John");
        profile1.setLastName("Doe");
        profile1.setBio("bio");
        profile1.setAge("24");
        profile1.setRole(Role.TEACHER);

        profile2 = new Profile();
        profile2.setFirstName("Jane");
        profile2.setLastName("Doe");
        profile2.setBio("bio");
        profile2.setAge("25");
        profile2.setRole(Role.STUDENT);

        user1 = new User();
        user1.setEmail("email@gmail.com");
        user1.setPassword("password");
        user2 = new User();
        user2.setEmail("email1@gmail.com");
        user2.setPassword("password");
        userService.deleteAllProfile();
        authService.deleteAllUsers();
    }

    @AfterEach
    void tearDown() {
        subscriptionService.deleteAll();
        userService.deleteAllProfile();
        authService.deleteAllUsers();
    }

    @Test
    public void subscribeUser_subscriptionTest() {
        UserResponse student = authService.register(user1);
        ProfileUpdateRequest profileRequest = new ProfileUpdateRequest();
        profileRequest.setId(student.getId());
        profileRequest.setToken(student.getToken());
        profileRequest.setProfile(profile2);
        UserResponse studentWithProfile = userService.updateProfile(profileRequest);

        UserResponse teacher = authService.register(user2);
        ProfileUpdateRequest profileRequest2 = new ProfileUpdateRequest();
        profileRequest2.setId(teacher.getId());
        profileRequest2.setToken(teacher.getToken());
        profileRequest2.setProfile(profile1);
        UserResponse teacherWithProfile = userService.updateProfile(profileRequest2);

        Subscription subscription = new Subscription();
        subscription.setSubscriberUserId(studentWithProfile.getId());
        subscription.setSubscribedToUserId(teacherWithProfile.getId());
        Subscription subscription2 = subscriptionService.subscribe(subscription);
        assertNotNull(subscription2);
        assertEquals(subscription2.getSubscribedToUserId(), teacherWithProfile.getId());
    }

    @Test
    public void subscribeUser_unSubscribeUser_subscriptionTest() {
        UserResponse student = authService.register(user1);
        ProfileUpdateRequest profileRequest = new ProfileUpdateRequest();
        profileRequest.setId(student.getId());
        profileRequest.setToken(student.getToken());
        profileRequest.setProfile(profile2);
        UserResponse studentWithProfile = userService.updateProfile(profileRequest);

        UserResponse teacher = authService.register(user2);
        ProfileUpdateRequest profileRequest2 = new ProfileUpdateRequest();
        profileRequest2.setId(teacher.getId());
        profileRequest2.setToken(teacher.getToken());
        profileRequest2.setProfile(profile1);
        UserResponse teacherWithProfile = userService.updateProfile(profileRequest2);

        Subscription subscription = new Subscription();
        subscription.setSubscriberUserId(studentWithProfile.getId());
        subscription.setSubscribedToUserId(teacherWithProfile.getId());
        Subscription subscription2 = subscriptionService.subscribe(subscription);
        assertNotNull(subscription2);
        assertEquals(subscription2.getSubscribedToUserId(), teacherWithProfile.getId());

        String response = subscriptionService.unsubscribe(subscription2);
        assertNotNull(response);
        assertEquals(response, "deleted successfully");
    }

    @Test
    public void subscribeUser_getAllSubscribers_subscriptionTest() {
        UserResponse student = authService.register(user1);
        ProfileUpdateRequest profileRequest = new ProfileUpdateRequest();
        profileRequest.setId(student.getId());
        profileRequest.setToken(student.getToken());
        profileRequest.setProfile(profile2);
        UserResponse studentWithProfile = userService.updateProfile(profileRequest);

        UserResponse teacher = authService.register(user2);
        ProfileUpdateRequest profileRequest2 = new ProfileUpdateRequest();
        profileRequest2.setId(teacher.getId());
        profileRequest2.setToken(teacher.getToken());
        profileRequest2.setProfile(profile1);
        UserResponse teacherWithProfile = userService.updateProfile(profileRequest2);

        Subscription subscription = new Subscription();
        subscription.setSubscriberUserId(studentWithProfile.getId());
        subscription.setSubscribedToUserId(teacherWithProfile.getId());
        Subscription subscription2 = subscriptionService.subscribe(subscription);
        assertNotNull(subscription2);
        assertEquals(subscription2.getSubscribedToUserId(), teacherWithProfile.getId());

        GetSubscribersRequest request = new GetSubscribersRequest();
        request.setId(teacherWithProfile.getId());
        request.setToken(teacherWithProfile.getToken());
        List<SubscriberResponse> subs = userService.getSubscribers(request);
        assertNotNull(subs);
        assertEquals(subs.size(), 1);
        assertEquals(subs.getFirst().getId(), studentWithProfile.getId());
    }
}