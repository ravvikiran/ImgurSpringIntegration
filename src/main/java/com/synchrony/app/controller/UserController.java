package com.synchrony.app.controller;

import com.synchrony.app.entity.User;
import com.synchrony.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Create a new user
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Creating user: {}", user.getUsername());

        User savedUser = userService.save(user);
        logger.info("User created successfully: {}", savedUser.getUsername());

        return ResponseEntity.created(URI.create("/api/users/" + savedUser.getId())).body(savedUser);
    }

    // Check if the user is authenticated
    public boolean checkAuthentication(User user, String password) {
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    // Get user details by username and password
    @GetMapping("{username}/{password}")
    public ResponseEntity getUser(@PathVariable String username, @PathVariable String password) {
        logger.info("Getting user details for user: {}", username);

        User user = userService.findByUsername(username);
        boolean success = checkAuthentication(user, password);

        if (!success) {
            logger.warn("Unauthorized access attempted for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } else {
            logger.info("User details retrieved successfully for user: {}", username);
            return ResponseEntity.ok(user);
        }
    }

    // Update user's image
    @PutMapping("{username}/{password}/image")
    public ResponseEntity<String> login(@PathVariable String username, @PathVariable String password, @RequestParam("imageFile") List<MultipartFile> imageFiles) {
        logger.info("Updating user image for user: {}", username);

        User user = userService.findByUsername(username);
        boolean success = checkAuthentication(user, password);

        if (!success) {
            logger.warn("Unauthorized access attempted for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } else {
            try {
                userService.updateImage(user, imageFiles);
                logger.info("User image updated successfully for user: {}", username);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Image(s) uploaded succesfully for the User - " + user.getUsername());
            } catch (IOException e) {
                logger.error("Error occurred while updating user image for user: {}", username, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    // Delete user's image
    @DeleteMapping("/{imageid}/{username}/{password}")
    public ResponseEntity<String> deleteUserImage(@PathVariable String imageid, @PathVariable String username, @PathVariable String password) {
        logger.info("Deleting user image for user: {}", username);

        User user = userService.findByUsername(username);
        boolean success = checkAuthentication(user, password);

        if (!success) {
            logger.warn("Unauthorized access attempted for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } else {
            userService.deleteImage(user, imageid);
            logger.info("User image deleted successfully for user: {}", username);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Images deleted succesfully for the User - " + user.getUsername());
        }
    }
}


