package ru.rebook.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.rebook.model.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @Sql("/sql/users.sql")
    void findByEmail_ExistentUser() {
        String email = "ivan@ivan";
        Optional<User> user = userRepository.findByEmail(email);
        assertFalse(user.isEmpty());
    }

    @Test
    @Sql("/sql/users.sql")
    void findByEmail_NonExistentUser() {
        String email = "nonexistent@nonexistent";
        Optional<User> user = userRepository.findByEmail(email);
        assertTrue(user.isEmpty());
    }

    @Test
    @Sql("/sql/users.sql")
    void existsByEmail_ExistentUser_ReturnsTrue() {
        String email = "ivan@ivan";
        assertTrue(userRepository.existsByEmail(email));
    }

    @Test
    @Sql("/sql/users.sql")
    void existsByEmail_NonExistentUser_ReturnsFalse() {
        String email = "ivan@ivan";
        assertFalse(userRepository.existsByEmail(email));
    }
}