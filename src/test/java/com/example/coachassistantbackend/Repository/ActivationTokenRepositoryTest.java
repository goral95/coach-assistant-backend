package com.example.coachassistantbackend.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.coachassistantbackend.Entity.ActivationToken;
import com.example.coachassistantbackend.Entity.Role;
import com.example.coachassistantbackend.Entity.User;

@DataJpaTest
public class ActivationTokenRepositoryTest {

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByTokenShouldReturnRightToken() {
        // given
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        ActivationToken token = new ActivationToken();
        token.setUser(savedUser);
        token.setToken("my-secret-token");
        token.setExpiredAt(LocalDateTime.now().plusDays(1));
        activationTokenRepository.save(token);

        // when
        Optional<ActivationToken> result = activationTokenRepository.findByToken("my-secret-token");

        // then
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getToken()).isEqualTo("my-secret-token");
    }

    @Test
    void testFindByTokenShouldReturnEmptyOptional() {
        // given
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        ActivationToken token = new ActivationToken();
        token.setUser(savedUser);
        token.setToken("my-secret-token");
        token.setExpiredAt(LocalDateTime.now().plusDays(1));
        activationTokenRepository.save(token);

        // when
        Optional<ActivationToken> result = activationTokenRepository.findByToken("my-genius-token");

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testConfirmTokenShouldChangeTokenFiledConfirmedToTrue() {
        // given
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        ActivationToken token = new ActivationToken();
        token.setUser(savedUser);
        token.setToken("my-secret-token");
        token.setExpiredAt(LocalDateTime.now().plusDays(1));
        activationTokenRepository.save(token);

        // when
        int result = activationTokenRepository.confirmToken("my-secret-token");

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
