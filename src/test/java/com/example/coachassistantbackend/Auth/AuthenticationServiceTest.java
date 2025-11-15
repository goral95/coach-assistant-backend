package com.example.coachassistantbackend.Auth;

import com.example.coachassistantbackend.Entity.ActivationToken;
import com.example.coachassistantbackend.Entity.Role;
import com.example.coachassistantbackend.Entity.User;
import com.example.coachassistantbackend.Exception.ObjectNotFoundException;
import com.example.coachassistantbackend.Exception.TokenExpiredException;
import com.example.coachassistantbackend.Exception.UserAlreadyExistsException;
import com.example.coachassistantbackend.Exception.UserNotFoundException;
import com.example.coachassistantbackend.Repository.UserRepository;
import com.example.coachassistantbackend.Service.ActivationTokenService;
import com.example.coachassistantbackend.Service.JwtService;
import com.example.coachassistantbackend.Util.TokenGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivationTokenService activationTokenService;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testConfirmTokenWithNoTokenExistShouldThrowObjectNotFoundException() {
        // given
        String token = "abcd";

        // when
        when(activationTokenService.getToken(token)).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.confirmToken(token))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("ActivationToken not found");
    }

    @Test
    void testConfirmWithAlreadyConfirmedUserShouldThrowUserAlreadyExistsException() {
        // given
        String token = "abcd";
        User user = new User();
        ActivationToken activationToken = new ActivationToken(token, user);
        activationToken.setConfirmed(true);

        // when
        when(activationTokenService.getToken(token)).thenReturn(Optional.of(activationToken));

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.confirmToken(token))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("Email already confirmed");
    }

    @Test
    void testConfirmTokenWithExpiredTokenShouldThrowTokenExpiredException() {
        // given
        String token = "abcd";
        User user = new User();

        ActivationToken activationToken = new ActivationToken(token, user);
        activationToken.setConfirmed(false);
        activationToken.setExpiredAt(LocalDateTime.now().minusHours(1));

        //when
        when(activationTokenService.getToken(token)).thenReturn(Optional.of(activationToken));

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.confirmToken(token))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("Token expired");
    }

    @Test
    void testConfirmTokenShouldReturnStringConfirmed() {
        // given
        String token = "abcd";
        User user = new User();
        user.setEmail("test@mail.com");

        ActivationToken activationToken = new ActivationToken(token, user);
        activationToken.setConfirmed(false);
        activationToken.setExpiredAt(LocalDateTime.now().plusHours(1));

        when(activationTokenService.getToken(token)).thenReturn(Optional.of(activationToken));

        // when
        String result = authenticationService.confirmToken(token);

        // then
        Assertions.assertThat(result).isEqualTo("confirmed");
        Mockito.verify(activationTokenService).confirmToken(token);
        Mockito.verify(userRepository).enableUser(user.getEmail());
    }

    @Test
    void testRegisterTheSameEmailThrowUserAlreadyExistsException() {
        // given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@email.com");
        User user = new User();
        user.setEmail("test@email.com");

        // when
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.register(registerRequest)).isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with email test@email.com already exist");
    }

    @Test
    public void testRegisterShouldReturnGeneratedToken() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("Lukas");
        request.setSurname("Podolski");
        request.setEmail("test@email.com");
        request.setBirthDate(LocalDateTime.now());
        request.setLicense("C");
        request.setPassword("password");
        String generatedToken = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail("lukas@gmail.com");
        user.setPassword("12345678");
        user.setRole(Role.USER);
        user.setName("Łukasz");
        user.setSurname("Górczyk");
        user.setBirthDate(LocalDateTime.now());

        // when
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(tokenGenerator.generateToken()).thenReturn(generatedToken);
        RegisterResponse response = authenticationService.register(request);

        // then
        Assertions.assertThat(response.getToken()).isEqualTo(generatedToken);
    }

    @Test
    void testAuthenticateUserNotFoundShouldThrowException() {
        // given
        AuthenticationRequest request = new AuthenticationRequest("email@test.com", "pass");

        // when
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.authenticate(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testAuthenticateInvalidCredentialsShouldThrowException() {
        // given
        AuthenticationRequest request = new AuthenticationRequest("user@mail.com", "pass");
        User user = new User();
        user.setEmail("user@mail.com");

        // when
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        Mockito.doThrow(new AuthenticationException("Bad credentials") {
                })
                .when(authenticationManager)
                .authenticate(Mockito.any());

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.authenticate(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Bad credentials");
    }

    @Test
    void testAuthenticateShouldReturnTokens() {
        // given
        AuthenticationRequest request = new AuthenticationRequest("user@mail.com", "pass");
        User user = new User();
        user.setEmail("user@mail.com");
        String jwt = "jwt-token";
        String refresh = "refresh-token";

        // when
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwt);
        when(jwtService.generateRefreshToken(user)).thenReturn(refresh);
        AuthenticationResponse response = authenticationService.authenticate(request);

        // then
        Assertions.assertThat(response.getAccessToken()).isEqualTo(jwt);
        Assertions.assertThat(response.getRefreshToken()).isEqualTo(refresh);
    }

    @Test
    void testRefreshTokenUserNotFoundShouldThrowException() {
        // given
        RefreshTokenRequest request = new RefreshTokenRequest("123");

        // when
        when(jwtService.extractUsername(request.getToken())).thenReturn("user@mail.com");
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> authenticationService.refreshToken(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testRefreshTokenShouldReturnNullWhenTokenInvalid() {
        // given
        RefreshTokenRequest request = new RefreshTokenRequest("123");
        User user = new User();
        user.setEmail("user@mail.com");

        // when
        when(jwtService.extractUsername(request.getToken())).thenReturn("user@mail.com");
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(request.getToken(), user)).thenReturn(false);
        AuthenticationResponse result = authenticationService.refreshToken(request);

        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    void testRefreshTokenShouldReturnNewAccessToken() {
        // given
        RefreshTokenRequest request = new RefreshTokenRequest("123");
        User user = new User();
        user.setEmail("user@mail.com");
        String newJwt = "new-access-token";

        // when
        when(jwtService.extractUsername(request.getToken())).thenReturn("user@mail.com");
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(request.getToken(), user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(newJwt);
        AuthenticationResponse response = authenticationService.refreshToken(request);

        // then
        Assertions.assertThat(response.getAccessToken()).isEqualTo(newJwt);
        Assertions.assertThat(response.getRefreshToken()).isEqualTo(request.getToken());
    }
}
