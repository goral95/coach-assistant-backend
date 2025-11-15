package com.example.coachassistantbackend.Repository;

import com.example.coachassistantbackend.Entity.ActivationToken;
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
public class ActivationTokenRepositoryTest {

    private static final String EMAIL = "lukas@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String NAME = "Łukasz";
    private static final String SURNAME = "Górczyk";
    private static final String TOKEN = "my-secret-token";

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private static final User user = new User();
    private static final ActivationToken token = new ActivationToken();

    @BeforeEach()
    static void prepare() {
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(Role.USER);
        user.setName(NAME);
        user.setSurname(SURNAME);
        user.setBirthDate(LocalDateTime.now());

        token.setToken(TOKEN);
        token.setExpiredAt(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testFindByTokenShouldReturnRightToken() {
        // given
        User savedUser = userRepository.save(user);
        token.setUser(savedUser);
        activationTokenRepository.save(token);

        // when
        Optional<ActivationToken> result = activationTokenRepository.findByToken(TOKEN);

        // then
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getToken()).isEqualTo(TOKEN);
    }

    @Test
    void testFindByTokenShouldReturnEmptyOptional() {
        // given
        User savedUser = userRepository.save(user);
        token.setUser(savedUser);
        activationTokenRepository.save(token);

        // when
        Optional<ActivationToken> result = activationTokenRepository.findByToken("my-genius-token");

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testConfirmTokenShouldChangeTokenFiledConfirmedToTrue() {
        // given
        User savedUser = userRepository.save(user);
        token.setUser(savedUser);
        activationTokenRepository.save(token);

        // when
        int result = activationTokenRepository.confirmToken(TOKEN);

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
