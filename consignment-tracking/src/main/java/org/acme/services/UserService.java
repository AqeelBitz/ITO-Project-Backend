package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.models.User;
import org.acme.respository.UserRepository;

import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }
}
