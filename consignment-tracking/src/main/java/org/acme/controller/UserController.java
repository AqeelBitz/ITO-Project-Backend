package org.acme.controller;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.models.User;
import org.acme.respository.UserRepository;

import java.sql.SQLException;
import java.util.*;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {


    public UserController() {
    }


    @Inject
    UserRepository userRepository;

    @GET
    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }
}
