package org.Teacherly.services.servicesImpl;

import org.Teacherly.data.models.Profile;
import org.Teacherly.data.models.User;
import org.Teacherly.data.models.Video;
import org.Teacherly.data.repositories.ProfileRepo;
import org.Teacherly.data.repositories.UserRepo;
import org.Teacherly.dtos.request.GetAllVideosRequest;
import org.Teacherly.dtos.request.VideoPostRequest;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.dtos.response.VideoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProfileRepo profileRepo;
    private Video video;
    private User user;
    private Profile profile;

    @BeforeEach
    void setUp() {
        userService.deleteAllProfile();
        userService.deleteAllVideos();
        video = new Video();
        video.setTitle("Test Video");
        video.setDescription("Test Description");
        video.setUrl("video url");
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        profile = new Profile();
        profile.setFirstName("firstname");
        profile.setLastName("lastname");
        authService.deleteAllUsers();
    }

    @AfterEach
    void tearDown() {
        userService.deleteAllProfile();
        userService.deleteAllVideos();
        authService.deleteAllUsers();
    }

    @Test
    public void postVideo_saveVideo_userServiceTest() {
        Profile savedProfile = profileRepo.save(profile);
        user.setProfile(savedProfile);
        UserResponse savedUser = authService.register(user);

        User user = userRepo.findById(savedUser.getId()).get();
        video.setUser(user);
        VideoPostRequest videoPostRequest = new VideoPostRequest();
        videoPostRequest.setVideo(video);
        videoPostRequest.setToken(savedUser.getToken());
        videoPostRequest.setId(savedUser.getId());
        VideoResponse video = userService.PostVideo(videoPostRequest);
        assertNotNull(video);
        assertEquals("Test Video", video.getTitle());
    }

    @Test
    public void postVideo_saveVideo_getPostedVideo() {
        Profile savedProfile = profileRepo.save(profile);
        user.setProfile(savedProfile);
        UserResponse savedUser = authService.register(user);

        User user = userRepo.findById(savedUser.getId()).get();
        video.setUser(user);
        VideoPostRequest videoPostRequest = new VideoPostRequest();
        videoPostRequest.setVideo(video);
        videoPostRequest.setToken(savedUser.getToken());
        videoPostRequest.setId(savedUser.getId());
        VideoResponse video = userService.PostVideo(videoPostRequest);
        List<VideoResponse> videos = userService.getAllVideoPostedByUser(new GetAllVideosRequest(savedUser.getToken(), savedUser.getId()));
        assertEquals(1, videos.size());
        assertEquals(video.getId(), videos.getFirst().getId());
    }
}