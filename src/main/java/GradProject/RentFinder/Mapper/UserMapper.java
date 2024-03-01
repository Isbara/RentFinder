package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;

import java.util.Optional;

public class UserMapper {
    public User ConvertToModel(UserRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }
    public UserRequest ConvertToRequest(User company){
        UserRequest request = new UserRequest();
        request.setEmail(company.getEmail());
        request.setPassword(company.getPassword());
        return request;
    }
    public User ConvertOptional(Optional<User> model){
        User user = new User();
        user.setUserID(model.get().getUserID());
        user.setEmail(model.get().getEmail());
        user.setPassword(model.get().getPassword());
        user.setRole(model.get().getRole());
        return user;
    }
}
