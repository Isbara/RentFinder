package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Respond;
import GradProject.RentFinder.RequestModel.RespondRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class RespondMapper {
    public Respond ConvertToModel(RespondRequest request){
        Respond respond = new Respond();
        respond.setDescription(request.getDescription());
        respond.setDate(request.getDate());
        return respond;
    }
    public RespondRequest ConvertToRequest(Respond respond){
        RespondRequest request = new RespondRequest();
        request.setDescription(respond.getDescription());
        request.setDate(respond.getDate());
        return request;
    }
    public Respond ConvertOptional(Optional<Respond> model){
        Respond respond = new Respond();
        respond.setCommentID(model.get().getCommentID());
        respond.setDescription(model.get().getDescription());
        respond.setDate(model.get().getDate());
        respond.setReview(model.get().getReview());
        return respond;
    }
}
