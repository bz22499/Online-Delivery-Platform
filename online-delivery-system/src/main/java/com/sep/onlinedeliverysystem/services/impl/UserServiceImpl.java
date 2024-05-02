package com.sep.onlinedeliverysystem.services.impl;

import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.repositories.UserRepository;
import com.sep.onlinedeliverysystem.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public List<User> findAll() {
       return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList()); //converting iterator to a list (easier to work with)
    }

    @Override
    public Optional<User> findOne(String email) {
        return userRepository.findById(email);
    }

    @Override
    public boolean Exists(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public User partialUpdate(String email, User userEntity) {
        userEntity.setEmail(email);

        return userRepository.findById(email).map(existingUser ->{
            Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
            Optional.ofNullable(userEntity.getFirstName()).ifPresent(existingUser::setFirstName);
            Optional.ofNullable(userEntity.getLastName()).ifPresent(existingUser::setLastName);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User doesn't exist"));
    }

    @Override
    public void delete(String email) {
        userRepository.deleteById(email);
    }

    @Override
    public boolean updateProfile(String email, String currentPassword, String newFirstName, String newLastName, String newPassword) {
        Optional<User> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check if the current password matches
            if (currentPassword.equals(user.getPassword())) {
                // Update the first name and last name fields
                user.setFirstName(newFirstName);
                user.setLastName(newLastName);
                // Update the password if a new password is provided
                if (newPassword != null && !newPassword.isEmpty()) {
                    user.setPassword(newPassword);
                }
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
