package com.example.coachassistantbackend.Repository;

import com.example.coachassistantbackend.Entity.Role;
import com.example.coachassistantbackend.Entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    private static final String EMAIL = "lukas@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String NAME = "Łukasz";
    private static final String SURNAME = "Górczyk";

    @Autowired
    private UserRepository userRepository;

    private static final User user = new User();

    @BeforeEach()
    static void prepare() {
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(Role.USER);
        user.setName(NAME);
        user.setSurname(SURNAME);
        user.setBirthDate(LocalDateTime.now());
    }

    @Test
    void testFindByEmailShouldReturnUserWithGivenEmail() {
        // given
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
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByEmail("Janush@gmail.com");

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testEnableUserShouldChangeUserFiledActivatedToTrue() {
        // given
        userRepository.save(user);

        // when
        int result = userRepository.enableUser("lukas@gmail.com");

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
