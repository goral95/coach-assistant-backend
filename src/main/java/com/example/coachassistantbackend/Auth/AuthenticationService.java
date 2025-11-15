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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ActivationTokenService activationTokenService;
    private final TokenGenerator tokenGenerator;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                                 AuthenticationManager authenticationManager, ActivationTokenService activationTokenService,
                                 TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.activationTokenService = activationTokenService;
        this.tokenGenerator = tokenGenerator;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exist");
        }
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());
        user.setLicense(request.getLicense());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        User createdUser = userRepository.save(user);

        String token = tokenGenerator.generateToken();
        ActivationToken activationToken = new ActivationToken(token, createdUser);
        activationTokenService.save(activationToken);

        // TODO send email

        return new RegisterResponse(token);
    }

    @Transactional
    public String confirmToken(String token) {
        ActivationToken activationToken = activationTokenService.getToken(token)
                .orElseThrow(() -> new ObjectNotFoundException("ActivationToken not found"));

        if (activationToken.getConfirmed()) {
            throw new UserAlreadyExistsException("Email already confirmed");
        }

        if (activationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        activationTokenService.confirmToken(token);
        userRepository.enableUser(activationToken.getUser().getEmail());
        return "confirmed";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.getToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (jwtService.isTokenValid(request.getToken(), user)) {
            var jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(jwtToken, request.getToken());
        }
        return null;
    }

}
