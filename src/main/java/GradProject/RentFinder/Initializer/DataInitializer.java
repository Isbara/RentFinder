package GradProject.RentFinder.Initializer;

import GradProject.RentFinder.Models.Image;
import GradProject.RentFinder.Models.Role;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.Repository.PropertyRepository; // Import the PropertyRepository
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import GradProject.RentFinder.Mapper.UserMapper;

import java.util.Date;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PropertyRepository propertyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeUser();
      //  initializeProperty();
    }

    private void initializeUser() {
        // Create and save a user entity
        User user = User.builder()
                .name("Canberk")
                .surname("Diner")
                .email("canberkdiner@hotmail.com")
                .password(passwordEncoder.encode("Basket1."))
                .phoneNumber("5338727035")
                .dateOfBirth(new Date())
                .karmaPoint(100)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

 /*   private void initializeProperty() {
        // Retrieve the user entity representing Canberk
        Optional<User> owner = userRepository.findByEmail("canberkdiner@hotmail.com"); // Adjust this according to your UserRepository method

        // Create and save a property entity with Canberk as the owner
        Property property = Property.builder()
                .propertyType('A')
                .flatNo(123)
                .address("Sample Address")
                .description("Sample Description")
                .price(1000)
                .placeOffers("Sample Offers")
                .owner(UserMapper.ConvertOptional(owner)) // Set Canberk as the owner of the property
                .build();
        Image image = Image.builder()
                .url("https://example.com/sample-image.jpg") // Replace with your image URL
                .build();

        image.setProperty(property); // Set the property for the image
        property.getImages().add(image); // Add the image to the property

        propertyRepository.save(property);
    }
    */


}
