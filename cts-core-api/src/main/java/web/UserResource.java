package web;

import dto.UserLoginResponseDTO;
import services.UserDao;
import util.ResponseUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
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
            userDao.registerUser(request.username, request.password, request.userEmail, request.roleId, request.branchId);
            return Response.ok("User registered successfully").build();
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception properly
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error"+e).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(UserLoginRequest request) {
        try {
            System.out.println("=================> logged in");
            UserLoginResponseDTO userLoginResponseDTO = userDao.loginUser(request.username.toLowerCase(), request.password.toLowerCase(), request.branchCd);
            if (userLoginResponseDTO != null && userLoginResponseDTO.getToken()!=null) {
                return Response.ok(userLoginResponseDTO).build();
            } else {
                return ResponseUtil.buildErrorResponse(Response.Status.UNAUTHORIZED, "Invalid credentials");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            return ResponseUtil.buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    public static class UserRegistrationRequest {
        public String username;
        public String password;
        public String userEmail;
        public int roleId;
        public int branchId;
    }

    public static class UserLoginRequest {
        public String username;
        public String password;
        public String branchCd;
    }

    public static class TokenResponse {
        public String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }
}