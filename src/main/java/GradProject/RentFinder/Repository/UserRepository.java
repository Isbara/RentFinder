package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM USER_TABLE WHERE EMAIL = :email")
    Optional<User> findByEmail(@Param("email") String email);

    void deleteByEmail(String email);
}
