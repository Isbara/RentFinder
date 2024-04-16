package GradProject.RentFinder.Helper;

import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;

import java.util.Date;

public class MockObjects {
    public User user_1() {

        User user = new User();
        user.setUserID(1);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setPhoneNumber("1234567890");
        user.setDateOfBirth(new Date(1990, 0, 1));
        user.setKarmaPoint(100);
        return user;
    }
    public UserRequest userRequest_1() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Jane");
        userRequest.setSurname("Doe");
        userRequest.setEmail("jane.doe@example.com");
        userRequest.setPassword("password123");
        userRequest.setPhoneNumber("9876543210");
        userRequest.setDateOfBirth(new Date(1995, 5, 15));
        return userRequest;
    }

}
