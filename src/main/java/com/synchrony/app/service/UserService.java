package com.synchrony.app.service;

import com.synchrony.app.entity.User;
import com.synchrony.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImgurService imgurService;

    // create logger object for UserService class
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User save(User user) {
        // save user to repository
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        // find user by username from repository
        return userRepository.findByUsername(username);
    }

    public void deleteUser(User user) {
        // delete user from repository by id
        userRepository.deleteById(user.getId());
    }

    public void updateImage(User user, List<MultipartFile> imageFile) throws IOException {
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        // Upload the image to Imgur
        // Update the user's image URL
        List<String> urls = new ArrayList<>();
        if(user.getImageurl()!= null)
            urls.addAll(user.getImageurl());

        // upload the image(s) to Imgur and add the URLs to the user's existing image URL list
        urls.addAll(imgurService.uploadImage(imageFile));
        user.setImageurl(urls);
        save(user);

        logger.info("User's image(s) updated");
    }

    public void deleteImage(User user, String... imageid) {
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        List<String> imageurls = user.getImageurl();

        // Delete the image from Imgur
        if (imageurls != null) {
            if(imageid!= null) {
                // delete the specified image from Imgur
                imgurService.deleteImage(imageid[0]);

                // remove the deleted image URL from the user's image URL list
                for (String url: imageurls) {
                    if(url.indexOf(imageid[0])!=-1) {
                        imageurls.remove(url);
                        break;
                    }
                }

                user.setImageurl(imageurls);
                save(user);

                logger.info("User's image deleted");
            }
            else {
                // Update the user's image URL to null
                user.setImageurl(null);
                save(user);

                logger.info("User's image set to null");
            }
        }

    }
}
