package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM USER_TABLE WHERE EMAIL = :email")
    Optional<User> findByEmail(@Param("email") String email);

    void deleteByEmail(String email);
}
