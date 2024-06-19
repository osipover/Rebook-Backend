package ru.rebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rebook.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
            FROM User u WHERE u.email = :email
            """)
    boolean existsByEmail(@Param("email") String email);
}
