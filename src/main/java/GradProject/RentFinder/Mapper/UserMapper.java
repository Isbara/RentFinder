package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {
    public User ConvertToModel(UserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        return user;
    }
    public UserRequest ConvertToRequest(User user){
        UserRequest request = new UserRequest();
        request.setName(user.getName());
        request.setSurname(user.getSurname());
        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword());
        request.setPhoneNumber(user.getPhoneNumber());
        request.setDateOfBirth(user.getDateOfBirth());
        return request;
    }
    public static User ConvertOptional(Optional<User> model){
        User user = new User();
        user.setUserID(model.get().getUserID());
        user.setName(model.get().getName());
        user.setSurname(model.get().getSurname());
        user.setEmail(model.get().getEmail());
        user.setPassword(model.get().getPassword());
        user.setPhoneNumber(model.get().getPhoneNumber());
        user.setDateOfBirth(model.get().getDateOfBirth());
        user.setKarmaPoint(model.get().getKarmaPoint());
        user.setRole(model.get().getRole());
        user.setProperties(model.get().getProperties());
        user.setReservations(model.get().getReservations());
        user.setTickets(model.get().getTickets());
        return user;
    }
}
