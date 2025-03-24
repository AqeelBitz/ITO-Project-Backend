package web;


//import org.bahl.entities.User;
import services.TokenService;
import services.UserDao;

import jakarta.annotation.security.PermitAll;
import javax.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserDao userDao;

    @POST
    @Path("/register")
    @PermitAll
    @Transactional

    public Response register(UserRegistrationRequest request) {
        try {
            userDao.registerUser(request.username, request.password, request.email, request.roleId, request.branchId);
            return Response.ok("User registered successfully").build();
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception properly
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error").build();
        }
    }

    @POST
    @Path("/login")
    public Response login(UserLoginRequest request) {
        try {
            String token = userDao.loginUser(request.username, request.password, request.branchCode);
            if (token != null) {
                return Response.ok(new TokenResponse(token)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception properly
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error").build();
        }
    }

    // Request and Response classes (inner classes or separate files)
    public static class UserRegistrationRequest {
        public String username;
        public String password;
        public String email;
        public int roleId;
        public int branchId;
    }

    public static class UserLoginRequest {
        public String username;
        public String password;
        public String branchCode;
    }

    public static class TokenResponse {
        public String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }
}