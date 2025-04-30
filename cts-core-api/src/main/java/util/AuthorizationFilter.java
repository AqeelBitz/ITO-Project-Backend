package util;

import io.smallrye.jwt.auth.principal.JWTCallerPrincipalFactory;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logmanager.Level;
import org.jboss.logmanager.Logger;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class.getSimpleName());

    @Inject
    JWTCallerPrincipalFactory principalFactory;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.log(Level.INFO, "AuthorizationFilter called");

        String path = requestContext.getUriInfo().getPath();

        if (path.endsWith("/users/login") || path.endsWith("/users/register")) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        LOGGER.log(Level.INFO, "Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            LOGGER.log(Level.INFO,"token: ",token);

            try {
                // Get the JsonWebToken from the security context. This is the preferred method.
                JsonWebToken jwt = (JsonWebToken) requestContext.getSecurityContext().getUserPrincipal();

                // If the JWT is null, it means that the authentication was not successful.
                if (jwt == null) {
                    LOGGER.log(Level.ERROR, "JWT token cannot be parsed");
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                    return;
                }

                requestContext.setSecurityContext(new jakarta.ws.rs.core.SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return jwt;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        try {
                            Set<String> groups = jwt.getGroups();
                            LOGGER.log(Level.INFO, "Token Groups: " + groups);
                            LOGGER.log(Level.INFO, "Checking role: " + role);

                            if (groups != null) {
                                boolean hasRole = groups.contains(role);
                                LOGGER.log(Level.INFO, "Role check result (case-sensitive): " + hasRole);

                                // Optional: Case-insensitive check
                                boolean hasRoleIgnoreCase = groups.stream()
                                        .anyMatch(group -> group.equalsIgnoreCase(role));
                                LOGGER.log(Level.INFO, "Role check result (case-insensitive): " + hasRoleIgnoreCase);
                                return hasRole; // or hasRoleIgnoreCase depending on your need.
                            }
                            return false;
                        } catch (Exception e) {
                            LOGGER.log(Level.ERROR, "Error checking role: " + e.getMessage());
                            return false;
                        }
                    }

                    @Override
                    public boolean isSecure() {
                        return requestContext.getSecurityContext().isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "Bearer";
                    }
                });

            } catch (Exception e) {
                LOGGER.log(Level.ERROR, "Invalid JWT token: " + e.getMessage());
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            LOGGER.log(Level.ERROR, "Authorization header missing or invalid");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}