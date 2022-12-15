package com.java.alex.crudapi.controller;

import com.java.alex.crudapi.entity.User;
import com.java.alex.crudapi.exception.ResourceNotFoundException;
import com.java.alex.crudapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent())
            return user.get();

        throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    @PostMapping
    public long createUser(@RequestBody User user) {
        return userRepository.save(user).getId();
    }

    @PutMapping("/{id}")
    public void updateUserById(@PathVariable("id")long userId, @RequestBody User user) throws ResourceNotFoundException {
        User saved = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        saved.setFirstName(user.getFirstName());
        saved.setLastName(user.getLastName());
        saved.setEmail(user.getEmail());

        userRepository.save(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id")long userId) throws ResourceNotFoundException {
        User saved = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(saved);

        return ResponseEntity.ok().build();
    }
}
