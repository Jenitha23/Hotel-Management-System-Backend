package com.usermanagement.service;

import com.usermanagement.dto.AuthResponse;
import com.usermanagement.dto.LoginRequest;
import com.usermanagement.dto.PasswordResetConfirm;
import com.usermanagement.dto.PasswordResetRequest;
import com.usermanagement.entity.ResetToken;
import com.usermanagement.entity.User;
import com.usermanagement.entity.VerificationToken;
import com.usermanagement.repository.ResetTokenRepository;
import com.usermanagement.repository.UserRepository;
import com.usermanagement.repository.VerificationTokenRepository;
import com.usermanagement.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified");
        }
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow();
        if (jwtUtil.validateToken(refreshToken, user)) {
            String newAccessToken = jwtUtil.generateToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);
            return new AuthResponse(newAccessToken, newRefreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    @Override
    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plus(24, ChronoUnit.HOURS));
        verificationTokenRepository.save(verificationToken);

        String link = baseUrl + "/api/auth/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify Your Email");
        message.setText("Click here to verify: " + link);
        mailSender.send(message);
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public void requestPasswordReset(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        resetTokenRepository.save(resetToken);

        String link = baseUrl + "/api/auth/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        message.setText("Click here to reset password: " + link);
        mailSender.send(message);
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirm confirm) {
        ResetToken resetToken = resetTokenRepository.findByToken(confirm.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(confirm.getNewPassword()));
        userRepository.save(user);
        resetTokenRepository.delete(resetToken);
    }
}