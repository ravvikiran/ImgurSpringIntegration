package com.synchrony.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table(name = "userdb")
public class User {

    private static final Logger logger = Logger.getLogger(User.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "imageurl")
    @JsonIgnore
    private List<String> imageurl;

    @Column(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("imageurl")
    public List<String> getImageurl() {
        logger.log(Level.INFO, "Getting image URLs of the user: " + this.username);
        return imageurl;
    }

    public void setImageurl(List<String> imageurl) {
        logger.log(Level.INFO, "Setting image URLs of the user: " + this.username);
        this.imageurl = imageurl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
