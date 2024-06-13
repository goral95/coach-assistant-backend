package com.example.coachassistantbackend.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.coachassistantbackend.Entity.Role;
import com.example.coachassistantbackend.Entity.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmailShouldReturnUserWithGivenEmail() {
        // given
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByEmail("lukas@gmail.com");

        // then
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getName()).isEqualTo("Łukasz");
    }

    @Test
    void testFindByEmailShouldReturnEmptyOptional() {
        // given
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByEmail("Janush@gmail.com");

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testEnableUserShouldChangeUserFiledActivatedToTrue() {
        // given
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());
        userRepository.save(user);

        // when
        int result = userRepository.enableUser("lukas@gmail.com");

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
