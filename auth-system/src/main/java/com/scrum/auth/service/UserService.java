package com.scrum.auth.service;

import com.scrum.auth.entity.User;
import com.scrum.auth.repository.UserRepository;
import com.scrum.auth.security.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher hasher;

    public UserService(UserRepository userRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    @Transactional
    public User registerUser(String email, String plainVal) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(hasher.hash(plainVal));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public boolean exists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
